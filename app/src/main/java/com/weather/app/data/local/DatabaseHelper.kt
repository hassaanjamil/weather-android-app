package com.weather.app.data.local

import com.weather.app.data.local.entity.Data

interface DatabaseHelper {

    suspend fun getCities(): List<Data>

    suspend fun insert(city: Data)

    suspend fun delete(city: Data)

}