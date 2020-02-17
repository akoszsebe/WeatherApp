package com.example.weatherapp.data.persistance.typeconverter

import androidx.room.TypeConverter
import com.example.weatherapp.data.model.Location
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*


class ListStringTypeConverter {
    var gson = Gson()

    @TypeConverter
    fun stringToSomeObjectList(data: String?): List<Location?>? {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType: Type =
            object : TypeToken<List<Location?>?>() {}.getType()
        return gson.fromJson<List<Location?>>(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: List<Location?>?): String? {
        return gson.toJson(someObjects)
    }
}