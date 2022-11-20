package com.ketchup.app

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ketchup.app.database.AppDatabase
import com.ketchup.app.database.Users
import com.ketchup.app.view.RequestAdapter
import com.ketchup.app.view.RequestList.Companion.requestList
import com.ketchup.app.view.UserAdapter
import com.ketchup.app.view.UserList
import com.ketchup.app.view.UserList.Companion.userList
import com.ketchup.utils.*
import com.ketchup.utils.files.CredentialsManager
import com.ketchup.utils.files.ImagePFP
import com.ketchup.utils.recycler_view.RecyclerViewUtils.Companion.refreshRecyclerView
import com.ketchup.utils.volley.VolleyHttpRequest.Companion.countUserRequests
import com.ketchup.utils.volley.VolleyHttpRequest.Companion.getUserRequests
import com.ketchup.utils.volley.VolleyHttpRequest.Companion.requestFriends
import com.ketchup.utils.volley.VolleyHttpRequest.Companion.requestUsers
import com.makeramen.roundedimageview.RoundedImageView


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

        // Changes the theme to remove the background used for the splash screen
        setTheme(R.style.Theme_KetchUp)

        // Sets the content for the activity
        setContentView(R.layout.chat_menu)

        // Gets the username and the profile picture from the intent
        val username = intent.getStringExtra(username)
        val pfp = findViewById<RoundedImageView>(R.id.userPFP)
        val selfId = CredentialsManager.getCredential("user", this).toInt()
        val selfpfp = "https://static.tvtropes.org/pmwiki/pub/images/maddyandtheo.png"
        val status = findViewById<TextView>(R.id.userStatus)
        pfp.setOnClickListener{profileSelected(username, selfpfp, status.text.toString())}
        setUser(username, pfp, selfpfp)

        // values for database managements
        val db = AppDatabase.createInstance(this)
        val userDao = db?.userDao()

        // Sets the event for the new Chat Floating Action Button
        val fabNewChat = findViewById<FloatingActionButton>(R.id.fabNewChat)

        fabNewChat.setOnClickListener {
            // When the add person button is pressed (fabNewChat) the method addUser is called,
            // and the username and the profile picture of the user is passed to future class calling
            addUser(username, selfpfp)
            addUsersOn = true;
        }




        // Request all friends of user
        requestFriends()
        // Request the count of pending friend request
        countUserRequests()
        // Removes the loading spinner
        val loadingSpinner = findViewById<ProgressBar>(R.id.progressBar)
        loadingSpinner.isGone = true

        //Value to get all users in bd
        userList = userDao?.getSingleChats(selfId) as ArrayList<Users>
        ImagePFP.setFriendsPictures()
        initRecyclerView()
    }


    override fun onResume() {
        super.onResume()
        // Clears the list of pending friend request
        requestList.clear()
        // Request all friends of user
        requestFriends()
        // Request the count of pending friend request
        countUserRequests()
    }

    private fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.usersRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = UserAdapter(UserList.userList) { userData -> onItemSelected(userData) }
    }

    private fun profileSelected(username: String?, selfpfp: String, status: String){
        val extras = Bundle()
        val goSettings = Intent(this, SettingScreen::class.java)
        extras.putString("username", username)
        extras.putString("status", status)
        extras.putString("profilePicture", selfpfp)
        goSettings.putExtras(extras)
        startActivity(goSettings)

    }

    @SuppressLint("UnsafeOptInUsageError")
    // Doesn't work yet
    private fun setBadge(fabNewChat: FloatingActionButton, count : Int) {
        val badgeDrawable = BadgeDrawable.create(this)
        badgeDrawable.badgeGravity = BadgeDrawable.TOP_END
        badgeDrawable.verticalOffset = 45
        badgeDrawable.horizontalOffset = -102
        badgeDrawable.backgroundColor = Color.parseColor("#EF5252")
        badgeDrawable.badgeTextColor = Color.WHITE
        badgeDrawable.isVisible = true
        badgeDrawable.number = count
        BadgeUtils.attachBadgeDrawable(badgeDrawable, fabNewChat, findViewById(R.id.frameLayout))

        if(count>=1){
            badgeDrawable.isVisible = false
        }
    }

    private fun onItemSelected(userData: Users){
        val extras = Bundle()
        val goChat = Intent(this, ChatScreen::class.java)
        extras.putInt("friendId", userData.user_id)
        extras.putString("friendUsername", userData.alias)
        extras.putString("friendProfilePicture", userData.pfp)
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
        //function that returns all pending requests
        getUserRequests()
        val recyclerView = findViewById<RecyclerView>(R.id.usersRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RequestAdapter(requestList)
        applyButton.setOnClickListener {
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
}

