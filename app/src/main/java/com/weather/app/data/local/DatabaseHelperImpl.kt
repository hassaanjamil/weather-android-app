package com.weather.app.data.local

import com.weather.app.data.local.entity.Data

class DatabaseHelperImpl(private val appDatabase: AppDatabase) : DatabaseHelper {
    override suspend fun getCities(): List<Data> = appDatabase.cityDao().getAll()
    override suspend fun insert(city: Data) = appDatabase.cityDao().insert(city)
    override suspend fun delete(city: Data) = appDatabase.cityDao().delete(city)
}