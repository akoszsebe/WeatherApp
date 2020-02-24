package com.example.weatherapp.data.model

import androidx.room.*
import com.example.weatherapp.data.persistance.typeconverter.Converter
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
)

@Entity
data class FiveDayForecastDb(
    @PrimaryKey @ColumnInfo(name = "fiveDayForecastId")
    var id: Long,
    @Embedded
    var city: City
) {
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
)

@Entity
data class WeatherListElement(
    @ColumnInfo(name = "weatherListElementId") @PrimaryKey(autoGenerate = true)
    var id: Long,
    @ColumnInfo(name = "fiveDayForecastId")
    var fiveDayForecastId: Long,
    var dt: Long,
    @Embedded
    var main: MainInfo,
    @TypeConverters(Converter::class)
    var weather: List<Weather>
)



