package com.example.weatherapp.data.repository.loader

import com.example.weatherapp.data.model.FavoriteLocation
import com.example.weatherapp.data.model.FiveDayForecast
import com.example.weatherapp.data.model.LocationWithWeather
import com.example.weatherapp.data.persistance.dao.LocationWeatherDao
import io.reactivex.SingleEmitter

class OfflineDataLoader(private val locationWeatherDao: LocationWeatherDao) {

    fun loadFiveDayForecastByLocationIdFromDb(
        locationId: Long,
        emitter: SingleEmitter<FiveDayForecast>
    ) {
        val locations = locationWeatherDao.getFiveDayForecastByLocationId(locationId)
        if (locations != null) {
            emitter.onSuccess(locations)
        } else {
            emitter.onError(Exception("Device is offline"))
        }
    }


    fun loadWeatherForFavoriteLocationsFromDb(emitter: SingleEmitter<List<LocationWithWeather>>) {
        val locations = locationWeatherDao.getFavoriteLocations()
        if (locations.isNotEmpty()) {
            emitter.onSuccess(locations)
        } else {
            emitter.onError(Exception("Device is offline"))
        }
    }

    fun loadWeatherLocationFromDb(
        locationId: Long,
        emitter: SingleEmitter<LocationWithWeather>
    ) {
        val location = locationWeatherDao.getLocationById(locationId)
        emitter.onSuccess(location)
    }

    fun addToFavorites(locationId: Long, emitter: SingleEmitter<Unit>) {
        try {
            locationWeatherDao.insertFavoriteLocation(FavoriteLocation(locationId))
            emitter.onSuccess(Unit)
        } catch (e: Exception) {
            emitter.onError(e)
        }
    }

    fun removeFromFavorites(locationId: Long, emitter: SingleEmitter<Unit>) {
        try {
            locationWeatherDao.deleteFavoriteLocation(locationId)
            emitter.onSuccess(Unit)
        } catch (e: Exception) {
            emitter.onError(e)
        }
    }

    fun isFavoriteLocationWithId(locationId: Long, emitter: SingleEmitter<Boolean>) {
        try {
            val isFavorite = locationWeatherDao.isFavoriteLocation(locationId) != null
            emitter.onSuccess(isFavorite)
        } catch (e: Exception) {
            emitter.onError(e)
        }
    }


}