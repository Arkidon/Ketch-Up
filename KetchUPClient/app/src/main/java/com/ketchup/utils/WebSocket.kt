package com.ketchup.utils

import android.content.Context
import android.util.Log
import com.ketchup.app.KetchUp
import com.ketchup.app.database.AppDatabase
import com.ketchup.utils.files.CredentialsManager
import com.ketchup.utils.files.ServerAddress
import com.neovisionaries.ws.client.*
import org.json.JSONException
import org.json.JSONObject


class ChatWebSocket{
    companion object{
        private var webSocket: WebSocket? = null

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
                            "incoming_message" -> handleNewTextMessage(jsonResponse)
                        }

                    }

                    override fun onConnectError(websocket: WebSocket?, exception: WebSocketException?) {
                        super.onConnectError(websocket, exception)
                        exception?.printStackTrace()
                    }

                    override fun onDisconnected(websocket: WebSocket?, serverCloseFrame: WebSocketFrame?,
                                                clientCloseFrame: WebSocketFrame?, closedByServer: Boolean) {

                        super.onDisconnected(websocket, serverCloseFrame, clientCloseFrame,closedByServer)
                        Log.i(null, "WebSocket Disconnected")

                    }
                })

                webSocket?.connectAsynchronously()
            }
        }

        fun closeConnection(){
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
        fun handleNewTextMessage(message: JSONObject){
            // Gets the current activity
            val currentActivity = KetchUp.getCurrentActivity()

            // Creates an instance to access the room database
            val databaseInstance = AppDatabase.createInstance(currentActivity)
            val dataAccessObject = databaseInstance?.userDao()

            // Gets the values from the JSON message

            /*

            val entryId = message.getInt("entry_id")
            val text = message.getString("text")
            val userSender = message.getInt("user_sender")
            val chatId = message.getInt("chat_id")
            val responseTo = message.getInt("response_to")

            chatEntry = ChatEntries()

            dataAccessObject.insertChat()

             */
            if (currentActivity.javaClass.simpleName == "ChatScreen" ){

            }
        }

    }
}
