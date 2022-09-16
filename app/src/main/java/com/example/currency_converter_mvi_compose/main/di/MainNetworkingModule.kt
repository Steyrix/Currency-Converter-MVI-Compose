package com.example.currency_converter_mvi_compose.main.di

import com.example.currency_converter_mvi_compose.core.network.QueryParameterInterceptor
import com.example.currency_converter_mvi_compose.main.data.CurrencyRateService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
object MainNetworkingModule {

    @Provides
    @Singleton
    fun provideCurrencyRateService(
        retrofit: Retrofit
    ): CurrencyRateService {
        return retrofit.create(CurrencyRateService::class.java)
    }

    @Provides
    fun provideRetrofit(
        client: OkHttpClient
    ): Retrofit {

        return Retrofit.Builder()
            .baseUrl("https://openexchangerates.org/api/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient()
            .newBuilder()
            .addInterceptor(QueryParameterInterceptor())
            .addInterceptor(logger)
            .build()
    }
}