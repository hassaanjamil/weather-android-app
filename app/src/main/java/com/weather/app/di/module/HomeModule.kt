package com.weather.app.di.module

import com.weather.app.ui.home.HomeAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HomeModule {
    @Provides
    @Singleton
    fun provideHomeAdapter(): HomeAdapter = HomeAdapter()
}