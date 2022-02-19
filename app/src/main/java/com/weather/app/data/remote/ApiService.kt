package com.weather.app.data.remote

import com.weather.app.data.remote.model.forecast.ResponseForecast
import com.weather.app.data.remote.model.weather.ResponseWeather
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {
    @Headers(value = [
        "X-RapidAPI-Host: community-open-weather-map.p.rapidapi.com",
        "X-RapidAPI-Key: c322118df6msh817a31335aa025dp14b8b2jsn017463c0fc26"
    ])
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("q") query: String,
        @Query("units") units: String = "metric",
        @Query("mode") mode: String = "json",
    ): ResponseWeather

    @Headers(value = [
        "X-RapidAPI-Host: community-open-weather-map.p.rapidapi.com",
        "X-RapidAPI-Key: c322118df6msh817a31335aa025dp14b8b2jsn017463c0fc26"
    ])
    @GET("climate/month")
    suspend fun getMonthlyForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("q") query: String,
        @Query("units") units: String = "metric",
        @Query("mode") mode: String = "json",
    ): ResponseForecast
}