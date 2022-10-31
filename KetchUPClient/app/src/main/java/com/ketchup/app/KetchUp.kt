package com.ketchup.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 * This class provides a static reference
 * to the current application context.
 */
class KetchUp: Application() {
    companion object{
        @SuppressLint("StaticFieldLeak")
        private lateinit var context: Context

        fun getApplicationContext(): Context{
            return context
        }
    }

    /**
     * Calls the parent's constructor and stores the context statically
     */
    @Override
    override fun onCreate() {
        super.onCreate()
        KetchUp.context = applicationContext
    }

}