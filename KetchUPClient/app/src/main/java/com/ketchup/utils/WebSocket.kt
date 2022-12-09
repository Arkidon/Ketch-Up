package com.ketchup.utils

import android.content.Context
import android.util.Log
import com.ketchup.app.ChatScreen
import com.ketchup.app.KetchUp
import com.ketchup.app.database.AppDatabase
import com.ketchup.app.database.ChatEntries
import com.ketchup.app.view.ChatList
import com.ketchup.utils.files.CredentialsManager
import com.ketchup.utils.files.ServerAddress
import com.neovisionaries.ws.client.*
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject


class ChatWebSocket{
    companion object{
        private var webSocket: WebSocket? = null
        private var keepConnection = true

        fun createConnection(context: Context){
            if(webSocket == null){
                // Retrieves the credentials from the shared preferences
                val userID = CredentialsManager.getCredential("user", KetchUp.getCurrentActivity())
                val sessionToken = CredentialsManager.getCredential("session-token", KetchUp.getCurrentActivity())

                // Retrieves the server url from disk
                val serverURL = "ws://" + ServerAddress.readUrl(context) + "/ws-test"
                Log.i(null, "Server URL: $serverURL")

                // Creates the WebSocket instance
                val factory = WebSocketFactory()
                webSocket = factory.createSocket(serverURL)

                // Adds headers for authentication
                webSocket?.addHeader("user", userID)
                webSocket?.addHeader("session", sessionToken)

                webSocket?.addListener(object : WebSocketAdapter() {
                    /**
                     * Handles the incoming messages
                     */
                    override fun onTextMessage(websocket: WebSocket, message: String){
                        Log.i(null, "Message received: $message")

                        // Creates the JSON Object from the response
                        val jsonResponse: JSONObject

                        try {
                            jsonResponse = JSONObject(message)
                        }
                        catch (e: JSONException){
                            // Aborts the method if the received message is not a JSON String
                            return
                        }

                        // Checks the message type and calls the
                        // appropriate handler for that message
                        when( jsonResponse.getString("type")){
                            "incoming_message" -> handleNewIncomingMessage(jsonResponse)

                            /*
                            Calls to the same function for testing and demo purposes,
                            the intended handler is the handleMessageValidation function
                             */
                            "message_confirmation" -> handleNewIncomingMessage(jsonResponse)
                        }
                    }

                    override fun onConnectError(websocket: WebSocket?, exception: WebSocketException?) {
                        Log.i(null, "Connection Error")
                        // Sets the websocket object to null
                        webSocket = null

                        // Tries to reconnect with the server
                        if(keepConnection){
                            Thread.sleep(1000)
                            createConnection(context)
                        }
                    }

                    override fun onDisconnected(websocket: WebSocket?, serverCloseFrame: WebSocketFrame?,
                                                clientCloseFrame: WebSocketFrame?, closedByServer: Boolean) {
                        Log.i(null, "WebSocket Disconnected")

                        // Sets the websocket object to null
                        webSocket = null

                        // Tries to reconnect with the server
                        if(keepConnection){
                            createConnection(context)
                        }
                    }

                    override fun onConnected(websocket: WebSocket?, headers: MutableMap<String, MutableList<String>>?) {
                        Log.i(null, "Connected")
                    }
                })

                webSocket?.connectAsynchronously()
            }
        }

        /**
         * Closes the websocket connection and updates the keepConection
         * variable so the websocket does not try to reconnect
         */
        fun closeConnection(){
            keepConnection = false
            webSocket?.disconnect()
        }

        fun sendMessage(message: String): Boolean {
            webSocket?.sendText(message)
            Log.i(null, "Message sent $message")
            return true
        }

        /**
         * Handles an incoming message and stores it in the local database.
         * If the message belongs to a chat that is currently open,
         * appends it to the recycler view
         */
        fun handleNewIncomingMessage(message: JSONObject){
            // Gets the current activity
            val currentActivity = KetchUp.getCurrentActivity()

            // Creates an instance to access the room database
            val databaseInstance = AppDatabase.createInstance(currentActivity)
            val dataAccessObject = databaseInstance?.userDao()

            // Gets the values from the JSON message
            val entryId = message.getInt("entry_id")
            val text = message.getString("message")
            val userSender = message.getInt("user_sender")
            val chatId = message.getInt("chat_id")
            val responseTo = null

            // Creates a chat entry object with some temporal placeholders
            val chatEntry = ChatEntries(entryId, text, userSender, chatId, responseTo, null, false, false)

            // Stores the object in the database
            dataAccessObject?.insertEntries(chatEntry)

            /* If the current activity is a ChatScreen with the
               same id than the received message, appends it to
               the recycler view */
            if ((currentActivity is ChatScreen) && (currentActivity.chatId == chatId)){
                currentActivity.appendMessage(chatEntry)
            }
        }

        /**
         * Handles a message validation received from the server and appends the message
         * to the recycler view
         */
        fun handleMessageValidation(message: JSONObject){

        }
    }
}
