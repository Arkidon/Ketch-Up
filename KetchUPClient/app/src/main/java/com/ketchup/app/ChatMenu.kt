package com.ketchup.app
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ketchup.app.R.drawable.william_pfp
import com.ketchup.app.models.UserData
import com.ketchup.app.view.UserAdapter
import com.makeramen.roundedimageview.RoundedImageView


class ChatMenu : AppCompatActivity() {
    private lateinit var addsButton: FloatingActionButton
    private lateinit var userAdapter: UserAdapter
    private lateinit var userList:ArrayList<UserData>
    private lateinit var recv:RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_KetchUp)
        setContentView(R.layout.chat_menu)
        userList = ArrayList()
        addsButton = findViewById(R.id.fabNewChat)
        recv = findViewById(R.id.usersRecyclerView)
        userAdapter = UserAdapter(this,userList)
        recv.layoutManager = LinearLayoutManager(this)
        recv.adapter = userAdapter
        addsButton.setOnClickListener{addUser()}
    }

    private fun addUser(){
        val inflter = LayoutInflater.from(this)
        val v = inflter.inflate(R.layout.chat_menu,null)
        val userName = v.findViewById<EditText>(R.id.textName)
        val userPFP = v.findViewById<RoundedImageView>(R.id.pfp)
        userList.add(UserData("Name : William", getDrawable(william_pfp)))
    }
}