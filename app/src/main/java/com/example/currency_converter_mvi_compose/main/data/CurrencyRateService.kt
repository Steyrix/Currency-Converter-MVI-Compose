package com.example.currency_converter_mvi_compose.main.data

import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyRateService {

    @GET("latest.json/")
    suspend fun getUSDCurrencyRate(
        @Query("base") base: String = "USD"
    ): CurrencyRateResponse
}