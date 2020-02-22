package com.example.weatherapp.data.model

import com.google.gson.annotations.SerializedName

data class FiveDayForecast (
    @SerializedName("list")
    var list: List<ListElement>,
    var city: City
)

data class City (
    var id: Long,
    var name: String,
    var country: String,
    var timezone: Long,
    var sunrise: Long,
    var sunset: Long
)

data class ListElement (
    var dt: Long,
    var main: MainInfo,
    var weather: List<Weather>,
    @SerializedName("dt_txt")
    var dtTxt: String
)