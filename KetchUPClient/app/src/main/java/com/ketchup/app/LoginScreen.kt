package com.ketchup.app

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginScreen : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_screen)
        //login and register buttons
        var loginButton: Button = findViewById(R.id.loginButton)
        var registerButton: Button = findViewById(R.id.signupButton)
        //Email and password texts
        var emailText : EditText = findViewById(R.id.usernameText)
        var passwordText : EditText = findViewById(R.id.passwordField)

        //takes to the chats selects
        loginButton.setOnClickListener {

            //checks if the fields are filled
            if(emailText.text.isNotEmpty() && passwordText.text.isNotEmpty()) {
                var intent: Intent
                intent = Intent(this, ChatMenu::class.java)
                startActivity(intent)
             }
            else if (emailText.text.isEmpty() && passwordText.text.isNotEmpty()) {
                Toast.makeText(applicationContext, "You must provide an user ", Toast.LENGTH_SHORT).show()
            }
            else if (emailText.text.isNotEmpty() && passwordText.text.isEmpty()) {
                Toast.makeText(applicationContext, "You must provide a password", Toast.LENGTH_SHORT).show()
            }
            else {
            Toast.makeText(applicationContext, "You must provide an user and a password", Toast.LENGTH_SHORT).show()
            }
        }
            //takes to the register screen
            registerButton.setOnClickListener {
            var intent: Intent
            intent = Intent(this,  RegisterScreen::class.java)
            startActivity(intent)
        }

    }

}