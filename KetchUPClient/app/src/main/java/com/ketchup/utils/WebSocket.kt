package com.ketchup.utils

class WebSocket {
    var instance: WebSocket? = null
        get() {
            if (field == null) {
                field = WebSocket()
            }
            return field
        }
        private set
}