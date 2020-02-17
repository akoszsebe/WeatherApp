package com.example.weatherapp.data.persistance.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.data.model.Location

@Dao
interface LocationWeatherDao {
    @Query("SELECT * FROM location")
    fun getAllLocation(): List<Location>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(users: List<Location>)
}