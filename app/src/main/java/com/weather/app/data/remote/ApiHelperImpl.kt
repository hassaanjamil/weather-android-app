package com.weather.app.data.remote

import com.weather.app.data.remote.model.weather.ResponseWeather
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService) : ApiHelper {
    /*override suspend fun getMostPopularArticles() = apiService.getMostPopularArticles(
        "all-sections", "7")*/

    override suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        query: String,
    ): ResponseWeather =
        apiService.getCurrentWeather(lat, lon, query)
}