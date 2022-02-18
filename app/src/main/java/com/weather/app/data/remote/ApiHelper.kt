package com.weather.app.data.remote

import com.weather.app.data.remote.model.ResponseArticles


interface ApiHelper {
    suspend fun getMostPopularArticles(): ResponseArticles
}