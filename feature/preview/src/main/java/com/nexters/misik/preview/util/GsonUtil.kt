package com.nexters.misik.preview.util

import com.google.gson.Gson

object GsonUtil {
    private val gson = Gson()

    fun <T> toJson(data: T): String {
        return gson.toJson(data)
    }

    fun <T> fromJson(json: String, clazz: Class<T>): T {
        return gson.fromJson(json, clazz)
    }
}
