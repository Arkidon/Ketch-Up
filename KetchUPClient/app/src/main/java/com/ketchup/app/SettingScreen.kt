package com.ketchup.app

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView

class SettingScreen: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_KetchUp)
        setContentView(R.layout.settings_screen)
        val userName = intent.getStringExtra(selfName)
        val selfPFP = intent.getStringExtra(selfPFP)
        val selfStatus = intent.getStringExtra(selfStatus)
        setUser(userName, selfPFP, selfStatus)
    }
    fun setUser(userName:String?, selfPFP:String?, selfStatus:String?){
        findViewById<TextView>(R.id.changeName).apply { text = userName }
        findViewById<TextView>(R.id.changeStatus).apply { text = selfStatus }
        val pfp = findViewById<RoundedImageView>(R.id.changePFP)
        Glide.with(pfp.context).load(selfPFP).into(pfp)
    }
}