package com.example.weatherapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.data.repository.WeatherRepository

class WeatherDetailsViewModelFactory(
    private val weatherRepository: WeatherRepository,
    private val locationId: Long
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(
        modelClass: Class<T>
    ): T {
        return WeatherDetailsViewModel(weatherRepository,locationId) as T
    }
}