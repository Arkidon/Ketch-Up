package com.ketchup.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class DevScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_KetchUp)
        setContentView(R.layout.debug_settings_screen)
    }
}