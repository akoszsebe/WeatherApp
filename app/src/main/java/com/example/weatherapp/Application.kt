package com.example.weatherapp

import android.app.Application
import java.io.File

class Application : Application() {
    object Cache {
        @JvmStatic
        var DIR: File? = null
    }

    override fun onCreate() {
        super.onCreate()
        Cache.DIR = cacheDir
    }
}