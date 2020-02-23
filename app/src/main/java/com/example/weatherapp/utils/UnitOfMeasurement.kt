package com.example.weatherapp.utils

enum class UnitOfMeasurement(val value: Int){
    CELSIUS(0),
    FAHRENHEIT(1),
    KELVIN(2);

    companion object {
        private val map = values().associateBy(UnitOfMeasurement::ordinal)
        fun valueOf(type: Int) = map[type] ?: KELVIN
    }
}