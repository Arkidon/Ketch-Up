package com.ketchup.app.view

import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ketchup.app.ChatMenu
import com.ketchup.app.ChatScreen
import com.ketchup.app.R
import com.ketchup.app.models.UserData
import com.makeramen.roundedimageview.RoundedImageView

class UserViewHolder(view:View):RecyclerView.ViewHolder(view){
    val friendName = view.findViewById<TextView>(R.id.friendName)
    val friendSub = view.findViewById<TextView>(R.id.friendSub)
    val pfp = view.findViewById<RoundedImageView>(R.id.friendPFP)

    fun render(userData: UserData, onClickListener:(UserData) -> Unit){
        friendName.text = userData.friendName
        friendSub.text = userData.friendSub
        Glide.with(pfp.context).load(userData.pfp).into(pfp)
        itemView.setOnClickListener{ onClickListener(userData)}
    }
}

