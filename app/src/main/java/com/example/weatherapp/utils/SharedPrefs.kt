package com.example.weatherapp.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

private const val CURRENT_LOCATION: String = "current_location"
private const val UNIT_OF_MEASUREMENT: String = "unit_of_measurement"
private const val AUTO_REFRESH: String = "auto_refresh"

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

    fun getUnitOfMeasurement(): UnitOfMeasurement {
        val sharedPref: SharedPreferences =
            context.getSharedPreferences(UNIT_OF_MEASUREMENT, MODE_PRIVATE)
        return UnitOfMeasurement.valueOf(sharedPref.getInt(UNIT_OF_MEASUREMENT, 0))
    }

    fun setUnitOfMeasurement(unitOfMeasurement: UnitOfMeasurement) {
        val sharedPref: SharedPreferences =
            context.getSharedPreferences(UNIT_OF_MEASUREMENT, MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putInt(UNIT_OF_MEASUREMENT, unitOfMeasurement.value)
        editor.apply()
    }

    fun getAutoRefresh(): AutoRefresh {
        val sharedPref: SharedPreferences =
            context.getSharedPreferences(AUTO_REFRESH, MODE_PRIVATE)
        return AutoRefresh.valueOf(sharedPref.getInt(AUTO_REFRESH, 0))
    }

    fun setAutoRefresh(autoRefresh: AutoRefresh) {
        val sharedPref: SharedPreferences =
            context.getSharedPreferences(AUTO_REFRESH, MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putInt(AUTO_REFRESH, autoRefresh.ordinal)
        editor.apply()
    }

}