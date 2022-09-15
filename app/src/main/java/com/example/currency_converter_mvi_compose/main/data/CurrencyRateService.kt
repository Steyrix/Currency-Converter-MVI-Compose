package com.example.currency_converter_mvi_compose.main.data

import com.example.currency_converter_mvi_compose.main.data.response.CurrencyRatesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyRateService {

    @GET("latest.json")
    suspend fun getCurrencyRates(
        @Query("base") base: String = "USD"
    ): Response<CurrencyRatesResponse>

    @GET("currencies.json")
    suspend fun getAvailableCurrencies(): Response<Map<String, String>>
}