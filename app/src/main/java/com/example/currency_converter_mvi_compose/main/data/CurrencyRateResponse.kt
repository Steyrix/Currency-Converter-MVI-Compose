package com.example.currency_converter_mvi_compose.main.data

data class CurrencyRate(
    val name: String,
    val rate: Double
)

data class CurrencyRateResponse(
    val base: String,
    val rates: List<CurrencyRate>
)
