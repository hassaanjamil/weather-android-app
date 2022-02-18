package com.weather.app.data.repository

import com.weather.app.data.remote.ApiHelper
import com.weather.app.data.remote.model.weather.ResponseWeather
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiHelper: ApiHelper,
) {
    /*suspend fun getMostPopularArticles(): ResponseArticles {
        return apiHelper.getMostPopularArticles()
    }*/

    suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        query: String,
    ): ResponseWeather {
        return apiHelper.getCurrentWeather(lat, lon, query)
    }
}