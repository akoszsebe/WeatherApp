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
        return weatherRepository.getFiveDayForecastForLocationWithId(
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

    fun isFavoriteLocation(): Single<Boolean> {
        return weatherRepository.isFavoriteLocationWithId(
            locationId
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun addToFavorites(): Single<Unit> {
        return weatherRepository.addToFavorites(
            locationId
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun removeFromFavorites(): Single<Unit> {
        return weatherRepository.removeFromFavorites(
            locationId
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}