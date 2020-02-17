package com.example.weatherapp.data.model

import com.google.gson.annotations.SerializedName

//@Entity
data class MainInfo(
//    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "main_id") var id: Long = 0,
    @SerializedName("temp") var temp: Double?,
    @SerializedName("feels_like") var feelsLike: Double?,
    @SerializedName("temp_min") var tempMin: Double?,
    @SerializedName("temp_max") var tempMax: Double?,
    @SerializedName("pressure") var pressure: Long?,
    @SerializedName("humidity") var humidity: Long?
) {
    constructor() : this(0.0, 0.0, 0.0, 0.0, 0, 0)
}
