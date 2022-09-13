package com.example.currency_converter_mvi_compose.main.data.response

data class CurrencyRate(
    val symbol: String,
    val rate: Double
)

data class CurrencyRatesResponse(
    val base: String,
    val rates: List<CurrencyRate>
)
