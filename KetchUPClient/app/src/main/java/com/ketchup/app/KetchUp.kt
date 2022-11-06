package com.ketchup.app

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

/**
 * This class provides a static reference
 * to the current application context.
 */
class KetchUp: Application() {
    companion object{
        private lateinit var activity: AppCompatActivity

        fun getCurrentActivity(): AppCompatActivity{
            return activity
        }

        fun setCurrentActivity(activity: AppCompatActivity){
            this.activity = activity
        }
    }

    /**
     * Calls the parent's constructor and stores the context statically
     */
    @Override
    override fun onCreate() {
        super.onCreate()
    }
}