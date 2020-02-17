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
            loadWeatherForLocationsFromNetwork(ids, emitter)
        }
    }

//    private fun shouldUpdate(page: Int, forced: Boolean) = when {
//        forced -> true
//        !connectionHelper.isOnline() -> false
//        else -> {
//            val lastUpdate = preferencesHelper.loadLong(LAST_UPDATE_KEY + page)
//            val currentTime = calendarWrapper.getCurrentTimeInMillis()
//            lastUpdate + Constants.REFRESH_LIMIT < currentTime
//        }
//    }

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
            val weather =
                weatherApiService.getWeatherForLocations(ids.joinToString(",")).execute().body()
            if (weather != null) {
//                userDao.insertAll(users.items)
//                val currentTime = calendarWrapper.getCurrentTimeInMillis()
                emitter.onSuccess(weather.list)
            } else {
                emitter.onError(Exception("No data received"))
            }
        } catch (exception: Exception) {
            emitter.onError(exception)
        }
    }


//    private fun loadOfflineUsers(page: Int, emitter: SingleEmitter<UserListModel>) {
//        val users = userDao.getUsers(page)
//        if (!users.isEmpty()) {
//            emitter.onSuccess(UserListModel(users))
//        } else {
//            emitter.onError(Exception("Device is offline"))
//        }
//    }
}