package com.android.template.utils.helpers

import android.content.Context
import com.google.gson.Gson
import java.lang.reflect.Type

class JsonParseHelper {
    companion object {

        fun <T> parseJsonFromFile(context: Context, gson: Gson, fileName: String, clazz: Class<T>): T {
            return gson.fromJson<T>(getJsonFromAssetFile(fileName, context), clazz)
        }

        fun <T> parseJsonFromFile(context: Context, gson: Gson, fileName: String, type: Type): T {
            return gson.fromJson<T>(getJsonFromAssetFile(fileName, context), type)
        }


        fun <T> parseJsonFromString(gson: Gson, jsonString: String, type: Type): T {
            return gson.fromJson(jsonString, type)
        }

        private fun getJsonFromAssetFile(fileName: String, context: Context): String {
            return context.assets!!.open(fileName).bufferedReader().use { it.readText() }
        }
    }
}