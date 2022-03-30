package com.gojek.demo.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NetworkConnectionUtil @Inject constructor(@ApplicationContext var context: Context?) {

    fun isNetworkAvailable(): Boolean {
        context?.applicationContext?.apply {
            val connectManager: ConnectivityManager? =
                getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            val wifiManager: WifiManager? =
                getApplicationContext().getSystemService(Context.WIFI_SERVICE) as? WifiManager

            val netInfo: NetworkInfo? = connectManager?.activeNetworkInfo
            if (netInfo?.isConnected ?: false || wifiManager?.isWifiEnabled ?: false) {
                return true
            }
        }
        return false
    }
}