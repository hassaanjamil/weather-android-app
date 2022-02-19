package com.weather.app.utils

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor

fun String.toDateFormat(inFormat: String, outFormat: String): String {
    val simpleDateFormat = SimpleDateFormat(inFormat, Locale.getDefault())
    return SimpleDateFormat(outFormat, Locale.getDefault()).format(simpleDateFormat.parse(this)!!)
}

fun Date.toDateFormat(inFormat: String): String {
    val simpleDateFormat = SimpleDateFormat(inFormat)
    return simpleDateFormat.format(this)
}

fun Date.toTimeFormat(inFormat: String): String {
    val simpleDateFormat = SimpleDateFormat(inFormat)
    return simpleDateFormat.format(this)
}

fun Double.formatBearing(): String {
    var bearing = this
    if (bearing < 0 && bearing > -180) {
        // Normalize to [0,360]
        bearing += 360.0
    }
    if (bearing > 360 || bearing < -180) {
        return "Unknown"
    }
    val directions = arrayOf(
        "N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE",
        "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW",
        "N")
    val cardinal = directions[floor((bearing + 11.25) % 360 / 22.5)
        .toInt()]
    return "$cardinal (${bearing}º)"
}

fun Long.convertUnixTimeMillisToDate(inFormat: String): String {
    val sdf = SimpleDateFormat(inFormat, Locale.getDefault())
    sdf.timeZone = TimeZone.getDefault()
    val time: Long = this * 1000L
    return sdf.format(Date(time))
}