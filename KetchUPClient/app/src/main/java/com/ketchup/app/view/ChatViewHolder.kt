package com.ketchup.app.view

import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.ketchup.app.KetchUp
import com.ketchup.app.R
import com.ketchup.app.database.ChatEntries
import com.ketchup.utils.files.CredentialsManager
import java.time.LocalDate
class ChatViewHolder (view: View): RecyclerView.ViewHolder(view){
    val messageText = view.findViewById<TextView>(R.id.textMessage)
    val messageDate = view.findViewById<TextView>(R.id.textDate)

    fun render(chatEntry: ChatEntries){
        // Sets the text value, retrieved from the ChatEntries object
        messageText.text = chatEntry.text

        // Placeholder date value
        messageDate.text = LocalDate.now().toString()

        /* Checks if the chatEntry was sent by self user or not,
           if the text entry was sent by the user himself, it turns red and to the right
           if not it is put to the left and in blue
        */
        if (chatEntry.user_sender != CredentialsManager.getCredential("user", KetchUp.getCurrentActivity()).toInt()){
                messageText.gravity= Gravity.START
                messageText.background = AppCompatResources.getDrawable(KetchUp.getCurrentActivity(), R.drawable.background_recieved_message)
                val textParams = messageText.layoutParams as ConstraintLayout.LayoutParams
                textParams.endToEnd = ConstraintSet.UNSET
                val dateParams = messageDate.layoutParams as ConstraintLayout.LayoutParams
                dateParams.endToEnd = ConstraintSet.UNSET
                messageText.requestLayout()
                messageDate.requestLayout()
        }else{
            messageText.background = AppCompatResources.getDrawable(KetchUp.getCurrentActivity(), R.drawable.background_sent_message)
            val textParams = messageText.layoutParams as ConstraintLayout.LayoutParams
            textParams.endToEnd = ConstraintSet.PARENT_ID
            val dateParams = messageDate.layoutParams as ConstraintLayout.LayoutParams
            dateParams.endToEnd = ConstraintSet.PARENT_ID
            messageText.requestLayout()
            messageDate.requestLayout()
        }



    }
}