package com.example.weatherapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.data.repository.WeatherRepository

class CurrentLocationViewModelFactory (
    private val weatherRepository: WeatherRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(
        modelClass: Class<T>
    ): T {
        return CurrentLocationViewModel(weatherRepository) as T
    }
}