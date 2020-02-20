package com.example.weatherapp.viewmodels

import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.repository.WeatherRepository

class WeatherDetailsViewModel internal constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {
}