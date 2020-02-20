package com.example.weatherapp.viewmodels

import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.model.LocationWithWeather
import com.example.weatherapp.data.repository.WeatherRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LocationSearchViewModel internal constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    fun getFavoriteLocationsWeather(): Single<LocationWithWeather> {
        return weatherRepository.getWeatherFromLatLon(
            45.5, 34.6
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}