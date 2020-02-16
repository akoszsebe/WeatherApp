package com.example.weatherapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class Location(
    @PrimaryKey @ColumnInfo(name = "id") val locationId: String,
    val name: String
)