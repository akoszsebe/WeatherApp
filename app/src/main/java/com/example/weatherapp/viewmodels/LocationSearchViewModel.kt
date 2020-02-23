package com.example.weatherapp.viewmodels

import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.model.LocationWithWeather
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.utils.ConnectionHelper
import com.example.weatherapp.utils.LocationHelper
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.IOException

class LocationSearchViewModel internal constructor(
    private val weatherRepository: WeatherRepository,
    val connectionHelper: ConnectionHelper,
    val locationHelper: LocationHelper,
    val geocoder: Geocoder
) : ViewModel() {

    var locationList: MutableList<Address> = mutableListOf()
    var locationNameList: MutableList<String> = mutableListOf()

    fun getLocationWeatherByName(locationName: String): Single<LocationWithWeather> {
        return weatherRepository.getWeatherForLocationWithName(
            locationName
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun addToFavorites(locationId: Long): Single<Unit> {
        return weatherRepository.addToFavorites(
            locationId
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getFromLocationName(
        location: String
    ): Single<MutableList<Address>> {
        return Single.create { emitter: SingleEmitter<MutableList<Address>> ->
            try {
                val locationList = geocoder.getFromLocationName(location, 5)
                emitter.onSuccess(locationList)
            } catch (ex: IOException) {
                emitter.onError(ex)
            }

        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}