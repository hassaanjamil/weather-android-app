package com.weather.app.data.remote.model.forecast


import com.google.gson.annotations.SerializedName

data class Temp(
    @SerializedName("average")
    val average: Double?,
    @SerializedName("average_max")
    val average_max: Double?,
    @SerializedName("average_min")
    val average_min: Double?,
    @SerializedName("record_max")
    val recordMax: Int?,
    @SerializedName("record_min")
    val recordMin: Double?,
)