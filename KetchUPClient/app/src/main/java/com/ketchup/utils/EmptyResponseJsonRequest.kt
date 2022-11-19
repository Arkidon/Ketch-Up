package com.ketchup.utils

import androidx.annotation.GuardedBy
import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.Response.ErrorListener
import com.android.volley.Response.Listener
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import com.ketchup.exceptions.ResponseNotEmpty
import org.json.JSONObject

/**
 * Custom Volley request for handling JSON requests that return an empty response body"
 */

class EmptyResponseJsonRequest(method: Int, url: String, jsonRequest: JSONObject?, listener: Listener<JSONObject>,
                               errorListener: ErrorListener?) :
    JsonObjectRequest(method, url, jsonRequest, listener, errorListener) {

    /** Lock to guard mListener as it is cleared on cancel() and read on delivery.  */
    private val lock = Any()

    private var headers = HashMap<String, String>()

    @GuardedBy("lock")
    private var listener: Listener<String>? = null

    @Override
    override fun parseNetworkResponse(response: NetworkResponse?): Response<JSONObject> {
        return if (response != null && response.data.isEmpty()) {
            // Dumb jsonObject
            val jsonObject = JSONObject()
            jsonObject.put("empty", "empty")

            Response.success(jsonObject, HttpHeaderParser.parseCacheHeaders(response))
        } else{
            Response.error(ResponseNotEmpty())
        }
    }

    @Override
    override fun deliverResponse(response: JSONObject?) {
        var listener: Listener<String>?
        synchronized(lock) { listener = this.listener }
        if (listener != null) {
            listener!!.onResponse(response.toString())
        }
    }

    /**
     * Adds custom headers to the request
     */
    fun addHeader(key: String, value: String){
        headers[key] = value
    }

    @Override
    override fun getHeaders(): MutableMap<String, String> {
        return headers
    }

    @Override
    override fun cancel() {
        super.cancel()
        synchronized(lock) { listener = null }
    }
}