package com.weather.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.weather.app.data.local.dao.UserDao
import com.weather.app.data.local.entity.User

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

}