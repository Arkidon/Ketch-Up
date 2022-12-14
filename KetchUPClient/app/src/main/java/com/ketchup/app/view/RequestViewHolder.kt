package com.ketchup.app.view

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ketchup.app.R
import com.ketchup.app.database.Users
import com.makeramen.roundedimageview.RoundedImageView
import java.text.FieldPosition

class RequestViewHolder(view:View):RecyclerView.ViewHolder(view){
    val friendName = view.findViewById<TextView>(R.id.friendName)
    val friendSub = view.findViewById<TextView>(R.id.friendSub)
    val pfp = view.findViewById<RoundedImageView>(R.id.friendPFP)
    val buttonAccept = view.findViewById<FloatingActionButton>(R.id.button_accept)
    val buttonReject = view.findViewById<FloatingActionButton>(R.id.button_cancel)
    fun render(requestData: Users){
        friendName.text = requestData.alias
        friendSub.text = requestData.status
        Glide.with(pfp.context).load("https://i.pinimg.com/474x/a4/60/b3/a460b3744133c33fa9dba7351c7d483f.jpg").into(pfp)
    }
}

