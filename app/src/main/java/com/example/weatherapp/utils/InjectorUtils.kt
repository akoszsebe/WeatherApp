package com.example.weatherapp.utils

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.weatherapp.data.persistance.AppDatabase
import com.example.weatherapp.data.persistance.dao.LocationWeatherDao
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.networking.api.WeatherApiService
import com.example.weatherapp.viewmodels.CurrentLocationViewModelFactory
import com.example.weatherapp.viewmodels.LocationListViewModelFactory
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

object InjectorUtils {
    fun provideLocationsListViewModelFactory(fragment: Fragment): LocationListViewModelFactory {
        val repository = provideWeatherRepository(
            provideRetrofit(),
            provideLocationWeatherDao(fragment.requireContext()),
            provideConnectionHelper(fragment.requireContext())
        )
        return LocationListViewModelFactory(repository)
    }

    fun provideCurrentLocationViewModelFactory(fragment: Fragment): CurrentLocationViewModelFactory {
        val repository = provideWeatherRepository(
            provideRetrofit(),
            provideLocationWeatherDao(fragment.requireContext()),
            provideConnectionHelper(fragment.requireContext())
        )
        return CurrentLocationViewModelFactory(repository)
    }

    private fun provideHttpLogger(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.apply { interceptor.level = HttpLoggingInterceptor.Level.BODY }
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }


    private fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(Gson()))
        .baseUrl(BASE_URL)
        .client(provideHttpLogger())
        .build()

    private fun provideLocationWeatherDao(context: Context): LocationWeatherDao =
        AppDatabase.getInstance(context.applicationContext).locationWeatherDao()

    private fun provideConnectionHelper(context: Context): ConnectionHelper =
        ConnectionHelper(context.applicationContext)

    private fun provideWeatherRepository(
        retrofit: Retrofit,
        locationWeatherDao: LocationWeatherDao,
        connectionHelper: ConnectionHelper
    ): WeatherRepository {
        return WeatherRepository(
            retrofit.create(WeatherApiService::class.java),
            locationWeatherDao,
            connectionHelper
        )
    }

}