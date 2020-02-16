package com.example.weatherapp.data.model

import com.google.gson.annotations.SerializedName

data class Coord (
    @SerializedName("lon") var lon: Double,
    @SerializedName("lat") var lat: Double
)