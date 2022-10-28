package com.ketchup.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ketchup.utils.ShowToast
import org.json.JSONObject


class RegisterScreen : AppCompatActivity() {

    lateinit var userText: EditText
    lateinit var passwordText: EditText
    lateinit var passwordRepText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_KetchUp)
        setContentView(R.layout.activity_register_screen)

        //User and password texts
        userText = findViewById(R.id.userSingField)
        passwordText = findViewById(R.id.passwordSingField)
        passwordRepText = findViewById(R.id.passwordRepSignField)

        //Sing up button
        val singUpButton: Button = findViewById(R.id.signupButton)

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

    private fun signUp(){

        // A few validations
        if (userText.text.isEmpty()) {
            passwordRepText.text = null
            passwordText.text = null
            ShowToast.showToast(this,"You must provide an user", Toast.LENGTH_SHORT)
            return
        }

        if (userText.text.isNotEmpty() && passwordText.text.isEmpty() && passwordRepText.text.isNotEmpty()) {
            passwordRepText.text = null
            ShowToast.showToast(this,"You must provide a password", Toast.LENGTH_SHORT)
            return
        }

        if (userText.text.isNotEmpty() && passwordText.text.isNotEmpty() && passwordRepText.text.isEmpty()) {
            passwordText.text = null
            ShowToast.showToast(this, "Your must repeat your password", Toast.LENGTH_SHORT)
            return
        }

        if (userText.text.isNotEmpty() && passwordText.text.isEmpty() && passwordRepText.text.isEmpty()){
            //checks if passwords match
            ShowToast.showToast(this,"Your must provide a password and repeat it", Toast.LENGTH_SHORT)
            return
        }

        if (!(passwordText.text.contentEquals(passwordRepText.text)) && passwordText.text.isNotEmpty()) {
            ShowToast.showToast(this, "Your passwords must match", Toast.LENGTH_SHORT)
            return
        }

        if(userText.text.isEmpty() && passwordText.text.isEmpty())
        {
            ShowToast.showToast(this,"You must provide an user and a password", Toast.LENGTH_SHORT)
            return
        }


        val queue = Volley.newRequestQueue(this)
        val url = "http://192.168.146.19:8000"

        val json: JSONObject = JSONObject()
        json.put("username", userText.text.toString())
        json.put("password", passwordText.text.toString())

        if(!(userText.text.isNotEmpty() && passwordText.text.isNotEmpty() && passwordRepText.text.equals(passwordText.text))) {
            Log.i(null, "Fields are empty :")
            return
        }

        val request = JsonObjectRequest(
            Request.Method.POST, url, json,
            // Success response handle
            { response ->
                Log.i(null, response.toString())
            },

            // Error response handle
            { error ->
                val status = error.networkResponse.statusCode.toString();
                if (status.equals("409")){
                    ShowToast.showToast(this, "Users already exits",Toast.LENGTH_SHORT)
                    return@JsonObjectRequest
                }
                if (status.equals("400")){
                    ShowToast.showToast(this, "Error conneting with the server", Toast.LENGTH_SHORT)
                    Log.i(null, "Error with Json Object")
                    return@JsonObjectRequest
                }
                if(status.equals("401")){

                }

                Log.i(null, error.networkResponse.statusCode.toString())
                Log.i(null, error.toString())
                ShowToast.showToast(this,"Error connecting to the server", Toast.LENGTH_SHORT)
            }
        )

        queue.add(request)
    }

    // function to change activity to login and finish this activity
    fun changeActivityLogin(){
        val intent = Intent(this, LoginScreen::class.java)
        startActivity(intent)
        finish()

    }
}