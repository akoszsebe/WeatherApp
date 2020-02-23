package com.example.weatherapp.data.persistance

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherapp.data.model.*
import com.example.weatherapp.data.persistance.dao.LocationWeatherDao

private const val DATABASE_NAME = "db-name"

@Database(
    entities = [
        (Location::class),
        (Weather::class),
        (LocationWeatherCrossRef::class),
        (FavoriteLocation::class),
        (FiveDayForecastDb::class),
        (FiveDayForecastListElementCrossRef::class),
        (WeatherListElement::class)
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun locationWeatherDao(): LocationWeatherDao

    companion object {
        var TEST_MODE = false
        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            if (TEST_MODE) {
                return Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
                    .allowMainThreadQueries().build()
            }
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()
        }
    }
}