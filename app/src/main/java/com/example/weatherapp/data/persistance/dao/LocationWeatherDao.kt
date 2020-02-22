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


    @Transaction
    @Query("SELECT * FROM location , favoritelocation WHERE location.locationId = favoritelocation.locationId")
    abstract fun getFavoriteLocations(): List<LocationWithWeather>

    @Transaction
    @Query("SELECT * FROM location")
    abstract fun getAllLocation(): List<LocationWithWeather>

    @Transaction
    @Query("SELECT * FROM location WHERE locationId=:locationId")
    abstract fun getLocationById(locationId : Long): LocationWithWeather

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

    @Query("DELETE FROM location")
    abstract fun deleteLocationData()

    @Query("DELETE FROM locationweathercrossref")
    abstract fun deleteLocationWeatherCrossRefData()

    @Query("DELETE FROM weather")
    abstract fun deleteWeatherData()

    @Query("DELETE FROM favoritelocation WHERE locationId=:locationId")
    abstract fun deleteFavoriteLocation(locationId: Long)
}