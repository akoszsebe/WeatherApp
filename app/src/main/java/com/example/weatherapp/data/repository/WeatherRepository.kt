package com.example.weatherapp.data.repository

import com.example.weatherapp.data.model.Location
import com.example.weatherapp.data.persistance.dao.LocationWeatherDao
import com.example.weatherapp.networking.api.WeatherApiService
import com.example.weatherapp.utils.ConnectionHelper
import io.reactivex.Single
import io.reactivex.SingleEmitter

class WeatherRepository(
    private val weatherApiService: WeatherApiService,
    private val locationWeatherDao: LocationWeatherDao,
    private val connectionHelper: ConnectionHelper
) {

    fun getWeatherFromLatLon(lat: Double, lon: Double): Single<Location> {
        return Single.create<Location> { emitter: SingleEmitter<Location> ->
            loadWeatherLatLonFromNetwork(lat, lon, emitter)
        }
    }

    fun getWeatherForLocations(ids: List<Long>): Single<List<Location>> {
        return Single.create<List<Location>> { emitter: SingleEmitter<List<Location>> ->
            if(connectionHelper.isOnline()) {
                loadWeatherForLocationsFromNetwork(ids, emitter)
            } else {
                loadWeatherForLocationsFromDb(emitter)
            }
        }
    }

    private fun loadWeatherLatLonFromNetwork(
        lat: Double,
        lon: Double,
        emitter: SingleEmitter<Location>
    ) {
        try {
            val weather = weatherApiService.getWeatherForLatLon(lat, lon).execute().body()
            if (weather != null) {
//                userDao.insertAll(users.items)
//                val currentTime = calendarWrapper.getCurrentTimeInMillis()
                emitter.onSuccess(weather)
            } else {
                emitter.onError(Exception("No data received"))
            }
        } catch (exception: Exception) {
            emitter.onError(exception)
        }
    }

    private fun loadWeatherForLocationsFromNetwork(
        ids: List<Long>,
        emitter: SingleEmitter<List<Location>>
    ) {
        try {
            val weathers =
                weatherApiService.getWeatherForLocations(ids.joinToString(",")).execute().body()
            if (weathers != null) {
                locationWeatherDao.insertAll(weathers.list)
                emitter.onSuccess(weathers.list)
            } else {
                emitter.onError(Exception("No data received"))
            }
        } catch (exception: Exception) {
            emitter.onError(exception)
        }
    }

    private fun loadWeatherForLocationsFromDb(emitter: SingleEmitter<List<Location>>) {
        val locations = locationWeatherDao.getAllLocation()
        if (!locations.isEmpty()) {
            emitter.onSuccess(locations)
        } else {
            emitter.onError(Exception("Device is offline"))
        }
    }

}