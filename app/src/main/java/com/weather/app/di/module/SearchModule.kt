package com.weather.app.di.module

import com.weather.app.ui.search.SearchAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SearchModule {
    @Provides
    @Singleton
    fun provideSearchAdapter(): SearchAdapter = SearchAdapter()
}