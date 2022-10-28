package com.ketchup.app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.TimeoutError
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ketchup.utils.ShowToast
import com.ketchup.utils.TokenFile
import org.json.JSONObject
import kotlin.math.log

const val username = "com.ketchup.app.selfUSERNAME"

class LoginScreen : AppCompatActivity() {

    private lateinit var loginButton: Button
    private lateinit var registerButton : Button
    private lateinit var userText: EditText
    private lateinit var passwordText: EditText
    private lateinit var devButton: FloatingActionButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_KetchUp)
        setContentView(R.layout.login_screen)
        //login and register buttons
        loginButton = findViewById(R.id.loginButton)
        registerButton = findViewById(R.id.signupButton)
        //Email and password texts
        userText = findViewById(R.id.usernameText)
        passwordText = findViewById(R.id.passwordField)
        devButton = findViewById(R.id.devButton)


        //keyboard compatibility
        userText.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                // If the event is a key-down event on the "enter" button
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    if(userText.text.isNotEmpty() && passwordText.text.isNotEmpty()) {
                        login()
                    }
                    else if (userText.text.isEmpty() && passwordText.text.isNotEmpty()) {
                        passwordText.text = null
                        if (v != null) {
                            ShowToast.showToast(v.context,"You must provide an user", Toast.LENGTH_SHORT)
                        }
                    }
                    else if (userText.text.isNotEmpty() && passwordText.text.isEmpty()) {
                        if (v != null) {
                            ShowToast.showToast(v.context,"You must provide a password", Toast.LENGTH_SHORT)
                        }
                    }
                    else {
                        if (v != null) {
                            ShowToast.showToast(v.context,"You must provide an user and a password", Toast.LENGTH_SHORT)
                        }
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
                        login()
                    }

                    return true
                }
                return false
            }
        })


        //takes to the chats selects
        loginButton.setOnClickListener {
            // Checks if the fields are filled──
            if(userText.text.isNotEmpty() && passwordText.text.isNotEmpty()) {
                login()
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
            var intent: Intent = Intent(this,  RegisterScreen::class.java)
            startActivity(intent)
        }

        devButton.setOnClickListener{
            var intent: Intent = Intent(this,  DevScreen::class.java)
            startActivity(intent)
        }

    }


    private fun login(){
        val queue = Volley.newRequestQueue(this)
        val url = "http://10.0.2.2:8000/login"

        val json: JSONObject = JSONObject()
        json.put("username", userText.text.toString())
        json.put("password", passwordText.text.toString())



        val request = JsonObjectRequest(Request.Method.POST, url, json,
            // Success response handle
            { response ->
                Log.i(null, response.toString())
                if(userText.text.isNotEmpty() && passwordText.text.isNotEmpty()) {
                    var intent: Intent =
                        Intent(applicationContext, ChatMenu::class.java).apply { putExtra(username, userText.text.toString())}
                    startActivity(intent)
                }
            },

            // Error response handle
            { error ->
                // Connection timed out
                if(error is TimeoutError){
                    ShowToast.showToast(this, "Server connection timed out", Toast.LENGTH_SHORT)
                    return@JsonObjectRequest
                }

                Log.i(null, error.networkResponse.statusCode.toString())
                Log.i(null, error.toString())
                ShowToast.showToast(this,"Error connecting to the server", Toast.LENGTH_SHORT)
            }
        )

        queue.add(request)

    }
}