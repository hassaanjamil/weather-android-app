package com.weather.app.di.module

import com.weather.app.BuildConfig
import com.weather.app.data.remote.ApiHelper
import com.weather.app.data.remote.ApiHelperImpl
import com.weather.app.data.remote.ApiService
import com.weather.app.data.repository.MainRepository
import com.weather.app.ui.home.HomeAdapter
import com.weather.app.ui.home.HomeViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
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