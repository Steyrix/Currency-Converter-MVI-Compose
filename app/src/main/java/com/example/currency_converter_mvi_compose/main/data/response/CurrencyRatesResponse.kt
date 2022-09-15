package com.example.currency_converter_mvi_compose.main.data.response

data class CurrencyRatesResponse(
    val base: String,
    val rates: Map<String, Double>
)
