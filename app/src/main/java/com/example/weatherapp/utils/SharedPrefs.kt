package com.example.weatherapp.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences


class SharedPrefs(private val context: Context) {

    fun getFavoriteLocations(): List<String> {
        val sharedPref: SharedPreferences =
            context.getSharedPreferences("favorite_locations", MODE_PRIVATE)
        var result = sharedPref.getStringSet("favorite_locations", HashSet())
        return when (result) {
            null -> listOf()
            else -> result.toList()
        }
    }

    fun setFavoriteLocations(locations: List<String>) {
        val sharedPref: SharedPreferences =
            context.getSharedPreferences("favorite_locations", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putStringSet("favorite_locations", locations.toSet())
        editor.apply()
    }

}