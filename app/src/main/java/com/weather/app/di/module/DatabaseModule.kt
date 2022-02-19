package com.weather.app.di.module

import android.content.Context
import androidx.room.Room
import com.weather.app.data.local.AppDatabase
import com.weather.app.data.local.DatabaseBuilder
import com.weather.app.data.local.DatabaseHelper
import com.weather.app.data.local.DatabaseHelperImpl
import com.weather.app.data.local.dao.CityDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "weather-app-db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCityDao(appDatabase: AppDatabase): CityDao {
        return appDatabase.cityDao()
    }

    @Provides
    @Singleton
    fun provideDatabaseHelper(@ApplicationContext context: Context):
            DatabaseHelper = DatabaseHelperImpl(DatabaseBuilder.getInstance(context))
}
