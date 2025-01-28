package com.android.template.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListIntConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromListInt(list: List<Int>?): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toListInt(data: String?): List<Int> {
        val type = object : TypeToken<List<Int>>() {}.type
        return gson.fromJson(data, type) ?: emptyList()
    }
}