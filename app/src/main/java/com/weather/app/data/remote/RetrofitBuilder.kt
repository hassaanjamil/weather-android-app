package com.weather.app.data.remote

import com.weather.app.BuildConfig
import com.weather.app.BuildConfig.BASE_URL_WEATHER
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitBuilder {
    private fun getRetrofit(): Retrofit {
        val httpBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            httpBuilder.addInterceptor(logging)
        }
        return Retrofit.Builder()
            .baseUrl(BASE_URL_WEATHER)
            .client(httpBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService = getRetrofit().create(ApiService::class.java)

}