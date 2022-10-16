package com.ketchup.app

import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ketchup.app.models.UserData
import com.ketchup.app.view.UserAdapter
import com.makeramen.roundedimageview.RoundedImageView


open class ChatMenu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_KetchUp)
        setContentView(R.layout.chat_menu)
        initRecyclerView()
        setUser()
        val spinner = findViewById<ProgressBar>(R.id.progressBar)
        spinner.isGone = true;

    }

    private fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.usersRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = UserAdapter(UserList.userList) { userData -> onItemSelected(userData) }
    }

    fun onItemSelected(userData: UserData){
        var goChat: Intent
        goChat = Intent(this, ChatScreen::class.java)
        startActivity(goChat)

    }

    fun setUser(){
        val username = findViewById<TextView>(R.id.textName)
        val status = findViewById<TextView>(R.id.userStatus)
        val pfp = findViewById<RoundedImageView>(R.id.userPFP)
        Glide.with(pfp.context).load("https://static.tvtropes.org/pmwiki/pub/images/maddyandtheo.png").into(pfp)
        username.text = "Madeline"
        status.text = "Strawberry Pie"}
}