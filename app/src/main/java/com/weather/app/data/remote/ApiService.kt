package com.weather.app.data.remote

import com.weather.app.BuildConfig
import com.weather.app.data.remote.model.cities.ResponseCities
import com.weather.app.data.remote.model.forecast.ResponseForecast
import com.weather.app.data.remote.model.weather.ResponseWeather
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {
    @Headers(value = [
        "X-RapidAPI-Host: community-open-weather-map.p.rapidapi.com",
        "X-RapidAPI-Key: ${BuildConfig.API_KEY}"
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
        "X-RapidAPI-Key: ${BuildConfig.API_KEY}"
    ])
    @GET("climate/month")
    suspend fun getMonthlyForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("q") query: String,
        @Query("units") units: String = "metric",
        @Query("mode") mode: String = "json",
    ): ResponseForecast

    @Headers(value = [
        "X-RapidAPI-Host: wft-geo-db.p.rapidapi.com",
        "X-RapidAPI-Key: ${BuildConfig.API_KEY}"
    ])
    @GET("${BuildConfig.BASE_URL_CITIES}v1/geo/cities")
    suspend fun getCities(
        @Query("namePrefix") prefix: String,
        @Query("sort") sort: String = "name",
        @Query("namePrefixDefaultLangResults") query: Boolean = true,
        @Query("limit") units: Int = 10,
    ): ResponseCities
}