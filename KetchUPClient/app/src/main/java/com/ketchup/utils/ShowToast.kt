package com.ketchup.utils

import android.content.Context
import android.widget.Toast
class ShowToast{
    //class to make toast
    companion object{
        //static var and function to create and show toasts
    var toast : Toast? = null
        fun  showToast(applicationContext: Context, text: CharSequence?, duration: Int) {
            if (toast == null) toast = Toast.makeText(applicationContext, text, duration)
            else{
                toast?.cancel()
                toast?.setText(text)
            }
            toast?.show()
        }
    }
}
