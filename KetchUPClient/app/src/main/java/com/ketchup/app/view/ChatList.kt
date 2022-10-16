package com.ketchup.app.view

import com.ketchup.app.models.ChatData
import com.ketchup.app.models.UserData
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChatList {
    companion object {
        var chatList = listOf<ChatData>(
        )
    }
}