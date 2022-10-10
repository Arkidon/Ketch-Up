package com.ketchup.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


class ChatMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_KetchUp)
        setContentView(R.layout.chat_menu)
    }
}