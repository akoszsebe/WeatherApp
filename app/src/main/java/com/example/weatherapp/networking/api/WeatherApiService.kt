package com.example.weatherapp.networking.api

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.model.GroupLocation
import com.example.weatherapp.data.model.LocationWithWeather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("weather")
    fun getWeatherForLocation(
        @Query("id") locationId: Long,
        @Query("appid") appId: String = BuildConfig.OPENWEATHERMAP_KEY
    ): Call<LocationWithWeather>

    @GET("weather")
    fun getWeatherForLatLon(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appId: String = BuildConfig.OPENWEATHERMAP_KEY
    ): Call<LocationWithWeather>

    @GET("group")
    fun getWeatherForLocations(
        @Query("id",encoded = true) ids : String,
        @Query("appid") appId: String = BuildConfig.OPENWEATHERMAP_KEY
    ): Call<GroupLocation>
}