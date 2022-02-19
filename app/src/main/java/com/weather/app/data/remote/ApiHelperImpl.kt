package com.weather.app.data.remote

import com.weather.app.data.remote.model.cities.ResponseCities
import com.weather.app.data.remote.model.forecast.ResponseForecast
import com.weather.app.data.remote.model.weather.ResponseWeather
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService) : ApiHelper {
    override suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        query: String,
    ): ResponseWeather = apiService.getCurrentWeather(lat, lon, query)

    override suspend fun getMonthlyForecast(
        lat: Double,
        lon: Double,
        query: String,
    ): ResponseForecast = apiService.getMonthlyForecast(lat, lon, query)

    override suspend fun getCities(
        prefix: String,
    ): ResponseCities = apiService.getCities(prefix)
}