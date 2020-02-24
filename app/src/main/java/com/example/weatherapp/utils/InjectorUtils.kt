package com.example.weatherapp.utils

import android.content.Context
import android.location.Geocoder
import androidx.fragment.app.Fragment
import com.example.weatherapp.Application
import com.example.weatherapp.data.persistance.AppDatabase
import com.example.weatherapp.data.persistance.dao.LocationWeatherDao
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.data.repository.loader.OfflineDataLoader
import com.example.weatherapp.data.repository.loader.OnlineDataLoader
import com.example.weatherapp.networking.api.WeatherApiService
import com.example.weatherapp.viewmodels.*
import com.google.gson.Gson
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
private const val CACHE_SIZE = 10 * 1024 * 1024L
private const val TIMEOUT = 90L

object InjectorUtils {
    private var retrofit: Retrofit? = null
    private var connectionHelper: ConnectionHelper? = null
    private var locationWeatherDao: LocationWeatherDao? = null
    private var weatherRepository: WeatherRepository? = null
    private var offlineDataLoader: OfflineDataLoader? = null
    private var onlineDataLoader: OnlineDataLoader? = null
    private var weatherApiService: WeatherApiService? = null

    fun provideWeatherRepositoryForWeatherSyncJobService(context: Context): WeatherRepository {
        val connectionHelper = provideConnectionHelper(context)
        val locationWeatherDao = provideLocationWeatherDao(context)
        val onlineDataLoader = provideOnlineDataLoader(
            provideWeatherApiService(),
            locationWeatherDao
        )
        val offlineDataLoader = provideOfflineDataLoader(locationWeatherDao)
        return provideWeatherRepository(
            connectionHelper,
            onlineDataLoader,
            offlineDataLoader
        )
    }

    fun provideSettingsViewModelFactory(): SettingsViewModelFactory {
        return SettingsViewModelFactory()
    }

    fun provideLocationSearchViewModelFactory(fragment: Fragment): LocationSearchViewModelFactory {
        val connectionHelper = provideConnectionHelper(fragment.requireContext())
        val locationWeatherDao = provideLocationWeatherDao(fragment.requireContext())
        val onlineDataLoader = provideOnlineDataLoader(
            provideWeatherApiService(),
            locationWeatherDao
        )
        val offlineDataLoader = provideOfflineDataLoader(locationWeatherDao)
        val repository = provideWeatherRepository(
            connectionHelper,
            onlineDataLoader,
            offlineDataLoader
        )
        val locationHelper = provideLocationHelper()
        val geocoder = provideGeoDecoder(fragment.requireContext())
        return LocationSearchViewModelFactory(
            repository,
            connectionHelper,
            locationHelper,
            geocoder
        )
    }

    fun provideWeatherDetailsViewModelFactory(
        fragment: Fragment,
        locationId: Long
    ): WeatherDetailsViewModelFactory {
        val connectionHelper = provideConnectionHelper(fragment.requireContext())
        val locationWeatherDao = provideLocationWeatherDao(fragment.requireContext())
        val onlineDataLoader = provideOnlineDataLoader(
            provideWeatherApiService(),
            locationWeatherDao
        )
        val offlineDataLoader = provideOfflineDataLoader(locationWeatherDao)
        val repository = provideWeatherRepository(
            connectionHelper,
            onlineDataLoader,
            offlineDataLoader
        )
        return WeatherDetailsViewModelFactory(repository, locationId)
    }

    fun provideHomeViewPagerViewModelFactory(): HomeViewPagerViewModelFactory {
        return HomeViewPagerViewModelFactory()
    }


    fun provideLocationsListViewModelFactory(fragment: Fragment): LocationListViewModelFactory {
        val connectionHelper = provideConnectionHelper(fragment.requireContext())
        val locationWeatherDao = provideLocationWeatherDao(fragment.requireContext())
        val onlineDataLoader = provideOnlineDataLoader(
            provideWeatherApiService(),
            locationWeatherDao
        )
        val offlineDataLoader = provideOfflineDataLoader(locationWeatherDao)
        val repository = provideWeatherRepository(
            connectionHelper,
            onlineDataLoader,
            offlineDataLoader
        )
        return LocationListViewModelFactory(repository)
    }

    fun provideCurrentLocationViewModelFactory(fragment: Fragment): CurrentLocationViewModelFactory {
        val connectionHelper = provideConnectionHelper(fragment.requireContext())
        val locationWeatherDao = provideLocationWeatherDao(fragment.requireContext())
        val onlineDataLoader = provideOnlineDataLoader(
            provideWeatherApiService(),
            locationWeatherDao
        )
        val offlineDataLoader = provideOfflineDataLoader(locationWeatherDao)
        val repository = provideWeatherRepository(
            connectionHelper,
            onlineDataLoader,
            offlineDataLoader
        )
        val locationHelper = provideLocationHelper()
        return CurrentLocationViewModelFactory(repository, locationHelper)
    }

    fun provideConnectionHelper(context: Context): ConnectionHelper = connectionHelper
        ?: ConnectionHelper.getInstance(context.applicationContext).apply {
            connectionHelper = this
        }

    private fun provideCache(): Cache? {
        var cache: Cache? = null
        try {
            cache = Cache(File(Application.Cache.DIR, "http-cache"), CACHE_SIZE)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return cache
    }

    private fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    private fun provideOfflineCacheInterceptor(): Interceptor = Interceptor { chain ->
        var request = chain.request()
        val cacheControl = CacheControl.Builder().maxStale(10, TimeUnit.MINUTES).build()
        try {
            request = request.newBuilder().cacheControl(cacheControl).build()
        } catch (e: Exception) {
        }
        chain.proceed(request)
    }

    private fun provideOkHttpClientWithLoggerAndCaching(): OkHttpClient {

        return OkHttpClient.Builder()
            .addInterceptor(provideHttpLoggingInterceptor())
            .addInterceptor(provideOfflineCacheInterceptor())
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .apply {
                Application.Cache.DIR?.let {
                    cache(provideCache())
                }
            }.build()
    }

    private fun provideRetrofit(): Retrofit = retrofit ?: Retrofit.Builder()
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(Gson()))
        .baseUrl(BASE_URL)
        .client(provideOkHttpClientWithLoggerAndCaching())
        .build().apply { retrofit = this }

    private fun provideLocationWeatherDao(context: Context): LocationWeatherDao = locationWeatherDao
        ?: AppDatabase.getInstance(context.applicationContext).locationWeatherDao().apply {
            locationWeatherDao = this
        }

    private fun provideWeatherRepository(
        connectionHelper: ConnectionHelper,
        onlineDataLoader: OnlineDataLoader,
        offlineDataLoader: OfflineDataLoader
    ): WeatherRepository = weatherRepository ?: WeatherRepository(
        connectionHelper,
        onlineDataLoader,
        offlineDataLoader
    )

    private fun provideLocationHelper(): LocationHelper = LocationHelper()

    private fun provideGeoDecoder(context: Context): Geocoder = Geocoder(context, Locale.ENGLISH)

    private fun provideOnlineDataLoader(
        weatherApiService: WeatherApiService,
        locationWeatherDao: LocationWeatherDao
    ) = onlineDataLoader ?: OnlineDataLoader(
        weatherApiService,
        locationWeatherDao
    ).apply { onlineDataLoader = this }

    private fun provideOfflineDataLoader(
        locationWeatherDao: LocationWeatherDao
    ) = offlineDataLoader ?: OfflineDataLoader(locationWeatherDao).apply {
        offlineDataLoader = this
    }

    private fun provideWeatherApiService(): WeatherApiService =
        weatherApiService ?: provideRetrofit().create(WeatherApiService::class.java)
}