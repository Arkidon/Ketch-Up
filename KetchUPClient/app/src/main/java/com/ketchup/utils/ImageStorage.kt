package com.ketchup.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Path
import android.graphics.Picture
import android.os.Build
import android.util.Base64.decode
import androidx.annotation.RequiresApi
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.charset.Charset
import java.util.*

class ImageStorage {
    companion object{
        private const val PATHNAME : String = "profiles-pictures"

        /** Gets a ByteArray from application internal files and converted it to a Bitmap
         *
         *
         * @param context The app context
         * @param filename The name of the pfp user with image extension
         * @return An image Bitmap
         */

        fun readImageFromDisk(context: Context, filename : String): Bitmap{
            val path = context.filesDir.toPath().toString()+ File.pathSeparator+ PATHNAME
            val file = File(path, filename)
            val fos = FileInputStream(file)
            val base64Image : ByteArray = fos.readBytes()
            return BitmapFactory.decodeByteArray(base64Image, 0, base64Image.size)
        }

        /** Writes a ByteArray in application internal files named with username.extension
         *
         * @param picture the image ByteArray
         * @param context The app context
         * @param filename The name of the pfp user with image extension
         *
         */

        fun writeImageToDisk(picture: ByteArray, context: Context,filename : String){
            val path = File(context.filesDir.toPath().toString()+File.pathSeparator +PATHNAME)
            if (!path.exists()) path.mkdir()
            val file = File(path, filename)
            if (!file.exists()) file.createNewFile()
            val fos = FileOutputStream(file)
            fos.write(picture,0 , picture.size)
        }
    }
}