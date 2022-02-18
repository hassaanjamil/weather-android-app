package com.weather.app.data.remote

import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService) : ApiHelper {
    override suspend fun getMostPopularArticles() = apiService.getMostPopularArticles(
        "all-sections", "7")
}