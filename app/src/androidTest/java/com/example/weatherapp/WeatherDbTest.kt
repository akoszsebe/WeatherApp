package com.example.weatherapp

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.weatherapp.data.model.Location
import com.example.weatherapp.data.persistance.AppDatabase
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WeatherDbTest {

    private var db: AppDatabase? = null

    @Before
    fun setup() {
        AppDatabase.TEST_MODE = true
        db = AppDatabase.getInstance(InstrumentationRegistry.getInstrumentation().context)
    }

    @After
    fun tearDown() {
        db?.close()
    }

    @Test
    fun can_insert_location() {
        val location = Location()
        db?.locationWeatherDao()?.insertLocation(location)
        val locationTest = db?.locationWeatherDao()?.getLocationById(location.id)!!
        Assert.assertEquals(location.name, locationTest.name)
    }

    @Test
    fun check_replace_location() {
        val name1 = "Location1"
        val name2 = "Location2"
        val location1 = Location()
        location1.id = 1
        location1.name = name1
        val location2 = Location()
        location2.id = 1
        location2.name = name2
        db?.locationWeatherDao()?.insertLocation(location1)
        db?.locationWeatherDao()?.insertLocation(location2)
        val locationTest = db?.locationWeatherDao()?.getLocationById(location1.id)!!
        Assert.assertEquals(name2, locationTest.name)
    }


}