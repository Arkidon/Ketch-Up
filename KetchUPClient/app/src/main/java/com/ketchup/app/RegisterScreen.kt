package com.ketchup.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast


class RegisterScreen : AppCompatActivity() {
    //var to make toasts
    var toast: Toast? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_screen)

        //User and password texts
        var userText: EditText = findViewById(R.id.userSingField)
        var passwordText: EditText = findViewById(R.id.passwordSingField)
        var passwordRepText: EditText = findViewById(R.id.passwordRepSignField)
        //Sing up button
        var singUpButton: Button = findViewById(R.id.signupButton)


        singUpButton.setOnClickListener {

            //checks if the fields are filled
            if(userText.text.isNotEmpty() && passwordText.text.isNotEmpty()  && passwordText.text.contentEquals(passwordRepText.text)) {
                var intent: Intent
                intent = Intent(this, LoginScreen::class.java)
                startActivity(intent)
            }
            else if (userText.text.isEmpty() && passwordText.text.isNotEmpty() && passwordRepText.text.isNotEmpty()) {
                passwordRepText.text = null
                passwordText.text = null
              showToast("You must provide an user", Toast.LENGTH_SHORT)
            }
            else if (userText.text.isNotEmpty() && passwordText.text.isEmpty() && passwordRepText.text.isNotEmpty()) {
                passwordRepText.text = null
                showToast("You must provide a password", Toast.LENGTH_SHORT)
            } else if ( userText.text.isNotEmpty() && passwordText.text.isNotEmpty() && passwordRepText.text.isEmpty()) {
                passwordText.text = null
                showToast("Your must repeat your password", Toast.LENGTH_SHORT)
                //checks if pass match
            }else if (!(passwordText.text.contentEquals(passwordRepText.text)) && passwordText.text.isNotEmpty())
                showToast("Your passwords must match", Toast.LENGTH_SHORT)
            else
            {
                showToast("You must provide an user and a password", Toast.LENGTH_SHORT)
            }
        }
    }
    fun showToast(text: CharSequence?, duration: Int) {
        if (toast == null) toast = Toast.makeText(applicationContext, text, duration)
        else{
            toast?.cancel()
            toast?.setText(text)
        }
        toast?.show()
    }
}