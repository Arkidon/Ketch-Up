package com.ketchup.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isGone
import com.bumptech.glide.Glide
import com.ketchup.app.models.UserData
import com.makeramen.roundedimageview.RoundedImageView

class ChatScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_KetchUp)
        setContentView(R.layout.chat_screen)
        val spinner = findViewById<ProgressBar>(R.id.progressBar)
        spinner.isGone = false;
        val userName = intent.getStringExtra(friendName)
        val friendPFP = intent.getStringExtra(friendPFP)
        setUser(userName, friendPFP)
    }

    fun setUser(userName:String?, friendPFP:String?){
        findViewById<TextView>(R.id.chatName).apply { text = userName }
        val pfp = findViewById<RoundedImageView>(R.id.chatPFP)
        Glide.with(pfp.context).load(friendPFP).into(pfp)
    }
}