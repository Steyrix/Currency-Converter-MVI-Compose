package com.example.currency_converter_mvi_compose

import com.example.currency_converter_mvi_compose.main.data.model.Amount
import com.example.currency_converter_mvi_compose.main.data.model.Currency
import com.example.currency_converter_mvi_compose.main.data.model.CurrencyRate
import com.example.currency_converter_mvi_compose.main.data.response.CurrencyRatesResponse

object TestData {
    val currencyRatesResponse =
        CurrencyRatesResponse(
            base = "USD",
            rates = mapOf(
                "USD" to 1.0,
                "JPY" to 0.007,
                "EUR" to 1.0
            )
        )

    val currenciesList = listOf(
        Currency("USD", "United States Dollar"),
        Currency("JPY", "Japanese Yen"),
        Currency("EUR", "Euro")
    )

    val currencyRateList = listOf(
        CurrencyRate("USD", 1.0),
        CurrencyRate("JPY", 0.007),
        CurrencyRate("EUR", 1.0)
    )

    val currencyRatesToJPY = listOf(
        CurrencyRate("USD", 0.007),
        CurrencyRate("EUR", 0.007)
    )

    val currencyJPY = Currency("JPY", "Japanese Yen")

    val amountsToOneJPY = listOf(
        Amount(CurrencyRate("USD", 0.007), 0.007),
        Amount(CurrencyRate("EUR", 0.007), 0.007)
    )
}