package com.example.weatherapp.data.repository

import com.example.weatherapp.data.model.LocationWithWeather
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

    fun getWeatherFromLocationName(locationName: String): Single<LocationWithWeather> {
        return Single.create<LocationWithWeather> { emitter: SingleEmitter<LocationWithWeather> ->
            loadWeatherByLocationNameFromNetwork(locationName, emitter)
        }
    }

    fun getWeatherFromLatLon(lat: Double, lon: Double): Single<LocationWithWeather> {
        return Single.create<LocationWithWeather> { emitter: SingleEmitter<LocationWithWeather> ->
            loadWeatherLatLonFromNetwork(lat, lon, emitter)
        }
    }

    fun getWeatherForLocation(locationId: Long,online: Boolean = true): Single<LocationWithWeather> {
        return Single.create<LocationWithWeather> { emitter: SingleEmitter<LocationWithWeather> ->
            if (connectionHelper.isOnline() && online) {
                loadWeatherLocationFromNetwork(locationId, emitter)
            } else {
                loadWeatherLocationFromDb(locationId, emitter)
            }
        }
    }

    fun getWeatherForLocations(ids: List<Long>): Single<List<LocationWithWeather>> {
        return Single.create<List<LocationWithWeather>> { emitter: SingleEmitter<List<LocationWithWeather>> ->
            if (connectionHelper.isOnline()) {
                loadWeatherForLocationsFromNetwork(ids, emitter)
            } else {
                loadWeatherForLocationsFromDb(emitter)
            }
        }
    }

    //online
    private fun loadWeatherByLocationNameFromNetwork(
        locationName: String,
        emitter: SingleEmitter<LocationWithWeather>
    ) {
        try {
            val weather = weatherApiService.getWeatherForLocationByName(locationName).execute().body()
            if (weather != null) {
                locationWeatherDao.insertWeatherForLocation(weather)
                emitter.onSuccess(weather)
            } else {
                emitter.onError(Exception("No data received"))
            }
        } catch (exception: Exception) {
            emitter.onError(exception)
        }
    }


    private fun loadWeatherLocationFromNetwork(
        locationId: Long,
        emitter: SingleEmitter<LocationWithWeather>
    ) {
        try {
            val weather = weatherApiService.getWeatherForLocation(locationId).execute().body()
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

    private fun loadWeatherLatLonFromNetwork(
        lat: Double,
        lon: Double,
        emitter: SingleEmitter<LocationWithWeather>
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
        emitter: SingleEmitter<List<LocationWithWeather>>
    ) {
        try {
            val weathers =
                weatherApiService.getWeatherForLocations(ids.joinToString(",")).execute().body()
            if (weathers != null) {
                locationWeatherDao.deleteAllData()
                locationWeatherDao.insertWeatherForLocations(weathers.list)
                emitter.onSuccess(weathers.list)
            } else {
                emitter.onError(Exception("No data received"))
            }
        } catch (exception: Exception) {
            emitter.onError(exception)
        }
    }

    //offline

    private fun loadWeatherForLocationsFromDb(emitter: SingleEmitter<List<LocationWithWeather>>) {
        val locations = locationWeatherDao.getAllLocation()
        if (!locations.isEmpty()) {
            emitter.onSuccess(locations)
        } else {
            emitter.onError(Exception("Device is offline"))
        }
    }

    private fun loadWeatherLocationFromDb(locationId: Long, emitter: SingleEmitter<LocationWithWeather>) {
        val location = locationWeatherDao.getLocationById(locationId)
        emitter.onSuccess(location)
    }

}