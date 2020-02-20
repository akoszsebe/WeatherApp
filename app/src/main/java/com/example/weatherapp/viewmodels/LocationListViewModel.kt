package com.example.weatherapp.viewmodels

import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.model.LocationWithWeather
import com.example.weatherapp.data.repository.WeatherRepository
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LocationListViewModel internal constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {
    var list: List<LocationWithWeather> = listOf()

    fun getFavoriteLocationsWeather(): Single<List<LocationWithWeather>> {
        return weatherRepository.getWeatherForLocations(
            listOf(524901, 2643743, 681290)
        ).doOnSuccess {
            list = it
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun filter(text: String): Single<List<LocationWithWeather>> {
        return Single.create<List<LocationWithWeather>> { emitter: SingleEmitter<List<LocationWithWeather>> ->
            emitter.onSuccess(list.filter { it.name.toUpperCase().startsWith(text.toUpperCase()) })
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}