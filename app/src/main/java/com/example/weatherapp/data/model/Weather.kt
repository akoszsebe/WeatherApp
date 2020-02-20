package com.example.weatherapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Weather(
    @SerializedName("id") @ColumnInfo(name = "weatherId") @PrimaryKey
    var id: Long,
    @SerializedName("main")
    var main: String,
    @SerializedName("description")
    var description: String,
    @SerializedName("icon")
    var icon: String
) {
    constructor() : this(-1, "", "", "")
}
