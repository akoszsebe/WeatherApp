package com.example.weatherapp.data.model

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Weather(
    @SerializedName("id") @PrimaryKey var id: Long,
    @SerializedName("main") var main: String,
    @SerializedName("description") var description: String,
    @SerializedName("icon") var icon: String
)
