package com.ketchup.app.view

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ketchup.app.R
import com.ketchup.app.database.Users
import com.ketchup.utils.ImageStorage
import com.makeramen.roundedimageview.RoundedImageView

class UserViewHolder(view:View):RecyclerView.ViewHolder(view){
    val friendName = view.findViewById<TextView>(R.id.friendName)
    val friendSub = view.findViewById<TextView>(R.id.friendSub)
    val pfp = view.findViewById<RoundedImageView>(R.id.friendPFP)

    fun render(userData: Users, onClickListener:(Users) -> Unit){
        friendName.text = userData.alias
        friendSub.text = userData.status
        pfp.setImageBitmap(userData.pictureBitmap)
        itemView.setOnClickListener{ onClickListener(userData)}
    }
}

