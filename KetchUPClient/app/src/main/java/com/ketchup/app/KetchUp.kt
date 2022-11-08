package com.ketchup.app

import android.app.Application
import androidx.appcompat.app.AppCompatActivity

/**
 * This class provides a static reference
 * to the current application context
 * and executes code in the application startup.
 */
class KetchUp: Application() {
    companion object{
        private var userID: Long? = null
        private lateinit var activity: AppCompatActivity

        fun getCurrentActivity(): AppCompatActivity{
            return activity
        }

        fun setCurrentActivity(activity: AppCompatActivity){
            this.activity = activity
        }

        fun getUserID(): Long?{
            return if(userID != null){
                return userID
            } else{
                return null
            }
        }
    }

    /**
     * onCreate is called when the application launches
     */
    @Override
    override fun onCreate() {
        super.onCreate()
    }
}