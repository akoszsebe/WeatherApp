package com.example.weatherapp.data.model

import com.google.gson.annotations.SerializedName

//@Entity
data class Weather(
    @SerializedName("id") var id: Long?,
    @SerializedName("main") var main: String?,
    @SerializedName("description") var description: String?,
    @SerializedName("icon") var icon: String?
) {
    constructor() : this(-1, "", "", "")
}
