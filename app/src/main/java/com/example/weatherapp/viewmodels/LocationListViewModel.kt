package com.example.weatherapp.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.model.Location
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.networking.api.WeatherApiService
import com.example.weatherapp.utils.InjectorUtils
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LocationListViewModel internal constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val weatherRepository: WeatherRepository =
        InjectorUtils.provideWeatherRepository(InjectorUtils.provideRetrofit())

    fun getFavoriteLocationsWeather(): Single<List<Location>>? {
        return weatherRepository.getWeatherForLocations(
            listOf(524901, 2643743,681290)
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}