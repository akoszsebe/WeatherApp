package com.example.weatherapp.utils

import androidx.fragment.app.Fragment
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.networking.api.WeatherApiService
import com.example.weatherapp.viewmodels.CurrentLocationViewModel
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
        //val repository = getPlantRepository(fragment.requireContext())
        return LocationListViewModelFactory(fragment)
    }

    fun provideCurrentLocationViewModelFactory(fragment: Fragment): CurrentLocationViewModelFactory {
        //val repository = getPlantRepository(fragment.requireContext())
        return CurrentLocationViewModelFactory(fragment)
    }

    fun provideHttpLogger() : OkHttpClient {
        var interceptor = HttpLoggingInterceptor()
        interceptor.apply { interceptor.level = HttpLoggingInterceptor.Level.BODY }
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }


    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(Gson()))
        .baseUrl(BASE_URL)
        .client(provideHttpLogger())
        .build()

    fun provideWeatherRepository(
        retrofit: Retrofit
    ): WeatherRepository {
        return WeatherRepository(
            retrofit.create(WeatherApiService::class.java)
        )
    }
}