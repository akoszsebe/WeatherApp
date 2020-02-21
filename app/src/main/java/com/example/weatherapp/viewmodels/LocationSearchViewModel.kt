package com.example.weatherapp.viewmodels

import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.model.LocationWithWeather
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.utils.ConnectionHelper
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.IOException

class LocationSearchViewModel internal constructor(
    private val weatherRepository: WeatherRepository,
    val connectionHelper: ConnectionHelper
) : ViewModel() {
    var locationList: MutableList<Address> = mutableListOf()
    var locationNameList: MutableList<String> = mutableListOf()

    fun getLocationWeatherByName(locationName: String): Single<LocationWithWeather> {
        return weatherRepository.getWeatherFromLocationName(
            locationName
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getFromLocationName(
        geocoder: Geocoder,
        location: String
    ): Observable<MutableList<Address>> {
        return Observable.create { emitter ->
            try {
                val locationList = geocoder.getFromLocationName(location, 5)
                emitter.onNext(locationList)
            } catch (ex : IOException){
                emitter.onError(ex)
            }

        }
    }

}