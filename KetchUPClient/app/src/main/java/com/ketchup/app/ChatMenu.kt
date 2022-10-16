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

const val friendName = "com.ketchup.app.USERNAME"
const val friendPFP = "com.ketchup.app.PFP"

 class ChatMenu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_KetchUp)
        setContentView(R.layout.chat_menu)
        initRecyclerView()
        val username = intent.getStringExtra(username)
        setUser(username)
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
        val extras = Bundle()
        goChat = Intent(this, ChatScreen::class.java)
        extras.putString(friendName, userData.friendName)
        extras.putString(friendPFP, userData.pfp)
        goChat.putExtras(extras)
        startActivity(goChat)

    }

    fun setUser(username:String?){
        val user_name = findViewById<TextView>(R.id.textName).apply { text = username }
        val status = findViewById<TextView>(R.id.userStatus)
        val pfp = findViewById<RoundedImageView>(R.id.userPFP)
        Glide.with(pfp.context).load("https://static.tvtropes.org/pmwiki/pub/images/maddyandtheo.png").into(pfp)
        status.text = "Strawberry Pie"}
}