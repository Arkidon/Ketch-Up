package com.ketchup.app.view

import android.view.View
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ketchup.app.KetchUp
import com.ketchup.app.R
import com.ketchup.app.database.Users
import com.ketchup.utils.files.ImagePFP
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
        // placeholder
        //pfp.setImageBitmap(requestData.pfp?.let { ImagePFP.readImageFromDisk(KetchUp.getCurrentActivity(), it) })
        pfp.setImageBitmap(
            AppCompatResources.getDrawable(KetchUp.getCurrentActivity(), R.mipmap.app_logo_round)
                ?.toBitmap()
        )
    }
}

