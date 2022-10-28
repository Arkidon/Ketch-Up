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


class EmptyResponseJsonRequest(method: Int, url: String, jsonRequest: JSONObject?, listener: Listener<JSONObject>,
                               errorListener: ErrorListener?) :
    JsonObjectRequest(method, url,jsonRequest, listener, errorListener) {

    /** Lock to guard mListener as it is cleared on cancel() and read on delivery.  */
    private val mLock = Any()

    @GuardedBy("mLock")
    private var mListener: Listener<String>? = null

    @Override
    override fun parseNetworkResponse(response: NetworkResponse?): Response<JSONObject> {
        return if (response != null && response.data.isEmpty()) {
            val jsonObject = JSONObject()

            // Dumb jsonObject
            jsonObject.put("empty", "empty")

            Response.success(jsonObject, HttpHeaderParser.parseCacheHeaders(response))
        } else{
            Response.error(ResponseNotEmpty())
        }
    }

    @Override
    override fun deliverResponse(response: JSONObject?) {
        var listener: Listener<String>?
        synchronized(mLock) { listener = mListener}
        if (listener != null) {
            listener!!.onResponse(response.toString())
        }
    }

    @Override
    override fun cancel() {
        super.cancel()
        synchronized(mLock) { mListener = null }
    }
}