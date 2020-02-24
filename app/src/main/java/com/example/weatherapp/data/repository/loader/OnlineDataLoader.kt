package com.example.weatherapp.data.repository.loader

import com.example.weatherapp.data.model.FiveDayForecast
import com.example.weatherapp.data.model.LocationWithWeather
import com.example.weatherapp.data.persistance.dao.LocationWeatherDao
import com.example.weatherapp.networking.api.WeatherApiService
import io.reactivex.SingleEmitter

class OnlineDataLoader(
    private val weatherApiService: WeatherApiService,
    private val locationWeatherDao: LocationWeatherDao
) {

    fun syncAllData(emitter: SingleEmitter<Unit>) {
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
                    for (location in weathers.list) {
                        val fiveDayForecast =
                            weatherApiService.getFiveDayForecastForLocationWithId(location.id)
                                .execute()
                                .body()
                        if (fiveDayForecast != null) {
                            locationWeatherDao.insertFiveDayForecast(fiveDayForecast)
                        }
                    }
                }
            }
            emitter.onSuccess(Unit)
        } catch (e: Exception) {
            emitter.onError(e)
        }
    }

    fun loadFiveDayForecastByLocationIdFromNetwork(
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

    fun loadWeatherByLocationNameFromNetwork(
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


    fun loadWeatherLocationFromNetwork(
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

    fun loadWeatherLatLonFromNetwork(
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

    fun loadWeatherForLocationsFromNetwork(
        emitter: SingleEmitter<List<LocationWithWeather>>
    ) {
        try {
            val ids: List<Long> =
                locationWeatherDao.getAllFavoriteLocations().map { it.locationId }
            if (ids.isNullOrEmpty()) {
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
}