package com.example.currency_converter_mvi_compose.main.di

import com.example.currency_converter_mvi_compose.main.data.CurrencyRateService
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
object MainNetworkingModule {

    @Singleton
    @Provides
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
        return OkHttpClient().apply {
            interceptors().add(
                Interceptor { chain ->
                    var request = chain.request()
                    val url = request.url()
                        .newBuilder()
                        .addQueryParameter("app_id", "1eb269224d074b83924241a2e277ccf7")
                        .build()

                    request = request
                        .newBuilder()
                        .url(url)
                        .build()

                    chain.proceed(request)
                }
            )
        }
    }
}