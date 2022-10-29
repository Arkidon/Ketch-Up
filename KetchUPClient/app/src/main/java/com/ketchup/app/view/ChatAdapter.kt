package com.ketchup.app.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ketchup.app.R
import com.ketchup.app.models.ChatData

class ChatAdapter (private val chatList: List<ChatData>): RecyclerView.Adapter<ChatViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            return ChatViewHolder(layoutInflater.inflate(R.layout.item_container_sent_message, parent, false))
        }

        override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
            val item = chatList[position]
            holder.render(item)
        }



        override fun getItemCount(): Int = chatList.size
    }

