package com.example.weatherapp.viewmodels

import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.model.LocationWithWeather
import com.example.weatherapp.data.repository.WeatherRepository
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LocationSearchViewModel internal constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {
    var locationList: MutableList<Address> = mutableListOf()
    var locationNameList: MutableList<String> = mutableListOf()

    fun getFavoriteLocationsWeather(): Single<LocationWithWeather> {
        return weatherRepository.getWeatherFromLatLon(
            45.5, 34.6
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getFromLocationName(
        geocoder: Geocoder,
        location: String
    ): Observable<MutableList<Address>> {
        return Observable.create { emitter ->
            var locationList = geocoder.getFromLocationName(location, 5)
            emitter.onNext(locationList)
        }
    }

}