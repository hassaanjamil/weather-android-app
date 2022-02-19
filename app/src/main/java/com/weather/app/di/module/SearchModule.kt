package com.weather.app.di.module

import com.weather.app.ui.search.CitiesAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class SearchModule {
    @Provides
    fun provideCitiesAdapter(): CitiesAdapter = CitiesAdapter()
}