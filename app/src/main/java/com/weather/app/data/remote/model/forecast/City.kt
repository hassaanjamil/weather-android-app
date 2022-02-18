package com.weather.app.data.remote.model.forecast


import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("coord")
    val coord: Coord?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
)