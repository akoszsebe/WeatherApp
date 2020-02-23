package com.example.weatherapp.data.model

import androidx.room.*
import com.google.gson.annotations.SerializedName

data class FiveDayForecast(
    @SerializedName("list")
    @Relation(
        parentColumn = "locationId",
        entityColumn = "listElementId",
        associateBy = Junction(FiveDayForecastListElementCrossRef::class)
    )
    var list: List<WeatherListElement>,
    @Embedded
    var city: City,
    @ColumnInfo(name = "locationId")
    var id: Long
)

@Entity
data class FiveDayForecastDb(
    @PrimaryKey @ColumnInfo(name = "locationId")
    var id: Long,
    @Embedded
    var city: City
) {
    constructor() : this(-1, City())

    constructor(fiveDayForecast: FiveDayForecast) :
            this(
                fiveDayForecast.city.id,
                fiveDayForecast.city
            )
}

@Entity(primaryKeys = ["locationId", "listElementId"])
class FiveDayForecastListElementCrossRef(
    var locationId: Long = 0,
    var listElementId: Long = 0
)

data class City(
    var id: Long,
    var name: String,
    var country: String,
    var timezone: Long
) {
    constructor() : this(-1, "", "", 0L)
}

@Entity
data class WeatherListElement(
    @ColumnInfo(name = "listElementId") @PrimaryKey(autoGenerate = true)
    var id: Long,
    var dt: Long,
    @Embedded
    var main: MainInfo,
    @Ignore var weather: List<Weather>
){
    constructor(): this(0,-1, MainInfo(), listOf<Weather>())
}