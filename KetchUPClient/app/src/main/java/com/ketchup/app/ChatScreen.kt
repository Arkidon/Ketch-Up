package com.ketchup.app

import android.os.Bundle
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ketchup.app.database.ChatEntries
import com.ketchup.app.models.ChatData
import com.ketchup.app.view.ChatAdapter
import com.ketchup.app.view.ChatList.Companion.chatList
import com.ketchup.utils.ChatWebSocket
import com.ketchup.utils.ImagePFP
import com.makeramen.roundedimageview.RoundedImageView
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChatScreen : AppCompatActivity() {

    private var chatEntries = mutableListOf<ChatEntries>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Updates the current activity reference
        KetchUp.setCurrentActivity(this)

        // Establish the WebSocket connection if it is not already established
        ChatWebSocket.createConnection(this)

        // Changes the theme to remove the background used for the splash screen
        setTheme(R.style.Theme_KetchUp)

        // Sets the content for the activity
        setContentView(R.layout.chat_screen)

        val userName = intent.getStringExtra(friendName)
        val friendPFP = intent.getStringExtra(friendPFP)
        setUser(userName, friendPFP)
        initRecyclerView()
        val sendButton : FloatingActionButton = findViewById(R.id.sendButton)

        sendButton.setOnClickListener {
            sendMessage()
            initRecyclerView()
        }

    }

    private fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.chatRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this).apply {
            this.stackFromEnd = true
        }
        val user = findViewById<TextView>(R.id.chatName)
        recyclerView.adapter = ChatAdapter(chatList.filter {
                chatData -> chatData.user == user.text.toString()
        })
    }

    private fun setUser(userName:String?, friendPFP:String?){
        findViewById<TextView>(R.id.chatName).apply { text = userName }
        val pfp = findViewById<RoundedImageView>(R.id.chatPFP)
        pfp.setImageBitmap(friendPFP?.let { ImagePFP.readImageFromDisk(this, it)})
    }

    private fun sendMessage(){
        // Gets the message
        val message : EditText = findViewById(R.id.inputMessage)

        val user : TextView = findViewById(R.id.chatName)

        ChatWebSocket.sendMessage(message.text.toString())
        val newMessage = listOf(ChatData(message.text.toString(),
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")),
            user.text.toString()))

        chatList = chatList + newMessage
        val recyclerView = findViewById<RecyclerView>(R.id.chatRecyclerView)
        recyclerView.adapter!!.notifyItemInserted(recyclerView.size-1)
        message.text = null
    }

    /**
     * Appends a new element to the Recycler View List
     */
    public fun appendMessage(chatEntry: ChatEntries){
        // Appends the new chat entry to the list
        chatEntries.add(chatEntry)

        // Send the message to the server


        // Notifies the recycler view that a new item has been inserted
        val recyclerView = findViewById<RecyclerView>(R.id.chatRecyclerView)
        recyclerView.adapter!!.notifyItemInserted(recyclerView.size-1)
    }
}