package com.ketchup.app.view

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ketchup.app.R
import com.ketchup.app.database.ChatEntries
import java.time.LocalDate

class ChatViewHolder (view: View): RecyclerView.ViewHolder(view){
    val messageText = view.findViewById<TextView>(R.id.textMessage)
    val messageDate = view.findViewById<TextView>(R.id.textDate)

    fun render(chatEntry: ChatEntries){
        // Sets the text value, retrieved from the ChatEntries object
        messageText.text = chatEntry.text

        // Placeholder date value
        messageDate.text = LocalDate.now().toString()
    }
}