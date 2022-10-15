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
        spinner.isGone = true;
        setUser()
    }

    fun setUser(){
        val username = findViewById<TextView>(R.id.chatName)
        val pfp = findViewById<RoundedImageView>(R.id.chatPFP)
        Glide.with(pfp.context).load("https://pbs.twimg.com/media/E_XBP6iWYAIJpZk?format=png&name=small").into(pfp)
        username.text = "Arkidon"
    }
}