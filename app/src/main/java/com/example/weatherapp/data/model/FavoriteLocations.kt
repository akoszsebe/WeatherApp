package com.example.weatherapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteLocation(
//    @PrimaryKey(autoGenerate = true)
//    var id: Long,
    @PrimaryKey
    var locationId: Long
){
    constructor() : this(-1)
}