package com.ketchup.app

import android.content.Intent
import android.os.Bundle
<<<<<<< HEAD
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
=======
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
>>>>>>> bd9e7b3e8a131ced20e43b377125e90ce8be5cc9
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

<<<<<<< HEAD
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
        Glide.with(pfp.context).load("https://avatars.githubusercontent.com/u/47338871?v=4").into(pfp)
        username.text = "Elon Musk"
        status.text = "Twitter what?"
=======
    private fun addUser(){
        val inflter = LayoutInflater.from(this)
        val v = inflter.inflate(R.layout.chat_menu,null)
        val userName = findViewById<TextView>(R.id.textName)
        val userPFP = findViewById<RoundedImageView>(R.id.pfp)
        userList.add(UserData("Name : William", getDrawable(william_pfp)))
        userName.text = userList.get(userList.size-1).username
        userPFP.background = userList.get(userList.size-1).friendPFP

>>>>>>> bd9e7b3e8a131ced20e43b377125e90ce8be5cc9
    }
}