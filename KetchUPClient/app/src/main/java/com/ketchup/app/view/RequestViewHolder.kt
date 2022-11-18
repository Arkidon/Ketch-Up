package com.ketchup.app.view

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ketchup.app.R
import com.ketchup.app.database.Users
import com.makeramen.roundedimageview.RoundedImageView

class RequestViewHolder(view:View):RecyclerView.ViewHolder(view){
    val friendName = view.findViewById<TextView>(R.id.friendName)
    val friendSub = view.findViewById<TextView>(R.id.friendSub)
    val pfp = view.findViewById<RoundedImageView>(R.id.friendPFP)

    fun render(requestData: Users, onClickListener:(Users) -> Unit){
        friendName.text = requestData.alias
        friendSub.text = requestData.status
        pfp.setImageBitmap(requestData.pictureBitmap)
        itemView.setOnClickListener{ onClickListener(requestData)}
    }
}

