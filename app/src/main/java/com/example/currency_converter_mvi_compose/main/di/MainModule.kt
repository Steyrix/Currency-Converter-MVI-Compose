package com.example.currency_converter_mvi_compose.main.di

import com.example.currency_converter_mvi_compose.main.data.CurrencyRateService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://openexchangerates.org/api/"

@Module
object MainModule {

    @Provides
    fun provideCurrencyRateService(
        retrofit: Retrofit
    ): CurrencyRateService {
        return retrofit.create(CurrencyRateService::class.java)
    }

    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}