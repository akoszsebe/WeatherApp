package com.example.weatherapp.utils

import android.text.format.DateUtils

object Extensions {

    fun isTomorrow(timeInMilliseconds: Long): Boolean {
        return DateUtils.isToday(timeInMilliseconds - DateUtils.DAY_IN_MILLIS)
    }

    infix fun Double.of(unitOfMeasurement: UnitOfMeasurement): Double {
        return when (unitOfMeasurement) {
            UnitOfMeasurement.KELVIN -> this
            UnitOfMeasurement.CELSIUS -> this - 273.15
            UnitOfMeasurement.FAHRENHEIT -> this * 1.8 - 459.67
        }
    }

    infix fun String.of(unitOfMeasurement: UnitOfMeasurement): String {
        return when (unitOfMeasurement) {
            UnitOfMeasurement.KELVIN -> this + "K"
            UnitOfMeasurement.CELSIUS -> "$this°C"
            UnitOfMeasurement.FAHRENHEIT -> "$this°F"
        }
    }
}

