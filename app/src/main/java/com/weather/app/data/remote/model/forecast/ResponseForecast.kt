package com.weather.app.data.remote.model.forecast


import com.google.gson.annotations.SerializedName

data class ResponseForecast(
    @SerializedName("city")
    val city: City? = null,
    @SerializedName("cod")
    val cod: String? = null,
    @SerializedName("list")
    val list: List<Forecast>? = null,
    @SerializedName("message")
    val message: Double? = null,
)