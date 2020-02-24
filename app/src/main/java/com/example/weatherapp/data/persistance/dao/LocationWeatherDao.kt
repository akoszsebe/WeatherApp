package com.example.weatherapp.data.persistance.dao

import androidx.room.*
import com.example.weatherapp.data.model.*
import com.example.weatherapp.data.persistance.typeconverter.Converter

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
        deleteAllWeatherListElements()
        deleteAllFiveDayForcasts()
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
        deleteWeatherListElement(fiveDayForecastDb.id)
        for (weatherListElement in fiveDayForecast.list) {
            weatherListElement.fiveDayForecastId = fiveDayForecastDb.id
        }
        insertWeatherListElementList(fiveDayForecast.list)
    }

    @Transaction
    @TypeConverters(Converter::class)
    @Query("SELECT * FROM fivedayforecastdb WHERE fiveDayForecastId=:locationId")
    abstract fun getFiveDayForecastByLocationId(locationId: Long): FiveDayForecast?

    @Transaction
    @Query("SELECT * FROM fivedayforecastdb")
    @TypeConverters(Converter::class)
    abstract fun getAllFiveDayForecast(): List<FiveDayForecast>

    @Transaction
    @Query("SELECT * FROM location , favoritelocation WHERE location.locationId = favoritelocation.locationId")
    abstract fun getFavoriteLocations(): List<LocationWithWeather>

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

    @TypeConverters(Converter::class)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertFiveDayForecastDb(fiveDayForecastDb: FiveDayForecastDb)

    @TypeConverters(Converter::class)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertWeatherListElementList(list: List<WeatherListElement>)

    @Query("DELETE FROM location")
    abstract fun deleteLocationData()

    @Query("DELETE FROM locationweathercrossref")
    abstract fun deleteLocationWeatherCrossRefData()

    @Query("DELETE FROM weather")
    abstract fun deleteWeatherData()

    @Query("DELETE FROM favoritelocation WHERE locationId=:locationId")
    abstract fun deleteFavoriteLocation(locationId: Long)

    @Query("DELETE FROM fivedayforecastdb")
    abstract fun deleteAllFiveDayForcasts()

    @Query("DELETE FROM weatherlistelement")
    abstract fun deleteAllWeatherListElements()

    @Query("DELETE FROM weatherlistelement WHERE fiveDayForecastId=:fiveDayForecastId")
    abstract fun deleteWeatherListElement(fiveDayForecastId: Long)
}