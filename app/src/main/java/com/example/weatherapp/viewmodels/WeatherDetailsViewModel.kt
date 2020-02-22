package com.example.weatherapp.viewmodels

import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.model.FiveDayForecast
import com.example.weatherapp.data.model.LocationWithWeather
import com.example.weatherapp.data.repository.WeatherRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class WeatherDetailsViewModel internal constructor(
    private val weatherRepository: WeatherRepository,
    private val locationId: Long
) : ViewModel() {

    fun getFiveDayForecastForLocationWithId(): Single<FiveDayForecast> {
        return weatherRepository.getFiveDayForcastForLocationWithId(
            locationId
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getWeatherForLocationWithId(online: Boolean = true): Single<LocationWithWeather> {
        return weatherRepository.getWeatherForLocationWithId(
            locationId, online
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}