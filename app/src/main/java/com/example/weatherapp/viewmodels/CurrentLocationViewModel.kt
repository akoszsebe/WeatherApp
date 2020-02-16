package com.example.weatherapp.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.model.Location
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.utils.InjectorUtils
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CurrentLocationViewModel internal constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val weatherRepository: WeatherRepository =
        InjectorUtils.provideWeatherRepository(InjectorUtils.provideRetrofit())

    fun getCurrentLocationWeather(): Single<Location>? {
        return weatherRepository.getWeatherFromLatLon(
            52.4901, 26.43743
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}