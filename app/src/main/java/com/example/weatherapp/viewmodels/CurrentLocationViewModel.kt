package com.example.weatherapp.viewmodels

import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.model.LocationWithWeather
import com.example.weatherapp.data.repository.WeatherRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CurrentLocationViewModel internal constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    fun getCurrentLocationWeather(): Single<LocationWithWeather> {
        return weatherRepository.getWeatherForLocationWithLatLon(
            52.4901, 26.43743
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}