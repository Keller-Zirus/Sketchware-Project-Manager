package `fun`.sketchcode.sketchware.project.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

val gson = Gson()

fun Any.toJson(): String = gson.toJson(this)

fun <T> String.parseJson(): T = gson.fromJson(this, object : TypeToken<T>() {}.type)