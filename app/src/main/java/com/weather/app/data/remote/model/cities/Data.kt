package com.weather.app.data.remote.model.cities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city")
data class Data(
    @PrimaryKey val id: Int = 0,
    @ColumnInfo(name = "city") val city: String = "",
    @ColumnInfo(name = "country") val country: String = "",
    @ColumnInfo(name = "countryCode") val countryCode: String = "",
    @ColumnInfo(name = "latitude") val latitude: Double = 0.0,
    @ColumnInfo(name = "lon") val longitude: Double = 0.0,
    @ColumnInfo(name = "longitude") val name: String = "",
    @ColumnInfo(name = "population") val population: Int = 0,
    @ColumnInfo(name = "region") val region: String = "",
    @ColumnInfo(name = "regionCode") val regionCode: String = "",
    @ColumnInfo(name = "type") val type: String = "",
    @ColumnInfo(name = "wikiDataId") val wikiDataId: String = "",
)