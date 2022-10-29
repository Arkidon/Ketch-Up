package com.ketchup.app

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.NoConnectionError
import com.android.volley.Request
import com.android.volley.TimeoutError
import com.android.volley.toolbox.Volley
import com.ketchup.utils.EmptyResponseJsonRequest
import com.ketchup.utils.ShowToast
import com.ketchup.utils.UrlFile
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
                    if(userText.text.isNotEmpty() && passwordText.text.isNotEmpty() && passwordText.text.contentEquals(passwordRepText.text)) {
                        signUp()
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
                    if(userText.text.isNotEmpty() && passwordText.text.isNotEmpty() && passwordText.text.contentEquals(passwordRepText.text)) {
                        signUp()
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
                    if(userText.text.isNotEmpty() && passwordText.text.isNotEmpty() && passwordText.text.contentEquals(passwordRepText.text)) {
                        signUp()
                    }

                    return true
                }
                return false
            }
        })

        singUpButton.setOnClickListener {
            signUp()
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
            // Checks if passwords match
            ShowToast.showToast(this,"Your must provide a password and repeat it", Toast.LENGTH_SHORT)
            return
        }

        if (!(passwordText.text.contentEquals(passwordRepText.text)) && passwordText.text.isNotEmpty()) {
            ShowToast.showToast(this, "Your passwords must match", Toast.LENGTH_SHORT)
            return
        }

        if(userText.text.isEmpty() && passwordText.text.isEmpty()) {
            ShowToast.showToast(this,"You must provide an user and a password", Toast.LENGTH_SHORT)
            return
        }

        if(!(userText.text.isNotEmpty() && passwordText.text.isNotEmpty() && passwordRepText.text.toString() == passwordText.text.toString())) {
            Log.i(null, "Fields are empty :")
            return
        }

        val queue = Volley.newRequestQueue(this)

        val url = UrlFile.readUrl(this)+"/signup"


        val json = JSONObject()
        json.put("username", userText.text.toString())
        json.put("password", passwordText.text.toString())


        val request = EmptyResponseJsonRequest(
            Request.Method.POST, url, json,
            // Success response handle
            { response ->
                Log.i(null, response.toString())
            },

            // Error response handle
            { error ->
                // Connection timed out
                if(error is TimeoutError){
                    ShowToast.showToast(this, "Server connection timed out", Toast.LENGTH_SHORT)
                    return@EmptyResponseJsonRequest
                }

                // No internet connection validation
                if(error is NoConnectionError){
                    ShowToast.showToast(this, "No internet connection", Toast.LENGTH_SHORT)
                    return@EmptyResponseJsonRequest
                }

                val status : Int = error.networkResponse.statusCode
                if (status == 409){
                    ShowToast.showToast(this, "Users already exits",Toast.LENGTH_SHORT)
                    userText.text = null
                    passwordText.text = null
                    passwordRepText.text = null
                    return@EmptyResponseJsonRequest
                }
                if (status == 404 || status == 405 || status == 400){
                    ShowToast.showToast(this, "Error conneting with the server", Toast.LENGTH_SHORT)
                    Log.i(null, error.networkResponse.statusCode.toString())
                    return@EmptyResponseJsonRequest
                }


                Log.i(null, error.networkResponse.statusCode.toString())
                Log.i(null, error.toString())
                ShowToast.showToast(this,"Error connecting to the server", Toast.LENGTH_SHORT)
            }
        )

        queue.add(request)
    }

    // Function to change activity to login and finish this activity
    /*fun changeActivityLogin(){
        val intent = Intent(this, LoginScreen::class.java)
        startActivity(intent)

    }
     */
}