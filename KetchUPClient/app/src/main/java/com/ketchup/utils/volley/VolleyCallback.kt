package com.ketchup.utils.volley

interface VolleyCallback {
    fun onSuccess(result: Int?, function: () -> Unit)
}