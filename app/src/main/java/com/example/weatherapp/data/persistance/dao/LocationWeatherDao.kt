package com.example.weatherapp.data.persistance.dao

import androidx.room.*
import com.example.weatherapp.data.model.Location
import com.example.weatherapp.data.model.LocationWithWeather
import com.example.weatherapp.data.model.Weather

@Dao
abstract class LocationWeatherDao {

    fun insertWeatherForLocations(list: List<LocationWithWeather>) {
        for (item in list) {
            insertWeatherForLocation(item)
        }
    }

    fun insertWeatherForLocation(locationWithWeather: LocationWithWeather) {
        val location = Location(locationWithWeather)
        for (weather in locationWithWeather.weather) {
            weather.locationId = location.id
        }
        _insertLocation(location)
        _insertWeatherList(locationWithWeather.weather)
    }

    @Transaction
    @Query("SELECT * FROM location")
    abstract fun getAllLocation(): List<LocationWithWeather>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun _insertLocation(locations: Location)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun _insertWeatherList(weather: List<Weather>)


}