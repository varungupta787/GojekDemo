package com.gojek.demo.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import com.gojek.demo.ui.RepoApplication
import dagger.hilt.android.qualifiers.ActivityContext

object NetworkUtils {
    const val BASE_URL = "https://api.github.com/"
    const val REPO_URL = "repos/octocat/hello-world"

    const val UNEXPECTED_ERR_MSG = "Unexpected Error"

    //Http Status Codes
    const val SESSION_EXPIRED = 401
    const val UNAUTHORIZED = 403
    const val DEFAULT = 101

//error messages
    const val unexpected_err_msg = "Something unexpected happened, please retry."
    const val default_err_message = "Something went wrong! Please try again"
    const val server_err_message = "Server is not reachable! Please try again later."
    const val session_expired_err_message = "Your session has expired, please login again."
    const val unauthorized_err_message = "Access Denied. User is unauthorized."
    const val bad_server_err_message = "Bad Server Error."

}