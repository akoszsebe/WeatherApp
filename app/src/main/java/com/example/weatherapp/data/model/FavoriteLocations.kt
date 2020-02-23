package com.example.weatherapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteLocation(
    @PrimaryKey
    var locationId: Long
)