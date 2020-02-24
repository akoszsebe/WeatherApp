package com.example.weatherapp.data.persistance.typeconverter

import androidx.room.TypeConverter
import com.example.weatherapp.data.model.Weather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class Converter {

    companion object {
        @TypeConverter
        @JvmStatic
        fun fromString(value: String?): List<Weather> {
            val listType: Type = object : TypeToken<List<Weather?>?>() {}.type
            return Gson().fromJson(value, listType)
        }

        @TypeConverter
        @JvmStatic
        fun fromList(list: List<Weather?>?): String {
            val gson = Gson()
            return gson.toJson(list)
        }
    }
}