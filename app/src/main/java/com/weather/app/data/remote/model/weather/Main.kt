package com.weather.app.data.remote.model.weather


import com.google.gson.annotations.SerializedName

data class Main(
    @SerializedName("feels_like")
    val feelsLike: Double?,
    @SerializedName("grnd_level")
    val grndLevel: Int?,
    @SerializedName("humidity")
    val humidity: Int?,
    @SerializedName("pressure")
    val pressure: Int?,
    @SerializedName("sea_level")
    val seaLevel: Int?,
    @SerializedName("temp")
    val temp: Double?,
    @SerializedName("temp_max")
    val temp_max: Double?,
    @SerializedName("temp_min")
    val temp_min: Double?,
)