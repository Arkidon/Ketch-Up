package com.ketchup.app

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ketchup.app.database.AppDatabase
import com.ketchup.app.database.ChatEntries
import com.ketchup.app.database.UserDao
import com.ketchup.app.view.ChatAdapter
import com.ketchup.app.view.ChatList
import com.ketchup.app.view.ChatList.Companion.chatList
import com.ketchup.utils.ChatWebSocket
import com.ketchup.utils.files.ImagePFP
import com.makeramen.roundedimageview.RoundedImageView
import org.json.JSONObject

class ChatScreen : AppCompatActivity() {

    var friendId: Int = -1
    var chatId: Int = -1
    private lateinit var databaseInstance: AppDatabase
    private lateinit var databaseAccess: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Updates the current activity reference
        KetchUp.setCurrentActivity(this)

        // Establish the WebSocket connection if it is not already established
        ChatWebSocket.createConnection(this)

        // Changes the theme to remove the background used for the splash screen
        setTheme(R.style.Theme_KetchUp)

        // Sets the content for the activity
        setContentView(R.layout.chat_screen)

        // Creates the database access object
        databaseInstance = AppDatabase.createInstance(this)!!
        databaseAccess = databaseInstance.userDao()

        // Gets the parameters from the intent
        friendId = intent.getIntExtra("friendId", -1)
        val userName = intent.getStringExtra("friendUsername")
        val friendPFP = intent.getStringExtra("friendProfilePicture")

        // Gets the chat id
        chatId = databaseAccess.getChatId(friendId)

        setUser(userName, friendPFP)

        // Cleans the list of any previous entries
        chatList.clear()

        // Retrieves the chat entries from the database and stores them in a list to
        // be used in the recycler view
        for(chatEntry in databaseAccess.getAllEntriesFromChat(chatId)){
            chatList.add(chatEntry)
        }

        initRecyclerView()
        val sendButton : FloatingActionButton = findViewById(R.id.sendButton)
        val message : EditText = findViewById(R.id.inputMessage)
        message.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                // If the event is a key-down event on the "enter" button
                if (event.action == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    if(message.text.toString().replace(" ", "") != "") {
                        sendMessage()
                    }

                    return true
                }
                return false
            }
        })

        sendButton.setOnClickListener {
            sendMessage()
            initRecyclerView()
        }

        // Removes the loading spinner
        val loadingSpinner = findViewById<ProgressBar>(R.id.progressBar)
        loadingSpinner.isGone = true
    }

    private fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.chatRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this).apply {
            this.stackFromEnd = true
        }

        recyclerView.adapter = ChatAdapter(chatList)
    }

    private fun setUser(userName:String?, friendPFP:String?){
        findViewById<TextView>(R.id.chatName).apply { text = userName }
        val pfp = findViewById<RoundedImageView>(R.id.chatPFP)
        pfp.setImageBitmap(friendPFP?.let { ImagePFP.readImageFromDisk(this, it)})
    }

    private fun sendMessage(){
        // Gets the message
        val message : EditText = findViewById(R.id.inputMessage)

        // Creates the JSON Object
        val jsonMessage = JSONObject()
        jsonMessage.put("chat_id", chatId)
        jsonMessage.put("message", message.text.toString())

        // Send the json to the server
        ChatWebSocket.sendMessage(jsonMessage.toString())

        message.text = null
    }

    /**
     * Appends a new element to the Recycler View List
     */
    fun appendMessage(chatEntry: ChatEntries){
        // Appends the new chat entry to the list
        chatList.add(chatEntry)

        // Notifies the recycler view that a new item has been inserted
        val recyclerView = findViewById<RecyclerView>(R.id.chatRecyclerView)
        recyclerView.adapter!!.notifyItemInserted(chatList.size)

        /* Moves the scroll to the bottom of the recycler view, in order
        * to show the new message */
        recyclerView.smoothScrollToPosition(recyclerView.adapter!!.itemCount - 1)
    }
}