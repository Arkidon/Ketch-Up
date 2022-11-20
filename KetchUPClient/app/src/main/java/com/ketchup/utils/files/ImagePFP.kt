package com.ketchup.utils.files

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.ketchup.app.KetchUp
import com.ketchup.app.view.UserList
import java.io.*
import java.net.URLConnection

class ImagePFP {
    companion object{
        /** Decodes Base64 into ByteArray
         * @see Base64.decode
         *  @return ByteArray
        */
        fun getImageByteArray(picture: String):ByteArray{
            return Base64.decode(picture, Base64.DEFAULT)
        }

        /** Gets the image extension from the ByteArray
         * splitting the returned string from guessContentTypeFromStream
         * @param imageByteArray A ByteArray with image codes
         * @see URLConnection.guessContentTypeFromStream
         *  @return String
         */
        fun getImageExtension(imageByteArray: ByteArray): String{
           return URLConnection.guessContentTypeFromStream(ByteArrayInputStream(imageByteArray)).split("/")[1]
        }

        fun getImageExtension(picture: String): String{
            return URLConnection.guessContentTypeFromStream(ByteArrayInputStream(getImageByteArray(picture))).split("/")[1]
        }
        /** Gets the imageName
         * @param username the username of the user who has the image
         * @param picture the Base64 code of the images
         * @see ImagePFP.getImageExtension
         *  @return String
         */
        fun getImageName(username:String, picture: String):String{
            return "$username.${getImageExtension(getImageByteArray(picture))}"
        }

        private const val PATHNAME : String = "profiles-pictures"

        /** Gets a ByteArray from application internal files and converted it to a Bitmap
         *
         *
         * @param context The app context
         * @param filename The name of the pfp user with image extension
         * @return An image Bitmap
         */

        fun readImageFromDisk(context: Context, filename : String): Bitmap {
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

        fun writeImageToDisk(picture: ByteArray, context: Context, filename : String){
            val path = File(context.filesDir.toPath().toString()+ File.pathSeparator + PATHNAME)
            if (!path.exists()) path.mkdir()
            val file = File(path, filename)
            if (!file.exists()) file.createNewFile()
            val fos = FileOutputStream(file)
            fos.write(picture,0 , picture.size)
        }
        // function to set all pictures of database users
         fun setFriendsPictures(){
            val activity = KetchUp.getCurrentActivity()
            for (i in 0 until UserList.userList.size) {
                UserList.userList[i].pictureBitmap =
                    UserList.userList[i].pfp?.let { readImageFromDisk(activity, it) }
            }
        }
    }
    }
