package com.ketchup.app

import android.content.Intent
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
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.ketchup.utils.ShowToast
import com.ketchup.utils.files.ServerAddress

class DevScreen : AppCompatActivity() {
    private lateinit var ipField: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_KetchUp)
        setContentView(R.layout.debug_settings_screen)

        // Button and fields
        val devButton: Button = findViewById(R.id.devButton)
        ipField = findViewById(R.id.urlField)

        ipField.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                // If the event is a key-down event on the "enter" button
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    updateIP()
                    return true
                }
                return false
            }
        })

        devButton.setOnClickListener {
           updateIP()
        }
    }

    /**
     * Stores the IP and tries to ping the server
     */
    private fun updateIP(){
        // Checks if the EditField is empty
        if (ipField.text.toString().isEmpty()) {
            ShowToast.showToast(this, "You must provide an IP address", Toast.LENGTH_SHORT)
            return
        }

        // Sets the url to make the request
        val url = """http://${ipField.text}"""

        val queue = Volley.newRequestQueue(this)

        // HTTP request to ping the server
        val request = StringRequest(
            Request.Method.GET, url,
            // Success response handle
            { response ->

                // Checks if the response is the expected value
                if(response.toString() != "KetchUp"){
                    ShowToast.showToast(this, "Wrong server address", Toast.LENGTH_SHORT)
                    return@StringRequest
                }

                // Displays a Toast
                ShowToast.showToast(this, "Connection established", Toast.LENGTH_LONG)

                // Stores the URL
                ServerAddress.writeUrl(ipField.text.toString(), this)

                // Changes to the login activity
                startActivity(Intent(this, LoginScreen::class.java))
            },

            // Error response handle
            StringRequest@{ error ->
                // Connection timed out validation
                if (error is TimeoutError) {
                    Log.i(null, "Timeout Error")
                    ShowToast.showToast(this, "Connection timed out", Toast.LENGTH_SHORT)
                    return@StringRequest
                }

                // No internet connection validation
                if (error is NoConnectionError) {
                    Log.i(null, "No connection Error")
                    ShowToast.showToast(this, "Can't connect to the server", Toast.LENGTH_SHORT)
                    return@StringRequest
                }

                val status = error.networkResponse.statusCode
                if (status == 405) {
                    Log.i(null, error.networkResponse.statusCode.toString())
                    ShowToast.showToast(this, "Incorrect request method", Toast.LENGTH_SHORT)
                    return@StringRequest
                }
            }
        )

        queue.add(request)
    }

    /**
     * Retrieves the store IP value and
     * sets it as the ipField hint
     */
    private fun setIPHint(){
        val serverAddress = ServerAddress.readUrl(this)

        // Sets the current URL value as the EditField hint
        if (serverAddress != "127.0.0.1") {
            ipField.hint = ServerAddress.readUrl(this)
        }
    }

    override fun onResume() {
        super.onResume()
        setIPHint()
    }

}
