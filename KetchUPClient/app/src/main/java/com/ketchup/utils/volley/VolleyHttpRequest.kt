package com.ketchup.utils.volley

import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.NoConnectionError
import com.android.volley.Request
import com.android.volley.TimeoutError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.ketchup.app.KetchUp
import com.ketchup.app.R
import com.ketchup.app.database.AppDatabase
import com.ketchup.app.database.ChatMembership
import com.ketchup.app.database.Chats
import com.ketchup.app.database.Users
import com.ketchup.app.view.RequestList.Companion.requestList
import com.ketchup.app.view.UserList
import com.ketchup.utils.*
import com.ketchup.utils.files.CredentialsManager
import com.ketchup.utils.files.ImagePFP
import com.ketchup.utils.files.ImagePFP.Companion.setFriendsPictures
import com.ketchup.utils.files.ServerAddress
import com.ketchup.utils.recycler_view.RecyclerViewUtils.Companion.refreshRecyclerView
import org.json.JSONObject

import java.util.Date

class VolleyHttpRequest {
    companion object{
        /** Http method to update the status of a friend request:
         * Accepts or Rejects it
         * @param friendId The id of a friend where is going to be updated (int)
         * @param friendStatus The status : 1 is accepted 4 is rejected (int)
         * @param position The status : 1 is accepted 4 is rejected (int)
         * @see EmptyResponseJsonRequest
         * @see KetchUp.getCurrentActivity
         */
        fun updateFriendRequests(friendId: Int, friendStatus: Int, position : Int){
            // Value to get current activity
            val activity = KetchUp.getCurrentActivity()
            // Value for Volley
            val queue = Volley.newRequestQueue(activity)
            // View url
            val url = "http://" + ServerAddress.readUrl(activity) + "/update-friendship-status"
            // ChatMenu Recycler view
            val recyclerView = activity.findViewById<RecyclerView>((R.id.usersRecyclerView))
            // Json object to request
            val json = JSONObject()
            json.put("user", friendId)
            json.put("status", friendStatus)
            //Custom Class of Volley
            val request = EmptyResponseJsonRequest(
                Request.Method.POST, url, json,
                // Success response handle
                { response ->
                    //not working because its not entering here
                    if (friendStatus == 1){
                        // if user accepts it removes from recycler view
                        ShowToast.showToast(activity, "User Added", Toast.LENGTH_SHORT)
                        requestList.removeAt(position)
                        recyclerView.adapter?.notifyItemRemoved(position)
                    }
                    else if(friendStatus == 4){
                        // if user rejects it removes from recycler view
                        ShowToast.showToast(activity, "User Rejected", Toast.LENGTH_SHORT)
                        requestList.removeAt(position)
                        recyclerView.adapter?.notifyItemRemoved(position)
                    }
                },
                // Error response handle
                { error ->
                    // Connection timed out
                    if(error is TimeoutError){
                        ShowToast.showToast(activity, "Server connection timed out", Toast.LENGTH_SHORT)
                        return@EmptyResponseJsonRequest
                    }

                    // No internet connection validation
                    if(error is NoConnectionError){
                        ShowToast.showToast(activity, "Can't connect to the server", Toast.LENGTH_SHORT)
                        return@EmptyResponseJsonRequest
                    }

                    val status : Int = error.networkResponse.statusCode

                    if (status == 404 || status == 405 || status == 400){
                        ShowToast.showToast(activity, "Error connecting with the server", Toast.LENGTH_SHORT)
                        Log.i(null, error.networkResponse.statusCode.toString())
                        return@EmptyResponseJsonRequest
                    }

                    //Console log for debugging
                    Log.i(null, error.networkResponse.statusCode.toString())
                    Log.i(null, error.toString())
                    ShowToast.showToast(activity,"Error connecting to the server", Toast.LENGTH_SHORT)
                }
            )

            //Adds customs headers to request
            request.addHeader("user", CredentialsManager.getCredential("user", KetchUp.getCurrentActivity()))
            request.addHeader("session", CredentialsManager.getCredential("session-token", KetchUp.getCurrentActivity()))
            queue.add(request)

            // Initialize the WebSocket channel
            ChatWebSocket.createConnection(activity)
            ChatWebSocket.sendMessage("Test")
        }
        /** Http method to request all friends, puts them in the database
         * and inserts them in recycler view
         * @see StringRequest
         * @see KetchUp.getCurrentActivity
         */
        fun requestFriends(){
            // Value to get current activity
            val activity = KetchUp.getCurrentActivity()
            // Creates objects to manages the database
            val db = AppDatabase.createInstance(activity)
            val userDao = db?.userDao()
            // Http request method
            val queue = Volley.newRequestQueue(activity)
            // view url
            val url = "http://" + ServerAddress.readUrl(activity) + "/request-friend-users"
            val request = object: StringRequest(
                Method.GET, url,
                // Success response handle
                { response ->
                    // Server JSON response
                    val jsonObject = JSONObject(response.toString())
                    //Gets users from the json
                    val users = jsonObject.getJSONArray("users")

                    for (i in 0 until users.length()){
                        // Gets all objects of the JSON
                        val friendUsername = users.getJSONObject(i).getString("username")
                        val picture = users.getJSONObject(i).getString("picture")
                        val userId  = users.getJSONObject(i).getInt("id")
                        val chatId = users.getJSONObject(i).getInt("chat_id")
                        val membershipId = users.getJSONObject(i).getInt("membership_id")
                        val role  = users.getJSONObject(i).getString("role")
                        val group = false
                        //val chatCreationDate = users.getJSONObject(i).getString("chat_date")
                        val imageByteArray = ImagePFP.getImageByteArray(picture)
                        //The name of the pfp created with the username and the image extension file
                        val pictureName = ImagePFP.getImageName(friendUsername,picture);
                        ImagePFP.writeImageToDisk(imageByteArray,activity, pictureName)
                        val user = Users(friendUsername, userId, pictureName, "placeholder")
                        val membership = ChatMembership(membershipId, chatId, role, userId)
                        val chat = Chats(chatId, Date() , false)
                        //Checks if the user is the actually user login
                        if (userId == userDao?.getUsersId(userId)) continue
                        // Sets all friends pictures
                        setFriendsPictures()
                        //Inserts users into database
                        userDao?.insertUser(user)
                        userDao?.insertChat(chat)
                        userDao?.insertMemberships(membership)
                        // Adds the user in user list to update the recycler view
                       if (userId == userDao?.getUserIdWithSingleChat(userId)) continue
                        UserList.userList.add(user)
                        refreshRecyclerView()
                    }

                },

                // Error response handle
                StringRequest@{ error ->
                    // Connection timed out validation
                    if(error is TimeoutError){
                        Log.i(null, "Timeout Error")
                        return@StringRequest
                    }

                    // No internet connection validation
                    if(error is NoConnectionError){
                        Log.i(null, "No connection Error")
                        return@StringRequest
                    }

                    val status = error.networkResponse.statusCode
                    if ( status == 404 || status == 405 || status == 400){
                        Log.i(null, error.networkResponse.statusCode.toString())


                        return@StringRequest
                    }
                    if (status == 401){
                        Log.i(null, error.networkResponse.statusCode.toString())
                        return@StringRequest
                    }

                    Log.i(null, error.networkResponse.statusCode.toString())
                    Log.i(null, error.toString())
                }
            ){
                /**
                 * Adds custom http request headers
                 */
                @Override
                override fun getHeaders(): MutableMap<String, String> {
                    val params: HashMap<String, String> = HashMap()

                    // Sets the custom headers
                    params["user"] = CredentialsManager.getCredential("user", KetchUp.getCurrentActivity())
                    params["session"] = CredentialsManager.getCredential("session-token", KetchUp.getCurrentActivity())

                    return params
                }
            }

            queue.add(request)

            // Initialize the WebSocket channel
            ChatWebSocket.createConnection(activity)
            ChatWebSocket.sendMessage("Test")

        }
        /** Http method to request all chats from self user and puts them into database
        * @see StringRequest
        * @see KetchUp.getCurrentActivity
        */
        fun requestSelfUserChats(){
            val selfId = CredentialsManager.getCredential("user", KetchUp.getCurrentActivity()).toInt()
            // Value to get current activity
            val activity = KetchUp.getCurrentActivity()
            // Creates objects to manages the database
            val db = AppDatabase.createInstance(activity)
            val userDao = db?.userDao()
            // Http request method
            val queue = Volley.newRequestQueue(activity)
            // view url
            val url = "http://" + ServerAddress.readUrl(activity) + "/request-self-user-chats"
            val request = object: StringRequest(
                Method.GET, url,
                // Success response handle
                { response ->
                    // Server JSON response
                    val jsonObject = JSONObject(response.toString())
                    //Gets users from the json
                    val users = jsonObject.getJSONArray("self_user_chats")

                    for (i in 0 until users.length()){
                        // Gets all objects of the JSON
                        val chatId = users.getJSONObject(i).getInt("chat_id")
                        val membershipId = users.getJSONObject(i).getInt("membership_id")
                        val role  = users.getJSONObject(i).getString("role")
                        //val chatCreationDate = users.getJSONObject(i).getString("chat_date")
                        val group = false
                        val membership = ChatMembership(membershipId, chatId, role, selfId)
                        val chat = Chats(chatId, Date() , group)
                        //Inserts users into database
                        if (chatId != userDao?.getChatsId(chatId)) {
                            userDao?.insertChat(chat)
                        }
                        if (membershipId != userDao?.getMembershipsId(membershipId))
                            userDao?.insertMemberships(membership)
                    }

                },

                // Error response handle
                StringRequest@{ error ->
                    // Connection timed out validation
                    if(error is TimeoutError){
                        Log.i(null, "Timeout Error")
                        return@StringRequest
                    }

                    // No internet connection validation
                    if(error is NoConnectionError){
                        Log.i(null, "No connection Error")
                        return@StringRequest
                    }

                    val status = error.networkResponse.statusCode
                    if ( status == 404 || status == 405 || status == 400){
                        Log.i(null, error.networkResponse.statusCode.toString())


                        return@StringRequest
                    }
                    if (status == 401){
                        Log.i(null, error.networkResponse.statusCode.toString())
                        return@StringRequest
                    }

                    Log.i(null, error.networkResponse.statusCode.toString())
                    Log.i(null, error.toString())
                }
            ){
                /**
                 * Adds custom http request headers
                 */
                @Override
                override fun getHeaders(): MutableMap<String, String> {
                    val params: HashMap<String, String> = HashMap()

                    // Sets the custom headers
                    params["user"] = CredentialsManager.getCredential("user", KetchUp.getCurrentActivity())
                    params["session"] = CredentialsManager.getCredential("session-token", KetchUp.getCurrentActivity())

                    return params
                }
            }

            queue.add(request)

            // Initialize the WebSocket channel
            ChatWebSocket.createConnection(activity)
            ChatWebSocket.sendMessage("Test")

        }
        /** Http method to request all friends Requests
         * and inserts them in Add friends recycler view
         * @see StringRequest
         * @see KetchUp.getCurrentActivity
         */
        fun getUserRequests(){
            // Gets the current activity
            val activity = KetchUp.getCurrentActivity()
            // Volley value
            val queue = Volley.newRequestQueue(activity)
            // View url
            val url = "http://" + ServerAddress.readUrl(activity) + "/friendship-requests-list"
            val request = object: StringRequest(
                Method.GET, url,
                // Success response handle
                { response ->
                    // Server JSON response
                    val jsonObject = JSONObject(response.toString())
                    // Gets all users from JSON
                    val users = jsonObject.getJSONArray("users")

                    for (i in 0 until users.length()){
                        //adds all users to requestList and put it in recycler view
                        val friendUsername = users.getJSONObject(i).getString("username")
                        val picture = users.getJSONObject(i).getString("picture")
                        val userId  = users.getJSONObject(i).getInt("id")
                        val imageByteArray = ImagePFP.getImageByteArray(picture)
                        //The name of the pfp created with the username and the image extension file
                        val pictureName = ImagePFP.getImageName(friendUsername,picture);
                        ImagePFP.writeImageToDisk(imageByteArray,activity, pictureName)
                        val user = Users(friendUsername, userId, pictureName, "placeholder")
                        /* Checks if user already exists in requestList for recycler view
                           if not exits put it in request List
                         */
                        var exists = false
                        for (request in requestList){
                            if (request.user_id == user.user_id) exists=true
                        }
                        if (!exists) requestList.add(user)
                        // Sets all friends pictures
                        setFriendsPictures()
                        // Updates recycler view
                        refreshRecyclerView()
                    }

                },

                // Error response handle
                StringRequest@{ error ->
                    // Connection timed out validation
                    if(error is TimeoutError){
                        Log.i(null, "Timeout Error")
                        return@StringRequest
                    }

                    // No internet connection validation
                    if(error is NoConnectionError){
                        Log.i(null, "No connection Error")
                        return@StringRequest
                    }

                    val status = error.networkResponse.statusCode
                    if ( status == 404 || status == 405 || status == 400){
                        Log.i(null, error.networkResponse.statusCode.toString())


                        return@StringRequest
                    }
                    if (status == 401){
                        Log.i(null, error.networkResponse.statusCode.toString())
                        return@StringRequest
                    }

                    Log.i(null, error.networkResponse.statusCode.toString())
                    Log.i(null, error.toString())
                }
            ){
                /**
                 * Adds custom http request headers
                 */
                @Override
                override fun getHeaders(): MutableMap<String, String> {
                    val params: HashMap<String, String> = HashMap()

                    // Sets the custom headers
                    params["user"] = CredentialsManager.getCredential("user", KetchUp.getCurrentActivity())
                    params["session"] = CredentialsManager.getCredential("session-token", KetchUp.getCurrentActivity())

                    return params
                }
            }

            queue.add(request)

            // Initialize the WebSocket channel
            ChatWebSocket.createConnection(activity)
            ChatWebSocket.sendMessage("Test")

        }
        /** Http method to sent friend request
         * @param username the username of the user to whom a request is to be sent
         * @see StringRequest
         * @see KetchUp.getCurrentActivity
         */
        fun requestUsers(username: String){
            // Gets the current activity
            val activity = KetchUp.getCurrentActivity()
            // Volley value
            val queue = Volley.newRequestQueue(activity)
            // View url
            val url = "http://" + ServerAddress.readUrl(activity) + "/search-users?query=" + username
            val request: StringRequest = object: StringRequest(
                Method.GET, url,

                // Success response handle
                {
                    ShowToast.showToast(activity,"Friend request sent", Toast.LENGTH_SHORT)
                },

                // Error response handle
                StringRequest@{ error ->
                    // Connection timed out validation
                    if(error is TimeoutError){
                        Log.i(null, "Timeout Error")
                        return@StringRequest
                    }

                    // No internet connection validation
                    if(error is NoConnectionError){
                        Log.i(null, "No connection Error")
                        return@StringRequest
                    }

                    val status = error.networkResponse.statusCode
                    if ( status == 404 || status == 405 || status == 400){
                        Log.i(null, error.networkResponse.statusCode.toString())

                        ShowToast.showToast(activity, "User not found", Toast.LENGTH_SHORT)
                        return@StringRequest
                    }
                    if (status == 401){
                        Log.i(null, error.networkResponse.statusCode.toString())
                        return@StringRequest
                    }
                    // Console log for debugging
                    Log.i(null, error.networkResponse.statusCode.toString())
                    Log.i(null, error.toString())
                }
            ){
                /**
                 * Adds custom http request headers
                 */
                @Override
                override fun getHeaders(): MutableMap<String, String> {
                    val params: HashMap<String, String> = HashMap()

                    // Sets the custom headers
                    params["user"] = CredentialsManager.getCredential("user", KetchUp.getCurrentActivity())
                    params["session"] = CredentialsManager.getCredential("session-token", KetchUp.getCurrentActivity())

                    return params
                }
            }

            queue.add(request)

            // Initialize the WebSocket channel
            ChatWebSocket.createConnection(activity)
            ChatWebSocket.sendMessage("Test")

        }
        /** Http method to get the count of pending requests (this method doesn't work yet)
         * @see StringRequest
         * @see KetchUp.getCurrentActivity
         */
        fun countUserRequests(){

            // Value to get current activity
            val activity = KetchUp.getCurrentActivity()
            //val fabNewChat = activity.findViewById<FloatingActionButton>(R.id.fabNewChat)
            // Volley value
            val queue = Volley.newRequestQueue(activity)
            // View url
            val url = "http://" + ServerAddress.readUrl(activity) + "/friendship-requests-count"

            val request = object: StringRequest(Method.GET, url,
                // Success response handle
                { response ->
                    var count = 0
                    val jsonObject = JSONObject(response.toString())
                    count = jsonObject.getInt("count")
                    Log.i("Count: ", count.toString())
                    //setBadge(fabNewChat,count)
                },

                // Error response handle
                StringRequest@{ error ->

                    // Connection timed out validation
                    if(error is TimeoutError){
                        Log.i(null, "Timeout Error")
                        return@StringRequest
                    }

                    // No internet connection validation
                    if(error is NoConnectionError){
                        Log.i(null, "No connection Error")
                        return@StringRequest
                    }

                    val status = error.networkResponse.statusCode
                    if ( status == 404 || status == 405 || status == 400){
                        Log.i(null, error.networkResponse.statusCode.toString())


                        return@StringRequest
                    }
                    if (status == 401){
                        Log.i(null, error.networkResponse.statusCode.toString())
                        return@StringRequest
                    }
                    // Console log for debugging
                    Log.i(null, error.networkResponse.statusCode.toString())
                    Log.i(null, error.toString())
                }
            ){
                /**
                 * Adds custom http request headers
                 */
                @Override
                override fun getHeaders(): MutableMap<String, String> {
                    val params: HashMap<String, String> = HashMap()

                    // Sets the custom headers
                    params["user"] = CredentialsManager.getCredential("user", KetchUp.getCurrentActivity())
                    params["session"] = CredentialsManager.getCredential("session-token", KetchUp.getCurrentActivity())

                    return params
                }
            }

            queue.add(request)

            // Initialize the WebSocket channel
            ChatWebSocket.createConnection(activity)
            ChatWebSocket.sendMessage("Test")
        }
        /** Http method to get self info and insert it into database
         * @see StringRequest
         * @see KetchUp.getCurrentActivity
         * @see VolleyHttpRequest.requestSelfUserChats
         */
        fun requestSelfInfo(){
            val selfId = CredentialsManager.getCredential("user", KetchUp.getCurrentActivity()).toInt()
            // Value to get current activity
            val activity = KetchUp.getCurrentActivity()
            // Creates objects to manages the database
            val db = AppDatabase.createInstance(activity)
            val userDao = db?.userDao()
            // Http request method
            val queue = Volley.newRequestQueue(activity)
            // view url
            val url = "http://" + ServerAddress.readUrl(activity) + "/request-self-info"
            val request = object: StringRequest(
                Method.GET, url,
                // Success response handle
                { response ->
                    // Server JSON response
                    val jsonObject = JSONObject(response.toString())

                    // Retrieves the values from the JSON Object
                    val username = jsonObject.getString("username")
                    val picture = jsonObject.getString("picture")
                    val status = jsonObject.getString("status")

                    val imageByteArray = ImagePFP.getImageByteArray(picture)
                    //The name of the pfp created with the username and the image extension file
                    val pictureName = ImagePFP.getImageName(username,picture);
                    ImagePFP.writeImageToDisk(imageByteArray,activity, pictureName)
                    // Self user
                    val user = Users(username,selfId, pictureName,status)
                    // If self user is not in database inserts it
                    if (selfId != userDao?.getUsersId(selfId)) {
                        // Sets all friends pictures
                        setFriendsPictures()
                        userDao?.insertUser(user)
                    }
                    // Request all chats of self user
                    requestSelfUserChats()

                },

                // Error response handle
                StringRequest@{ error ->
                    // Connection timed out validation
                    if(error is TimeoutError){
                        Log.i(null, "Timeout Error")
                        return@StringRequest
                    }

                    // No internet connection validation
                    if(error is NoConnectionError){
                        Log.i(null, "No connection Error")
                        return@StringRequest
                    }

                    val status = error.networkResponse.statusCode
                    if ( status == 404 || status == 405 || status == 400){
                        Log.i(null, error.networkResponse.statusCode.toString())


                        return@StringRequest
                    }
                    if (status == 401){
                        Log.i(null, error.networkResponse.statusCode.toString())
                        return@StringRequest
                    }

                    Log.i(null, error.networkResponse.statusCode.toString())
                    Log.i(null, error.toString())
                }
            ){
                /**
                 * Adds custom http request headers
                 */
                @Override
                override fun getHeaders(): MutableMap<String, String> {
                    val params: HashMap<String, String> = HashMap()

                    // Sets the custom headers
                    params["user"] = CredentialsManager.getCredential("user", KetchUp.getCurrentActivity())
                    params["session"] = CredentialsManager.getCredential("session-token", KetchUp.getCurrentActivity())

                    return params
                }
            }

            queue.add(request)

            // Initialize the WebSocket channel
            ChatWebSocket.createConnection(activity)
            ChatWebSocket.sendMessage("Test")

        }
    }
}