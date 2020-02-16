package com.example.weatherapp.utils

import androidx.fragment.app.Fragment
import com.example.weatherapp.viewmodels.LocationListViewModelFactory
object InjectorUtils {
    fun provideLocationsListViewModelFactory(fragment: Fragment): LocationListViewModelFactory {
        //val repository = getPlantRepository(fragment.requireContext())
        return LocationListViewModelFactory(fragment)
    }
}