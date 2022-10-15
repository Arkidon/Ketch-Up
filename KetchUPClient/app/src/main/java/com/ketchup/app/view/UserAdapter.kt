package com.ketchup.app.view
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ketchup.app.R
import com.ketchup.app.UserList
import com.ketchup.app.models.UserData

class UserAdapter(private val userList: List<UserData>, private val onClickListener:(UserData) -> Unit): RecyclerView.Adapter<UserViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return UserViewHolder(layoutInflater.inflate(R.layout.item_friends, parent, false))
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = userList[position]
        holder.render(item, onClickListener)
    }

    override fun getItemCount(): Int = userList.size
}
