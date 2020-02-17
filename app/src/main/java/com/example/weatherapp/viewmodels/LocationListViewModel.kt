package com.example.weatherapp.viewmodels

import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.model.Location
import com.example.weatherapp.data.repository.WeatherRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LocationListViewModel internal constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    fun getFavoriteLocationsWeather(): Single<List<Location>> {
        return weatherRepository.getWeatherForLocations(
            listOf(524901, 2643743,681290)
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}