package com.weather.app.di.module

import com.weather.app.data.repository.MainRepository
import com.weather.app.ui.home.HomeAdapter
import com.weather.app.ui.home.HomeViewModel
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

    @Provides
    @Singleton
    fun provideHomeViewModel(mainRepository: MainRepository) = HomeViewModel(mainRepository)
}