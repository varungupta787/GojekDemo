package com.gojek.demo.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import dagger.hilt.android.qualifiers.ActivityContext

class NetworkConnectionUtil(@ActivityContext val context: Context?) {

    fun isNetworkAvailable(): Boolean {
        context?.apply {
            val connectManager: ConnectivityManager? =
                applicationContext?.getApplicationContext()
                    ?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            val wifiManager: WifiManager? =
                applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager

            val netInfo: NetworkInfo? = connectManager?.activeNetworkInfo
            if (netInfo?.isConnected ?: false || wifiManager?.isWifiEnabled ?: false) {
                return true
            }
        }
        return false
    }
}