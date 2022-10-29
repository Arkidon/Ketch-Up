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
const val selfName = "com.ketchup.app.selfname"
const val selfStatus = "com.ketchup.app.status"
const val selfPFP = "com.ketchup.app.selfPFP"

 class ChatMenu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_KetchUp)
        setContentView(R.layout.chat_menu)
        val username = intent.getStringExtra(username)
        val spinner = findViewById<ProgressBar>(R.id.progressBar)
        val pfp = findViewById<RoundedImageView>(R.id.userPFP)
        val selfpfp = "https://static.tvtropes.org/pmwiki/pub/images/maddyandtheo.png"
        setUser(username, pfp, selfpfp)
        initRecyclerView()
        val status = findViewById<TextView>(R.id.userStatus)
        pfp.setOnClickListener{profileSelected(username, selfpfp, status.text.toString())}
        spinner.isGone = true

    }

    private fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.usersRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = UserAdapter(UserList.userList) { userData -> onItemSelected(userData) }
    }

    private fun profileSelected(username: String?, selfpfp: String, status: String){
        val extras = Bundle()
        val goSettings = Intent(this, SettingScreen::class.java)
        extras.putString(selfName, username)
        extras.putString(selfStatus, status)
        extras.putString(selfPFP, selfpfp)
        goSettings.putExtras(extras)
        startActivity(goSettings)

    }

    private fun onItemSelected(userData: UserData){
        val extras = Bundle()
        val goChat = Intent(this, ChatScreen::class.java)
        extras.putString(friendName, userData.friendName)
        extras.putString(friendPFP, userData.pfp)
        goChat.putExtras(extras)
        startActivity(goChat)

    }

    private fun setUser(username:String?, pfp:RoundedImageView, selfpfp:String){
        // val user_name = findViewById<TextView>(R.id.textName).apply { text = username }
        val status = findViewById<TextView>(R.id.userStatus)
        Glide.with(pfp.context).load(selfpfp).into(pfp)
        status.text = "Strawberry Pie"}
}