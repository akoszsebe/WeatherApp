package com.example.weatherapp.data.model

import com.google.gson.annotations.SerializedName

//@Entity
data class MainInfo(
    @SerializedName("temp")
    var temp: Double?,
    @SerializedName("feels_like")
    var feelsLike: Double?,
    @SerializedName("temp_min")
    var tempMin: Double?,
    @SerializedName("temp_max")
    var tempMax: Double?,
    @SerializedName("pressure")
    var pressure: Long?,
    @SerializedName("humidity")
    var humidity: Long?
) {
    constructor() : this(272.15, 272.15, 272.15, 272.15, 0, 0)
}
