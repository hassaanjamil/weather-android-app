package com.weather.app.utils

import java.text.SimpleDateFormat
import java.util.*

fun String.toDateFormat(inFormat: String, outFormat: String): String {
    val simpleDateFormat = SimpleDateFormat(inFormat, Locale.getDefault())
    return SimpleDateFormat(outFormat, Locale.getDefault()).format(simpleDateFormat.parse(this)!!)
}