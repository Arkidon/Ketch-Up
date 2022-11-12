package com.ketchup.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.ketchup.app.KetchUp
/**
 * Manages the reading and writing of the user credentials used to validate
 * against the server
 */
class CredentialsManager {
    companion object{
       // File name to store the preferences
        private const val credentialsFile = "credentials"

        /**
         * @param key The name of the key to store
         * @param value The value linked to the key
         * @param activity An activity object to get the shared preferences
         */
        fun setCredential(key: String, value: String, activity: AppCompatActivity){
            // Object to access the shared Preferences
            val sharedPreferences = activity.getSharedPreferences(credentialsFile, Context.MODE_PRIVATE)

            // Stores the credential
            with(sharedPreferences.edit()){
                putString(key, value)
                commit()
            }
        }

        /**
         * @param key The name of the key to retrieve
         * @param activity An activity object to get the shared preferences
         * @return A String with the value linked to the specified key
         */
        fun getCredential(key: String, activity: AppCompatActivity): String {
            val sharedPreferences = activity.getSharedPreferences(credentialsFile, Context.MODE_PRIVATE)
            return sharedPreferences.getString(key, "").toString()
        }
    }
}