package com.ketchup.utils

import okhttp3.*

class ChatWebSocketListener : WebSocketListener() {

    override fun onOpen(webSocket: WebSocket, response: Response) {
        // Handles the websocket connection opening
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        // Handles the message received event
        println(text)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        // Handles the websocket connection closing
    }

}

class ChatWebSocket{
    companion object{
        private var webSocket: WebSocket? = null

        fun createConnection(){
            val client = OkHttpClient()
            val request = Request.Builder().url("ws://127.0.0.1:8000/ws-test").build()
            val listener = ChatWebSocketListener()

            webSocket = client.newWebSocket(request, listener)
        }

        fun closeConnection(){
            webSocket?.close(1000, "")
        }

        fun sendMessage(message: String){
            webSocket?.send(message)
            println("Message sent $message")
        }
    }
}
