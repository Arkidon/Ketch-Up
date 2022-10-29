package com.ketchup.app.view

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ketchup.app.R
import com.ketchup.app.models.ChatData

class ChatViewHolder (view: View): RecyclerView.ViewHolder(view){
        val messageText = view.findViewById<TextView>(R.id.textMessage)
        val messageDate = view.findViewById<TextView>(R.id.textDate)

        fun render(chatData: ChatData){
        messageText.text = chatData.messageText
        messageDate.text = chatData.messageDate
    }
}