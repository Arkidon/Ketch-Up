package com.ketchup.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ketchup.app.models.ChatData
import com.ketchup.app.models.UserData
import com.ketchup.app.view.ChatAdapter
import com.ketchup.app.view.ChatList
import com.ketchup.app.view.ChatList.Companion.chatList
import com.ketchup.app.view.UserAdapter
import com.makeramen.roundedimageview.RoundedImageView
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChatScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_KetchUp)
        setContentView(R.layout.chat_screen)
        val spinner = findViewById<ProgressBar>(R.id.progressBar)
        val userName = intent.getStringExtra(friendName)
        val friendPFP = intent.getStringExtra(friendPFP)
        setUser(userName, friendPFP)
        initRecyclerView()
        val sendButton : FloatingActionButton = findViewById(R.id.sendButton)
        sendButton.setOnClickListener{sendMessage();initRecyclerView()}
        spinner.isGone = true;
    }

    private fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.chatRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val user = findViewById<TextView>(R.id.chatName)
        recyclerView.adapter = ChatAdapter(ChatList.chatList.filter { chatData -> chatData.user.equals(user.text.toString())})
    }

    fun setUser(userName:String?, friendPFP:String?){
        findViewById<TextView>(R.id.chatName).apply { text = userName }
        val pfp = findViewById<RoundedImageView>(R.id.chatPFP)
        Glide.with(pfp.context).load(friendPFP).into(pfp)
    }

    fun sendMessage(){
        val message : EditText = findViewById(R.id.inputMessage)
        val user : TextView = findViewById(R.id.chatName)
        val newMessage = listOf<ChatData>(ChatData(message.text.toString(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")), user.text.toString()))
        chatList = chatList + newMessage
        val recyclerView = findViewById<RecyclerView>(R.id.chatRecyclerView)
        recyclerView.adapter!!.notifyDataSetChanged()
        recyclerView.scrollToPosition(recyclerView.bottom)
        message.text = null
    }
}