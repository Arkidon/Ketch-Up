package com.ketchup.utils

import android.content.Context
import android.util.Log
import com.ketchup.app.KetchUp
import com.ketchup.utils.files.CredentialsManager
import com.ketchup.utils.files.ServerAddress
import com.neovisionaries.ws.client.WebSocket
import com.neovisionaries.ws.client.WebSocketAdapter
import com.neovisionaries.ws.client.WebSocketException
import com.neovisionaries.ws.client.WebSocketFactory


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
                    override fun onTextMessage(websocket: WebSocket, message: String){
                        Log.i(null, "Mensaje recibido: $message")
                    }

                    override fun onConnectError(websocket: WebSocket?, exception: WebSocketException?) {
                        super.onConnectError(websocket, exception)
                        exception?.printStackTrace()
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

    }
}
