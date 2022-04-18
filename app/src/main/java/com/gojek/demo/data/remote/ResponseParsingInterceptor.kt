package com.gojek.demo.data.remote

import android.text.TextUtils
import android.util.Log
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Response
import okhttp3.ResponseBody

class ResponseParsingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response: Response = chain.proceed(chain.request())
        val responseString: String = response.body()?.string() ?: ""
        val newResponse = response.newBuilder()
        var contentType = response.header("Content-Type")
        if (TextUtils.isEmpty(contentType))
            contentType = "application/json;charset=UTF-8"
        newResponse.body(
            ResponseBody.create(
                MediaType.parse(contentType), responseString ?: ""
            )
        )
        return newResponse.build()
    }
}