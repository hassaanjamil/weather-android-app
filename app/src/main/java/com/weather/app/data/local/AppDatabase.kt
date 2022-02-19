package com.weather.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.weather.app.data.local.dao.CityDao
import com.weather.app.data.remote.model.cities.Data

@Database(entities = [Data::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao
}