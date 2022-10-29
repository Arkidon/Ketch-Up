package com.ketchup.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ketchup.utils.ShowToast
import com.ketchup.utils.UrlFile

class DevScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_KetchUp)
        setContentView(R.layout.debug_settings_screen)

        // Button and fields
        val devButton: Button = findViewById(R.id.devButton)
        val urlField : EditText = findViewById(R.id.urlField)

        if (UrlFile.readUrl(this) != "http://127.0.0.1"){
            urlField.hint = UrlFile.readUrl(this)

        }

        devButton.setOnClickListener{
            if (urlField.text ==null){
                ShowToast.showToast(this, "You must provide an url", Toast.LENGTH_SHORT)
                return@setOnClickListener
            }
                UrlFile.writeUrl(urlField.text.toString(), this)
                startActivity(Intent(this,LoginScreen::class.java))
            }
        }

    override fun onResume() {
        super.onResume()
        val urlField : EditText = findViewById(R.id.urlField)
        if (UrlFile.readUrl(this) != "http://127.0.0.1"){
            urlField.hint = UrlFile.readUrl(this)
        }
    }

}
