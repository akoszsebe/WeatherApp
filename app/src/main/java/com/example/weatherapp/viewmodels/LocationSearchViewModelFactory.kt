package com.example.weatherapp.viewmodels

import android.location.Geocoder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.utils.ConnectionHelper
import com.example.weatherapp.utils.LocationHelper

class LocationSearchViewModelFactory(
    private val weatherRepository: WeatherRepository,
    private val connectionHelper: ConnectionHelper,
    private val locationHelper: LocationHelper,
    private val geocoder: Geocoder
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(
        modelClass: Class<T>
    ): T {
        return LocationSearchViewModel(weatherRepository,connectionHelper,locationHelper,geocoder) as T
    }
}