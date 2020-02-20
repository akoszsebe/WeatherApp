package com.example.weatherapp.data.model

import androidx.room.*
import com.google.gson.annotations.SerializedName

data class LocationWithWeather(
    @SerializedName("id") @ColumnInfo(name = "locationId")
    var id: Long,
    @SerializedName("weather") @Relation(
        parentColumn = "locationId",
        entityColumn = "weatherId",
        associateBy = Junction(LocationWeatherCrossRef::class)
    )
    var weather: List<Weather>,
    @SerializedName("main") @Embedded
    var main: MainInfo,
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
    @PrimaryKey @ColumnInfo(name = "locationId")
    var id: Long,
    @Embedded
    var main: MainInfo,
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

@Entity(primaryKeys = ["locationId", "weatherId"])
class LocationWeatherCrossRef(
    var locationId: Long = 0,
    var weatherId: Long = 0
)


