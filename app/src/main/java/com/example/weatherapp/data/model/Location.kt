package com.example.weatherapp.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.google.gson.annotations.SerializedName

data class LocationWithWeather(
    @SerializedName("id")
    var id: Long,
    @SerializedName("weather")
    @Relation(parentColumn = "id", entityColumn = "locationId")
    var weather: List<Weather>,
    @SerializedName("main")
    @Embedded var main: MainInfo,
    @SerializedName("dt")
    var dt: Long,
    @SerializedName("timezone")
    var timezone: Long,
    @SerializedName("name")
    var name: String
) {
    constructor() : this(-1, listOf<Weather>(), MainInfo(), 0, 0, "")
}

@Entity
data class Location(
    @PrimaryKey var id: Long,
    @Embedded var main: MainInfo, //@Embedded
    var dt: Long,
    var timezone: Long,
    var name: String
) {
    constructor() : this(-1, MainInfo(), 0, 0, "")

    constructor(
        locationWithWeather: LocationWithWeather
    ) :
            this(
                locationWithWeather.id,
                locationWithWeather.main,
                locationWithWeather.dt,
                locationWithWeather.timezone,
                locationWithWeather.name
            )
}


