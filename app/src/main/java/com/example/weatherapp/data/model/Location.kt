package com.example.weatherapp.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Location(
    @SerializedName("id") @PrimaryKey var id: Long? = null,
    @SerializedName("weather") @Ignore @Embedded var weather: List<Weather>? = null,
    @SerializedName("main") @Embedded var main: MainInfo? = null, //@Embedded
    @SerializedName("dt") var dt: Long? = null,
    @SerializedName("timezone") var timezone: Long? = null,
    @SerializedName("name") var name: String?
) {
    constructor() : this(-1, listOf<Weather>(), MainInfo(), 0, 0, "")
}