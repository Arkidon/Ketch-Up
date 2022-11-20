package com.ketchup.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ketchup.utils.files.ServerAddress

// Activity that selects the main activity
class ActivityManager: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // If url is default takes to dev activity
        if( ServerAddress.readUrl(this).equals("127.0.0.1")){
            startActivity(Intent(this, DevScreen::class.java))
        } else {
            startActivity(Intent(this, LoginScreen::class.java))
        }
        finish()

    }
}