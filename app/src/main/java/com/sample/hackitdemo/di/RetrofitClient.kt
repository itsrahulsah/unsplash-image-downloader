package com.sample.hackitdemo.di

import com.sample.hackitdemo.BuildConfig
import com.sample.hackitdemo.network.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitClient {

    private const val BASE_URL = "https://api.unsplash.com"

    private val okhttpClient = OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", BuildConfig.ACCESS_KEY)
            .build()
        chain.proceed(newRequest)
    })

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit {

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okhttpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    @Provides
    fun providesTopHeadlinesApi(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }



}