package com.ketchup.utils

import android.content.Context


class UrlFile {
    companion object{
        private const val key: String = "url_key"
        private const val defUrl ="http://127.0.0.1"
        fun writeUrl(url:String, context: Context ){
           val sharedPref = context.getSharedPreferences(key, Context.MODE_PRIVATE)?: return
            with(sharedPref.edit()) {
                putString(key, url)
                apply()
            }
        }
        fun readUrl(context: Context): String? {
            val sharedPref = context.getSharedPreferences(key, Context.MODE_PRIVATE) ?: return null

            return sharedPref.getString(key, defUrl)
        }
    }
}