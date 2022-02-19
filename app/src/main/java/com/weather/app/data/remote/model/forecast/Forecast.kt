package com.weather.app.data.remote.model.forecast


import com.google.gson.annotations.SerializedName

data class Forecast(
    @SerializedName("dt")
    val dt: Long?,
    @SerializedName("humidity")
    val humidity: Double?,
    @SerializedName("pressure")
    val pressure: Double?,
    @SerializedName("temp")
    val temp: Temp?,
    @SerializedName("wind_speed")
    val windSpeed: Double?,
)