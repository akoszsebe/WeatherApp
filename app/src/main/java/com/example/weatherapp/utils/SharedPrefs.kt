package com.example.weatherapp.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

private const val CURRENT_LOCATION: String = "current_location"

class SharedPrefs(private val context: Context) {

    fun getCurrentLocation(): Long {
        val sharedPref: SharedPreferences =
            context.getSharedPreferences(CURRENT_LOCATION, MODE_PRIVATE)
        return sharedPref.getLong(CURRENT_LOCATION, 0)
    }


    fun setCurrentLocation(locationId: Long) {
        val sharedPref: SharedPreferences =
            context.getSharedPreferences(CURRENT_LOCATION, MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putLong(CURRENT_LOCATION, locationId)
        editor.apply()
    }

}