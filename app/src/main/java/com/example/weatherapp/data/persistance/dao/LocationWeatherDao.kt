package com.example.weatherapp.data.persistance.dao

import androidx.room.*
import com.example.weatherapp.data.model.*

@Dao
abstract class LocationWeatherDao {

    fun insertWeatherForLocations(list: List<LocationWithWeather>) {
        for (item in list) {
            insertWeatherForLocation(item)
        }
    }

    fun deleteAllData() {
        deleteLocationWeatherCrossRefData()
        deleteWeatherData()
        deleteLocationData()
    }

    fun insertWeatherForLocation(locationWithWeather: LocationWithWeather) {
        val location = Location(locationWithWeather)
        for (weather in locationWithWeather.weather) {
            insertLocationWeatherCrossRef(LocationWeatherCrossRef(location.id, weather.id))
        }
        insertLocation(location)
        insertWeatherList(locationWithWeather.weather)
    }

    fun insertFiveDayForecast(fiveDayForecast: FiveDayForecast) {
        val fiveDayForecastDb = FiveDayForecastDb(fiveDayForecast)
        insertFiveDayForecastDb(fiveDayForecastDb)
        val insertedWeatherListElements = insertWeatherListElementList(fiveDayForecast.list)
        for (weatherListElement in insertedWeatherListElements) {
            insertFiveDayForecastListElementCrossRef(
                FiveDayForecastListElementCrossRef(
                    fiveDayForecastDb.city.id,
                    weatherListElement
                )
            )
        }

        val t = getAllFiveDayForecast()
        println(t)
    }

    @Transaction
    @Query("SELECT * FROM fivedayforecastdb")
    abstract fun getAllFiveDayForecast(): List<FiveDayForecast>

    @Transaction
    @Query("SELECT * FROM location , favoritelocation WHERE location.locationId = favoritelocation.locationId")
    abstract fun getFavoriteLocations(): List<LocationWithWeather>

    @Transaction
    @Query("SELECT * FROM location")
    abstract fun getAllLocation(): List<LocationWithWeather>

    @Transaction
    @Query("SELECT locationId FROM location")
    abstract fun getAllLocationIds(): List<Long>

    @Transaction
    @Query("SELECT * FROM location WHERE locationId=:locationId")
    abstract fun getLocationById(locationId: Long): LocationWithWeather

    @Transaction
    @Query("SELECT * FROM favoritelocation")
    abstract fun getAllFavoriteLocations(): List<FavoriteLocation>

    @Transaction
    @Query("SELECT * FROM favoritelocation WHERE locationId=:locationId")
    abstract fun isFavoriteLocation(locationId: Long): FavoriteLocation?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertFavoriteLocation(favoriteLocation: FavoriteLocation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertLocation(locations: Location)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertLocationWeatherCrossRef(locationWeatherCrossRef: LocationWeatherCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertWeatherList(weather: List<Weather>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertFiveDayForecastDb(fiveDayForecastDb: FiveDayForecastDb)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertFiveDayForecastListElementCrossRef(fiveDayForecastListElementCrossRef: FiveDayForecastListElementCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertWeatherListElementList(list: List<WeatherListElement>): List<Long>

    @Query("DELETE FROM location")
    abstract fun deleteLocationData()

    @Query("DELETE FROM locationweathercrossref")
    abstract fun deleteLocationWeatherCrossRefData()

    @Query("DELETE FROM weather")
    abstract fun deleteWeatherData()

    @Query("DELETE FROM favoritelocation WHERE locationId=:locationId")
    abstract fun deleteFavoriteLocation(locationId: Long)
}