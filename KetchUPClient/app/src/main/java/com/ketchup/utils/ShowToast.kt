package com.ketchup.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.internal.synchronized

class ShowToast{
    //class to make toast
    companion object{
        // Static var and function to create and show toasts
        var toast : Toast? = null
        fun showToast(applicationContext: Context, text: CharSequence?, duration: Int) {
            // Checks if the toast is not null
            if (toast != null){
                // Cancels the previous toast
                toast?.cancel()

                // Sets the toast to null, so the next iterarion does not
                toast = null

                // Recursive call
                showToast(applicationContext, text, duration)
            }

            else{
                // Displays a toast
                toast = Toast.makeText(applicationContext, text, duration)
                toast?.setText(text)
                toast?.show()
            }
        }
    }
}
