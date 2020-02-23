package com.example.weatherapp.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

private const val CURRENT_LOCATION: String = "current_location"
private const val UNIT_OF_MEASUREMENT: String = "unit_of_measurement"

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

}