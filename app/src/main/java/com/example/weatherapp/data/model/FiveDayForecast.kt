package com.example.weatherapp.data.model

import androidx.room.*
import com.google.gson.annotations.SerializedName

data class FiveDayForecast(
    @Embedded
    var city: City,
    @SerializedName("list")
    @Relation(
        parentColumn = "fiveDayForecastId",
        entityColumn = "fiveDayForecastId"
    )
    var list: List<WeatherListElement>,
    @ColumnInfo(name = "fiveDayForecastId")
    var id: Long
) {
    constructor() : this(City(), listOf<WeatherListElement>(), 0)
}

@Entity
data class FiveDayForecastDb(
    @PrimaryKey @ColumnInfo(name = "fiveDayForecastId")
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
    @ColumnInfo(name = "weatherListElementId") @PrimaryKey(autoGenerate = true)
    var id: Long,
    @ColumnInfo(name = "fiveDayForecastId")
    var fiveDayForecastId: Long,
    var dt: Long,
    @Embedded
    var main: MainInfo,
    @Ignore var weather: List<Weather>
) {
    constructor() : this(0, -1, -1, MainInfo(), listOf<Weather>())
}


