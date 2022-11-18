package com.ketchup.app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.NoConnectionError
import com.android.volley.TimeoutError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ketchup.app.database.AppDatabase
import com.ketchup.app.database.Users
import com.ketchup.app.models.ChatData
import com.ketchup.app.models.RequestData
import com.ketchup.app.view.RequestAdapter
import com.ketchup.app.view.RequestList.Companion.requestList
import com.ketchup.app.view.UserAdapter
import com.ketchup.app.view.UserList.Companion.userList
import com.ketchup.utils.ChatWebSocket
import com.ketchup.utils.ImagePFP
import com.ketchup.utils.ServerAddress
import com.ketchup.utils.ShowToast
import com.makeramen.roundedimageview.RoundedImageView
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


const val friendName = "com.ketchup.app.USERNAME"
const val friendPFP = "com.ketchup.app.PFP"
const val selfName = "com.ketchup.app.selfname"
const val selfStatus = "com.ketchup.app.status"
const val selfPFP = "com.ketchup.app.selfPFP"

open class ChatMenu : AppCompatActivity() {

    @SuppressLint("CutPasteId")
    var addUsersOn = false;

    @SuppressLint("UnsafeOptInUsageError")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Updates the current activity reference
        KetchUp.setCurrentActivity(this)

        // Establish the WebSocket connection if it is not already established
        ChatWebSocket.createConnection(this)

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

        //Value to get all users in bd
        userList = userDao?.getAllUsers() as ArrayList<Users>
        initRecyclerView()

        val status = findViewById<TextView>(R.id.userStatus)
        pfp.setOnClickListener{profileSelected(username, selfpfp, status.text.toString())}
        spinner.isGone = true

        fabNewChat.setOnClickListener {
            // When the add person button is pressed (fabNewChat) the method addUser is called,
            // and the username and the profile picture of the user is passed to future class calling
            addUser(username, selfpfp)
            addUsersOn = true;
        }

        }



    // Object expression for overriding the getHeader method to insert the
    // Authorization header.
    private fun requestFriends(){
        val selfUser = intent.getStringExtra(username)
        val queue = Volley.newRequestQueue(this)
        val db = AppDatabase.createInstance(this)
        val userDao = db?.userDao()
        val url = "http://" + ServerAddress.readUrl(this) + "/request-friend-users?username=" + selfUser
        val request: StringRequest = @SuppressLint("NotifyDataSetChanged")
        object: StringRequest(
            Method.GET, url,
            // Success response handle
            { response ->
                Log.i("onResponse: ","asdfasdrf")
                val jsonObject = JSONObject(response.toString())
                val users = jsonObject.getJSONArray("users")

                for (i in 0 until users.length()){
                    val friendUsername = users.getJSONObject(i).getString("username")
                    val picture = users.getJSONObject(i).getString("picture")
                    val userId  = users.getJSONObject(i).getInt("id")
                    val imageByteArray = ImagePFP.getImageByteArray(picture)
                    //The name of the pfp created with the username and the image extension file
                    val pictureName = ImagePFP.getImageName(friendUsername,picture);
                    ImagePFP.writeImageToDisk(imageByteArray,this, pictureName)
                    val user = Users(friendUsername, userId, pictureName, "placeholder")
                    //Checks if the user is the actually user login
                    if (userId == userDao?.getUsersId(userId)) continue
                    userList.add(user)
                    setFriendsPictures()
                    userDao?.insertUser(user)
                    refreshRecyclerView()
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
    private fun requestUsers(username: String){
        val selfUser = intent.getStringExtra(com.ketchup.app.username)
        val queue = Volley.newRequestQueue(this)
        val db = AppDatabase.createInstance(this)
        val userDao = db?.userDao()
        val url = "http://" + ServerAddress.readUrl(this) + "/search-users?query="+username + "&self-user=" + selfUser
        val request: StringRequest = @SuppressLint("NotifyDataSetChanged")
        object: StringRequest(
            Method.GET, url,
            // Success response handle
            { response ->

                val jsonObject = JSONObject(response.toString())
                val users = jsonObject.getJSONArray("users")

                for (i in 0 until users.length()){
                    val friendUsername = users.getJSONObject(i).getString("username")
                    val picture = users.getJSONObject(i).getString("picture")
                    val userId  = users.getJSONObject(i).getInt("id")
                    val imageByteArray = ImagePFP.getImageByteArray(picture)
                    //The name of the pfp created with the username and the image extension file
                    val pictureName = ImagePFP.getImageName(friendUsername,picture);
                    ImagePFP.writeImageToDisk(imageByteArray,this, pictureName)
                    val user = Users(friendUsername, userId, pictureName, "placeholder")
                    //Checks if the user is the actually user login
                    if (userId == userDao?.getUsersId(userId)) continue
                    userList.add(user)
                    setFriendsPictures()
                    userDao?.insertUser(user)
                    refreshRecyclerView()
                    ShowToast.showToast(this,"User added", Toast.LENGTH_SHORT)

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

                    ShowToast.showToast(this, "User not found", Toast.LENGTH_SHORT)
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
    private fun setFriendsPictures(){
        for (i in 0 until userList.size) {
            userList[i].pictureBitmap =
                userList[i].pfp?.let { ImagePFP.readImageFromDisk(this, it) }
        }
    }
    private fun refreshRecyclerView(){
        val recyclerView = findViewById<RecyclerView>(R.id.usersRecyclerView)
        setFriendsPictures()
        recyclerView.adapter!!.notifyItemInserted(userList.size-1)
    }
    private fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.usersRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = UserAdapter(userList) { userData -> onItemSelected(userData) }
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
        extras.putString(friendPFP, userData.pfp)
        goChat.putExtras(extras)
        startActivity(goChat)

    }

    private fun setUser(username:String?, pfp:RoundedImageView, selfpfp:String){
        findViewById<TextView>(R.id.textName).apply { text = username }
        val status = findViewById<TextView>(R.id.userStatus)
        Glide.with(pfp.context).load(selfpfp).into(pfp)
        status.text = "Strawberry Pie"
    }

    //Method called by pressing the add user button (fabNewChat)
    private fun addUser(username:String?, selfpfp:String ){
        setContentView(R.layout.add_users)
        val spinner = findViewById<ProgressBar>(R.id.progressBar)
        val addUserText = findViewById<EditText>(R.id.searchUserField).text
        spinner.isGone=true
        val pfp = findViewById<RoundedImageView>(R.id.userPFP)
        setUser(username, pfp, selfpfp)
        val applyButton = findViewById<Button>(R.id.addButton)
        listOf(RequestData("Alice", "UwU", "https://static.tvtropes.org/pmwiki/pub/images/maddyandtheo.png"))
            val recyclerView = findViewById<RecyclerView>(R.id.usersRecyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = RequestAdapter(requestList) { requestData -> onItemSelected(requestData) }

            applyButton.setOnClickListener(){
            requestUsers(addUserText.toString())
            finish();
            startActivity(intent);
            overridePendingTransition(R.anim.no_animation, R.anim.no_animation)
            addUsersOn = false;
        }
    }


    override fun onBackPressed() {
        if(addUsersOn){
            finish()
            startActivity(intent);
            overridePendingTransition(R.anim.no_animation, R.anim.no_animation)
            addUsersOn = false
        }
        else{
            finish()
            }
    }

    override fun onResume() {
        super.onResume()
        //Request all friends of user
        requestFriends()
        if (KetchUp.getCurrentActivity() == this) refreshRecyclerView()
    }
}

