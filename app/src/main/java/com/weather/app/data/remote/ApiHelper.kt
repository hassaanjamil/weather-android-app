package com.weather.app.data.remote

import com.weather.app.data.remote.model.forecast.ResponseForecast
import com.weather.app.data.remote.model.weather.ResponseWeather


interface ApiHelper {
    suspend fun getCurrentWeather(lat: Double, lon: Double, query: String): ResponseWeather
    suspend fun getMonthlyForecast(lat: Double, lon: Double, query: String): ResponseForecast
}