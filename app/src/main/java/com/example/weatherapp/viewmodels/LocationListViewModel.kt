package com.example.weatherapp.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.Location

class LocationListViewModel internal constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val locations: MutableList<Location> =
        mutableListOf(
            Location("121", "Turin"),
            Location("122", "Milan"),
            Location("123", "Turin"),
            Location("124", "Milan"),
            Location("125", "Turin"),
            Location("126", "Milan"),
            Location("127", "Turin"),
            Location("128", "Milan"),
            Location("129", "Turin"),
            Location("130", "Milan"),
            Location("131", "Turin"),
            Location("132", "Milan"),
            Location("133", "Turin"),
            Location("134", "Milan")
        )
}