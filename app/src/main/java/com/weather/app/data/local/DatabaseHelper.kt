package com.weather.app.data.local

import com.weather.app.data.remote.model.cities.Data

interface DatabaseHelper {

    suspend fun getCities(): List<Data>

    suspend fun insert(city: Data)

    suspend fun delete(city: Data)

}