package com.weather.app.data.repository

import com.weather.app.data.local.DatabaseHelper
import com.weather.app.data.remote.ApiHelper
import com.weather.app.data.remote.model.cities.Data
import com.weather.app.data.remote.model.cities.ResponseCities
import com.weather.app.data.remote.model.forecast.ResponseForecast
import com.weather.app.data.remote.model.weather.ResponseWeather
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiHelper: ApiHelper,
    private val databaseHelper: DatabaseHelper,
) {
    suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        query: String,
    ): ResponseWeather {
        return apiHelper.getCurrentWeather(lat, lon, query)
    }

    suspend fun getMonthlyForecast(
        lat: Double,
        lon: Double,
        query: String,
    ): ResponseForecast {
        return apiHelper.getMonthlyForecast(lat, lon, query)
    }

    suspend fun getCities(prefix: String): ResponseCities {
        return apiHelper.getCities(prefix)
    }


    suspend fun getCities(): List<Data> {
        return databaseHelper.getCities()
    }

    suspend fun insert(city: Data) {
        return databaseHelper.insert(city)
    }

    suspend fun delete(city: Data) {
        return databaseHelper.delete(city)
    }
}