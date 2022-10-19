package com.ketchup.app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ketchup.utils.CreateDatabase
import com.ketchup.utils.ShowToast

const val username = "com.ketchup.app.selfUSERNAME"

class LoginScreen : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_KetchUp)
        setContentView(R.layout.login_screen)
        //login and register buttons
        val loginButton: Button = findViewById(R.id.loginButton)
        val registerButton: Button = findViewById(R.id.signupButton)
        //Email and password texts
        val userText : EditText = findViewById(R.id.usernameText)
        val passwordText : EditText = findViewById(R.id.passwordField)
        //creates the database
        val db = CreateDatabase.createBd(this)
        val userDao = db.userDao()
        // user to use the database
        //keyboard compatibility
        userText.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                // If the event is a key-down event on the "enter" button
                if (event.action == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    if(userText.text.isNotEmpty() && passwordText.text.isNotEmpty()) {
                        var intent: Intent =
                            Intent(applicationContext, ChatMenu::class.java).apply { putExtra(username, userText.text.toString())}
                        startActivity(intent)
                        finish()
                    }
                    return true
                }
                return false
            }
        })
        passwordText.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                // If the event is a key-down event on the "enter" button
                if (event.action == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    if(userText.text.isNotEmpty() && passwordText.text.isNotEmpty()) {
                        val intent: Intent =
                            Intent(applicationContext, ChatMenu::class.java).apply { putExtra(username, userText.text.toString())}
                        startActivity(intent)
                        finish()
                    }

                    return true
                }
                return false
            }
        })


        //takes to the chats selects
        loginButton.setOnClickListener {

            //checks if the fields are filled
            if(userText.text.isNotEmpty() && passwordText.text.isNotEmpty()) {
                 val userdb = userDao.findByName(username)
                 if (!userdb.equals(username)) {
                     ShowToast.showToast(this, "Username not found", Toast.LENGTH_SHORT)
                 }else
                 {
                     val intent: Intent =
                         Intent(this, ChatMenu::class.java).apply { putExtra(username, userText.text.toString())}
                     startActivity(intent)
                     finish()
                 }
             }
            else if (userText.text.isEmpty() && passwordText.text.isNotEmpty()) {
                passwordText.text = null
                ShowToast.showToast(this,"You must provide an user", Toast.LENGTH_SHORT)
            }
            else if (userText.text.isNotEmpty() && passwordText.text.isEmpty()) {
                ShowToast.showToast(this,"You must provide a password", Toast.LENGTH_SHORT)
            }
            else {
               ShowToast.showToast(this,"You must provide an user and a password", Toast.LENGTH_SHORT)
            }
        }
            //takes to the register screen
            registerButton.setOnClickListener {
            val intent: Intent = Intent(this,  RegisterScreen::class.java)
            startActivity(intent)
                finish()
        }

    }




}