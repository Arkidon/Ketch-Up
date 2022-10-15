package com.ketchup.app.view
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ketchup.app.R
import com.ketchup.app.models.UserData
import com.makeramen.roundedimageview.RoundedImageView

class UserAdapter (val c: Context, val userList: ArrayList<UserData>):RecyclerView.Adapter<UserAdapter.userViewHolder>(){
    inner class userViewHolder(val v:View):RecyclerView.ViewHolder(v){
        val name = v.findViewById<TextView>(R.id.textName)
        val pfp = v.findViewById<RoundedImageView>(R.id.friendPFP)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): userViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.chat_menu,parent,false)
        return userViewHolder(v)
    }

    override fun onBindViewHolder(holder: userViewHolder, position: Int) {
        val newList = userList[position]
        holder.name.text = newList.username
        holder.pfp.background = newList.friendPFP
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}