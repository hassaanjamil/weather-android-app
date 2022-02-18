package com.weather.app.data.remote

import com.weather.app.data.remote.model.weather.ResponseWeather


interface ApiHelper {
    //suspend fun getMostPopularArticles(): ResponseArticles
    suspend fun getCurrentWeather(lat: Double, lon: Double, query: String): ResponseWeather
}