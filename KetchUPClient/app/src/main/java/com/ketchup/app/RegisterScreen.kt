package com.ketchup.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.ketchup.utils.ShowToast


class RegisterScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_KetchUp)
        setContentView(R.layout.activity_register_screen)

        //User and password texts
        var userText: EditText = findViewById(R.id.userSingField)
        var passwordText: EditText = findViewById(R.id.passwordSingField)
        var passwordRepText: EditText = findViewById(R.id.passwordRepSignField)
        //Sing up button
        var singUpButton: Button = findViewById(R.id.signupButton)

        passwordText.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                // If the event is a key-down event on the "enter" button
                if (event.action == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    if(userText.text.isNotEmpty() && passwordText.text.isNotEmpty()  && passwordText.text.contentEquals(passwordRepText.text)) {
                        changeActivityLogin()
                    }

                    return true
                }
                return false
            }
        })
        passwordRepText.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                // If the event is a key-down event on the "enter" button
                if (event.action == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    if(userText.text.isNotEmpty() && passwordText.text.isNotEmpty()  && passwordText.text.contentEquals(passwordRepText.text)) {
                        changeActivityLogin()
                    }
                    return true
                }
                return false
            }
        })
        userText.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                // If the event is a key-down event on the "enter" button
                if (event.action == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    if(userText.text.isNotEmpty() && passwordText.text.isNotEmpty()  && passwordText.text.contentEquals(passwordRepText.text)) {
                        changeActivityLogin()
                    }

                    return true
                }
                return false
            }
        })




        singUpButton.setOnClickListener {

            //checks if the fields are filled
            //all goooooood
            if(userText.text.isNotEmpty() && passwordText.text.isNotEmpty()  && passwordText.text.contentEquals(passwordRepText.text)) {
                changeActivityLogin()
            }else if (userText.text.isEmpty()) {
                passwordRepText.text = null
                passwordText.text = null
                ShowToast.showToast(this,"You must provide an user", Toast.LENGTH_SHORT)
            }else if (userText.text.isNotEmpty() && passwordText.text.isEmpty() && passwordRepText.text.isNotEmpty()) {
                passwordRepText.text = null
                ShowToast.showToast(this,"You must provide a password", Toast.LENGTH_SHORT)
            }else if ( userText.text.isNotEmpty() && passwordText.text.isNotEmpty() && passwordRepText.text.isEmpty()) {
                passwordText.text = null
                ShowToast.showToast(this, "Your must repeat your password", Toast.LENGTH_SHORT)
            }else if (userText.text.isNotEmpty() && passwordText.text.isEmpty() && passwordRepText.text.isEmpty()){
                ShowToast.showToast(this,"Your must provide a password and repeat it", Toast.LENGTH_SHORT)
                //checks if pass match
            }else if (!(passwordText.text.contentEquals(passwordRepText.text)) && passwordText.text.isNotEmpty()) {
                ShowToast.showToast(this, "Your passwords must match", Toast.LENGTH_SHORT)
            }else
            {
                ShowToast.showToast(this,"You must provide an user and a password", Toast.LENGTH_SHORT)
            }
        }
    }
    // function to change activity to login and finish this activity
    fun changeActivityLogin(){
        val intent = Intent(this, LoginScreen::class.java)
        startActivity(intent)
        finish()

    }
}