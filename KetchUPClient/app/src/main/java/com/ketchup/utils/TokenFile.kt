package com.ketchup.utils

import android.content.Context
import java.io.File
import java.nio.charset.Charset

class TokenFile {
    companion object{
        private const val filename : String = "token"

        fun readToken(context: Context): String{
            val file: File = File(context.filesDir, filename)
            return file.readText(Charset.defaultCharset())
        }

        fun writeToken(token: String, context: Context){
            val file: File = File(context.filesDir, filename)
            file.writeText(token, Charset.defaultCharset())
        }
    }
}