package com.ketchup.app.view
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ketchup.app.R
import com.ketchup.app.database.Users
import com.ketchup.app.view.RequestList.Companion.requestList


class RequestAdapter(private val requestList: ArrayList<Users>, private val onClickListener:(Users) -> Unit): RecyclerView.Adapter<RequestViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return RequestViewHolder(layoutInflater.inflate(R.layout.item_friends_request, parent, false))
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val item = requestList[position]
        holder.render(item, onClickListener)
    }

    override fun getItemCount(): Int = requestList.size
}
