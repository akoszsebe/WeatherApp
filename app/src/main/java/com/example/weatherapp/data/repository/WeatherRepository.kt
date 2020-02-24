package com.example.weatherapp.data.repository

import com.example.weatherapp.data.model.FiveDayForecast
import com.example.weatherapp.data.model.LocationWithWeather
import com.example.weatherapp.data.repository.loader.OfflineDataLoader
import com.example.weatherapp.data.repository.loader.OnlineDataLoader
import com.example.weatherapp.utils.ConnectionHelper
import io.reactivex.Single
import io.reactivex.SingleEmitter

class WeatherRepository(
    private val connectionHelper: ConnectionHelper,
    private val onlineDataLoader: OnlineDataLoader,
    private val offlineDataLoader: OfflineDataLoader
) {
    fun syncAllDate(): Single<Unit> {
        return Single.create<Unit> { emitter: SingleEmitter<Unit> ->
            if (connectionHelper.isOnline()) {
                onlineDataLoader.syncAllData(emitter)
            }
        }
    }

    fun getFiveDayForecastForLocationWithId(locationId: Long): Single<FiveDayForecast> {
        return Single.create<FiveDayForecast> { emitter: SingleEmitter<FiveDayForecast> ->
            if (connectionHelper.isOnline()) {
                onlineDataLoader.loadFiveDayForecastByLocationIdFromNetwork(locationId, emitter)
            } else {
                offlineDataLoader.loadFiveDayForecastByLocationIdFromDb(locationId, emitter)
            }
        }
    }

    fun getWeatherForLocationWithName(locationName: String): Single<LocationWithWeather> {
        return Single.create<LocationWithWeather> { emitter: SingleEmitter<LocationWithWeather> ->
            if (connectionHelper.isOnline()) {
                onlineDataLoader.loadWeatherByLocationNameFromNetwork(locationName, emitter)
            }
        }
    }

    fun getWeatherForLocationWithLatLon(lat: Double, lon: Double): Single<LocationWithWeather> {
        return Single.create<LocationWithWeather> { emitter: SingleEmitter<LocationWithWeather> ->
            if (connectionHelper.isOnline()) {
                onlineDataLoader.loadWeatherLatLonFromNetwork(lat, lon, emitter)
            }
        }
    }

    fun getWeatherForLocationWithId(
        locationId: Long,
        online: Boolean = true
    ): Single<LocationWithWeather> {
        return Single.create<LocationWithWeather> { emitter: SingleEmitter<LocationWithWeather> ->
            if (connectionHelper.isOnline() && online) {
                onlineDataLoader.loadWeatherLocationFromNetwork(locationId, emitter)
            } else {
                offlineDataLoader.loadWeatherLocationFromDb(locationId, emitter)
            }
        }
    }

    fun getWeatherForFavoriteLocations(): Single<List<LocationWithWeather>> {
        return Single.create<List<LocationWithWeather>> { emitter: SingleEmitter<List<LocationWithWeather>> ->
            if (connectionHelper.isOnline()) {
                onlineDataLoader.loadWeatherForLocationsFromNetwork(emitter)
            } else {
                offlineDataLoader.loadWeatherForFavoriteLocationsFromDb(emitter)
            }
        }
    }

    //offline
    fun addToFavorites(locationId: Long): Single<Unit> {
        return Single.create<Unit> { emitter: SingleEmitter<Unit> ->
            offlineDataLoader.addToFavorites(locationId, emitter)
        }
    }

    fun removeFromFavorites(locationId: Long): Single<Unit> {
        return Single.create<Unit> { emitter: SingleEmitter<Unit> ->
            offlineDataLoader.removeFromFavorites(locationId, emitter)
        }
    }

    fun isFavoriteLocationWithId(locationId: Long): Single<Boolean> {
        return Single.create<Boolean> { emitter: SingleEmitter<Boolean> ->
            offlineDataLoader.isFavoriteLocationWithId(locationId, emitter)
        }
    }


}