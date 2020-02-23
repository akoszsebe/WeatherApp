package com.example.weatherapp.utils

enum class AutoRefresh (val value: Long) {
    NEVER(0),
    EVERY_HOUR(  3600000L),
    EVERY_6HOUR( 21600000L),
    EVERY_12HOUR(43200000L),
    EVERY_24HOUR(86400000L);

    companion object {
        private val map = values().associateBy(AutoRefresh::ordinal)
        fun valueOf(type: Int) = map[type] ?: NEVER
    }
}