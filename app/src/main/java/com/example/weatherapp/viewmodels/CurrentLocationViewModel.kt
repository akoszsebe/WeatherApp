package com.example.weatherapp.viewmodels

import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.model.FiveDayForecast
import com.example.weatherapp.data.model.LocationWithWeather
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.utils.LocationHelper
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CurrentLocationViewModel internal constructor(
    private val weatherRepository: WeatherRepository,
    val locationHelper: LocationHelper
) : ViewModel() {

    var locationId: Long = 675937

    fun getCurrentLocationWeather(lat : Double, lon : Double): Single<LocationWithWeather> {
        return weatherRepository.getWeatherForLocationWithLatLon(
            lat, lon
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

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