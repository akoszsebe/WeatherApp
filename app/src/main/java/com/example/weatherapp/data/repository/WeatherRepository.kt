package com.example.weatherapp.data.repository

import com.example.weatherapp.data.model.FavoriteLocation
import com.example.weatherapp.data.model.FiveDayForecast
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
    fun syncAllDate(): Single<Unit> {
        return Single.create<Unit> { emitter: SingleEmitter<Unit> ->
            if (connectionHelper.isOnline()) {
                syncAllData(emitter)
            }
        }
    }

    fun getFiveDayForecastForLocationWithId(locationId: Long): Single<FiveDayForecast> {
        return Single.create<FiveDayForecast> { emitter: SingleEmitter<FiveDayForecast> ->
            if (connectionHelper.isOnline()) {
                loadFiveDayForecastByLocationIdFromNetwork(locationId, emitter)
            }
        }
    }

    fun getWeatherForLocationWithName(locationName: String): Single<LocationWithWeather> {
        return Single.create<LocationWithWeather> { emitter: SingleEmitter<LocationWithWeather> ->
            if (connectionHelper.isOnline()) {
                loadWeatherByLocationNameFromNetwork(locationName, emitter)
            }
        }
    }

    fun getWeatherForLocationWithLatLon(lat: Double, lon: Double): Single<LocationWithWeather> {
        return Single.create<LocationWithWeather> { emitter: SingleEmitter<LocationWithWeather> ->
            if (connectionHelper.isOnline()) {
                loadWeatherLatLonFromNetwork(lat, lon, emitter)
            }
        }
    }

    fun getWeatherForLocationWithId(
        locationId: Long,
        online: Boolean = true
    ): Single<LocationWithWeather> {
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

    private fun syncAllData(emitter: SingleEmitter<Unit>){
        try {
            val locationIds = locationWeatherDao.getAllLocationIds()
            if (locationIds.isNotEmpty()) {
                val weathers =
                    weatherApiService.getWeatherForLocationsWithIds(locationIds.joinToString(","))
                        .execute()
                        .body()
                if (weathers != null) {
                    locationWeatherDao.deleteAllData()
                    locationWeatherDao.insertWeatherForLocations(weathers.list)
                }
            }
            emitter.onSuccess(Unit)
        } catch (e : Exception){
            emitter.onError(e)
        }
    }

    private fun loadFiveDayForecastByLocationIdFromNetwork(
        locationId: Long,
        emitter: SingleEmitter<FiveDayForecast>
    ) {
        try {
            val fiveDayForecast =
                weatherApiService.getFiveDayForecastForLocationWithId(locationId).execute().body()
            if (fiveDayForecast != null) {
                locationWeatherDao.insertFiveDayForecast(fiveDayForecast)
                emitter.onSuccess(fiveDayForecast)
            } else {
                emitter.onError(Exception("No data received"))
            }
        } catch (exception: Exception) {
            emitter.onError(exception)
        }
    }

    private fun loadWeatherByLocationNameFromNetwork(
        locationName: String,
        emitter: SingleEmitter<LocationWithWeather>
    ) {
        try {
            val weather =
                weatherApiService.getWeatherForLocationWithName(locationName).execute().body()
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
            val weather = weatherApiService.getWeatherForLocationWithId(locationId).execute().body()
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

    private fun loadWeatherLatLonFromNetwork(
        lat: Double,
        lon: Double,
        emitter: SingleEmitter<LocationWithWeather>
    ) {
        try {
            val weather =
                weatherApiService.getWeatherForLocationWithLatLon(lat, lon).execute().body()
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

    private fun loadWeatherForLocationsFromNetwork(
        ids: List<Long>,
        emitter: SingleEmitter<List<LocationWithWeather>>
    ) {
        try {
            if (ids.isNullOrEmpty()){
                emitter.onSuccess(listOf())
            } else {
                val weathers =
                    weatherApiService.getWeatherForLocationsWithIds(ids.joinToString(",")).execute()
                        .body()
                if (weathers != null) {
                    locationWeatherDao.insertWeatherForLocations(weathers.list)
                    emitter.onSuccess(weathers.list)
                } else {
                    emitter.onError(Exception("No data received"))
                }
            }
        } catch (exception: Exception) {
            emitter.onError(exception)
        }
    }

    //offline

    private fun loadWeatherForFavoriteLocationsFromDb(emitter: SingleEmitter<List<LocationWithWeather>>) {
        val locations = locationWeatherDao.getFavoriteLocations()
        if (locations.isNotEmpty()) {
            emitter.onSuccess(locations)
        } else {
            emitter.onError(Exception("Device is offline"))
        }
    }

    private fun loadWeatherForLocationsFromDb(emitter: SingleEmitter<List<LocationWithWeather>>) {
        val locations = locationWeatherDao.getAllLocation()
        if (locations.isNotEmpty()) {
            emitter.onSuccess(locations)
        } else {
            emitter.onError(Exception("Device is offline"))
        }
    }

    private fun loadWeatherLocationFromDb(
        locationId: Long,
        emitter: SingleEmitter<LocationWithWeather>
    ) {
        val location = locationWeatherDao.getLocationById(locationId)
        emitter.onSuccess(location)
    }

    fun addToFavorites(locationId: Long): Single<Unit> {
        return Single.create<Unit> { emitter: SingleEmitter<Unit> ->
            try {
                locationWeatherDao.insertFavoriteLocation(FavoriteLocation(locationId))
                emitter.onSuccess(Unit)
            } catch (e: Exception) {
                emitter.onError(e)
            }

        }
    }

    fun removeFromFavorites(locationId: Long): Single<Unit> {
        return Single.create<Unit> { emitter: SingleEmitter<Unit> ->
            try {
                locationWeatherDao.deleteFavoriteLocation(locationId)
                emitter.onSuccess(Unit)
            } catch (e: Exception) {
                emitter.onError(e)
            }

        }
    }

    fun isFavoriteLocationWithId(locationId: Long): Single<Boolean> {
        return Single.create<Boolean> { emitter: SingleEmitter<Boolean> ->
            try {
                val isFavorite = locationWeatherDao.isFavoriteLocation(locationId) != null
                emitter.onSuccess(isFavorite)
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }

    fun getWeatherForFavoriteLocations(): Single<List<LocationWithWeather>> {
        return Single.create<List<LocationWithWeather>> { emitter: SingleEmitter<List<LocationWithWeather>> ->
            if (connectionHelper.isOnline()) {
                val ids: List<Long> =
                    locationWeatherDao.getAllFavoriteLocations().map { it.locationId }
                loadWeatherForLocationsFromNetwork(ids, emitter)
            } else {
                loadWeatherForFavoriteLocationsFromDb(emitter)
            }
        }
    }
}