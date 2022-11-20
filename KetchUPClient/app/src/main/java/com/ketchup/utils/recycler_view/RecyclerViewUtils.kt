package com.ketchup.utils.recycler_view


import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.StringRequest
import com.ketchup.app.*
import com.ketchup.app.view.UserList
import com.ketchup.utils.files.ImagePFP.Companion.setFriendsPictures
class RecyclerViewUtils {
    companion object{
        /** Refresh the recycler view when the user has new friends
         * @see RecyclerView
         * @see RecyclerView.getAdapter
         * @see KetchUp.getCurrentActivity
         */
        fun refreshRecyclerView(){
            // Value to get current activity
            val activity = KetchUp.getCurrentActivity()
            // Current activity recycler view
            val recyclerView = activity.findViewById<RecyclerView>(R.id.usersRecyclerView)
            // Sets the friends pictures
            setFriendsPictures()
            // Updates the recycler view
            recyclerView.adapter!!.notifyItemInserted(UserList.userList.size-1)
        }

    }
}