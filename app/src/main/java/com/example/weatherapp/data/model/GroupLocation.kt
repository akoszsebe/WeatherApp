package com.example.weatherapp.data.model

import com.google.gson.annotations.SerializedName

data class GroupLocation(
    @SerializedName("list") var list: List<LocationWithWeather>
)