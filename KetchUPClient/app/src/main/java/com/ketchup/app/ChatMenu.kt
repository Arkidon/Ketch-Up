package com.ketchup.app

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.NoConnectionError
import com.android.volley.TimeoutError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ketchup.app.database.AppDatabase
import com.ketchup.app.database.Users
import com.ketchup.app.view.UserAdapter
import com.ketchup.utils.ChatWebSocket
import com.ketchup.utils.ImageStorage
import com.ketchup.utils.ServerAddress
import com.ketchup.utils.ShowToast
import com.makeramen.roundedimageview.RoundedImageView
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.net.URLConnection


const val friendName = "com.ketchup.app.USERNAME"
const val friendPFP = "com.ketchup.app.PFP"
const val selfName = "com.ketchup.app.selfname"
const val selfStatus = "com.ketchup.app.status"
const val selfPFP = "com.ketchup.app.selfPFP"

class ChatMenu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_KetchUp)
        setContentView(R.layout.chat_menu)
        val username = intent.getStringExtra(username)
        val spinner = findViewById<ProgressBar>(R.id.progressBar)
        val pfp = findViewById<RoundedImageView>(R.id.userPFP)
        val fabNewChat = findViewById<FloatingActionButton>(R.id.fabNewChat)
        val selfpfp = "https://static.tvtropes.org/pmwiki/pub/images/maddyandtheo.png"
        setUser(username, pfp, selfpfp)
        val db = AppDatabase.createInstance(this)
        val userDao = db?.userDao()



        val usersList : ArrayList<Users> = userDao?.getAllUsers() as ArrayList<Users>
        initRecyclerView(usersList)

        val status = findViewById<TextView>(R.id.userStatus)
        pfp.setOnClickListener{profileSelected(username, selfpfp, status.text.toString())}
        spinner.isGone = true

        fabNewChat.setOnClickListener {
            requestUsers()
        }

    }

    // Object expression for overriding the getHeader method to insert the
    // Authorization header.
   private fun requestUsers(){
        var usersList : ArrayList<Users>? = null
        val queue = Volley.newRequestQueue(this)
        val db = AppDatabase.createInstance(this)
        val userDao = db?.userDao()
        val url = "http://" + ServerAddress.readUrl(this) + "/request-users"
        val request: StringRequest = object: StringRequest(
            Method.GET, url,
            // Success response handle
            { response ->
                val jsonObject = JSONObject(response.toString())
                val users = jsonObject.getJSONArray("users")


                for (i in 0 until users.length()){
                    val friendUsername = users.getJSONObject(i).getString("username")
                    val picture = users.getJSONObject(i).getString("picture")
                    val userId  = users.getJSONObject(i).getInt("id")

                    //variables to converter Base64 image to byteArray
                    val imageByteArray = Base64.decode(picture, Base64.DEFAULT)
                    val ism: InputStream = ByteArrayInputStream(imageByteArray)
                    //Method to get extension file from byteArray
                    val imageExtension = URLConnection.guessContentTypeFromStream(ism).split("/")[1]
                    //The name of the pfp created with the username and the image extension file
                    val pictureName = "$friendUsername.$imageExtension"
                    ImageStorage.writeImageToDisk(imageByteArray,this, pictureName )

                    val user = Users(friendUsername, userId, pictureName, "placeholder")
                    //Checks if the user is the actually user login
                    if(friendUsername.equals(username)) continue
                    //Checks if the user is already in the database
                    if (userId == userDao?.getUsersId(userId)) continue
                    userDao?.insertUser(user)
         /*
                    usersList?.add(user)
                }
                if(usersList == null) {
                    ShowToast.showToast(this, "New users not found", Toast.LENGTH_SHORT)
                }else{
                    initRecyclerView(usersList)
        */
                }
            },

            // Error response handle
            StringRequest@{ error ->
                // Connection timed out validation
                if(error is TimeoutError){
                    Log.i(null, "Timeout Error")
                    return@StringRequest
                }

                // No internet connection validation
                if(error is NoConnectionError){
                    Log.i(null, "No connection Error")
                    return@StringRequest
                }

                val status = error.networkResponse.statusCode
                if ( status == 404 || status == 405 || status == 400){
                    Log.i(null, error.networkResponse.statusCode.toString())
                    return@StringRequest
                }
                if (status == 401){
                    Log.i(null, error.networkResponse.statusCode.toString())
                    return@StringRequest
                }

                Log.i(null, error.networkResponse.statusCode.toString())
                Log.i(null, error.toString())
            }
        ){
            /**
             * Adds custom http request headers
             */
            @Override
            override fun getHeaders(): MutableMap<String, String> {
                val params: HashMap<String, String> = HashMap()

                // Test value
                params["Authorization"] = "Test"

                return params
            }
        }

        queue.add(request)

        // Initialize the WebSocket channel
        ChatWebSocket.createConnection(this)
        ChatWebSocket.sendMessage("Test")

    }

    private fun refreshRecyclerView(){
        //TODO
    }
    private fun initRecyclerView(usersList: ArrayList<Users>) {
        val recyclerView = findViewById<RecyclerView>(R.id.usersRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = UserAdapter(usersList) { userData -> onItemSelected(userData) }
    }

    private fun profileSelected(username: String?, selfpfp: String, status: String){
        val extras = Bundle()
        val goSettings = Intent(this, SettingScreen::class.java)
        extras.putString(selfName, username)
        extras.putString(selfStatus, status)
        extras.putString(selfPFP, selfpfp)
        goSettings.putExtras(extras)
        startActivity(goSettings)

    }

    private fun onItemSelected(userData: Users){
        val extras = Bundle()
        val goChat = Intent(this, ChatScreen::class.java)
        extras.putString(friendName, userData.alias)
        extras.putParcelable("Image", null)
        goChat.putExtras(extras)
        startActivity(goChat)

    }

    private fun setUser(username:String?, pfp:RoundedImageView, selfpfp:String){
        findViewById<TextView>(R.id.textName).apply { text = username }
        val status = findViewById<TextView>(R.id.userStatus)
        Glide.with(pfp.context).load(selfpfp).into(pfp)
        status.text = "Strawberry Pie"}
}