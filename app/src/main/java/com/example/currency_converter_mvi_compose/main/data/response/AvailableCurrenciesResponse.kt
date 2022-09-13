package com.example.currency_converter_mvi_compose.main.data.response

data class Currency(
    val symbol: String,
    val name: String
)

data class AvailableCurrenciesResponse(
    val currencies: List<Currency>
)
