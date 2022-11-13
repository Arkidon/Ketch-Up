package com.ketchup.utils

import android.content.Context
import android.util.Log
import com.ketchup.app.KetchUp
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
                val userID = CredentialsManager.getCredential("id", KetchUp.getCurrentActivity())
                val sessionToken = CredentialsManager.getCredential("session-token", KetchUp.getCurrentActivity())

                // Retrieves the server url from disk
                val serverURL = "ws://" + ServerAddress.readUrl(context) + "/ws-test"
                Log.i(null, "Server URL: $serverURL")

                val factory = WebSocketFactory()

                webSocket = factory.createSocket(serverURL)

                // Adds headers for authentification
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
            Log.i(null, "Message sent $message")
            return true
        }

    }
}
