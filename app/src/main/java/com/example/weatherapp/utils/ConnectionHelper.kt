package com.example.weatherapp.utils

import android.content.Context
import android.net.ConnectivityManager

class ConnectionHelper(private val context: Context) {

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: ConnectionHelper? = null

        fun getInstance(context: Context): ConnectionHelper {
            return instance ?: synchronized(this) {
                instance ?: buildConnectionHelper(context).also { instance = it }
            }
        }

        private fun buildConnectionHelper(context: Context): ConnectionHelper {
            return buildConnectionHelper(context)
        }
    }

    fun isOnline(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connectivityManager.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }
}