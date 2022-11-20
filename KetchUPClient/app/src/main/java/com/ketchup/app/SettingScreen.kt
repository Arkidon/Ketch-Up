package com.ketchup.app

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView

class SettingScreen: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        KetchUp.setCurrentActivity(this)
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_KetchUp)
        setContentView(R.layout.settings_screen)
        val userName = intent.getStringExtra("username")
        val selfPFP = intent.getStringExtra("profilePicture")
        val selfStatus = intent.getStringExtra("status")
        setUser(userName, selfPFP, selfStatus)
    }

    private fun setUser(userName:String?, selfPFP:String?, selfStatus:String?){
        findViewById<TextView>(R.id.changeName).apply { text = userName }
        findViewById<TextView>(R.id.changeStatus).apply { text = selfStatus }
        val pfp = findViewById<RoundedImageView>(R.id.changePFP)
        Glide.with(pfp.context).load(selfPFP).into(pfp)
    }
}