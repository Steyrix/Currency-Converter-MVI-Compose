package com.example.currency_converter_mvi_compose.main.data

import com.example.currency_converter_mvi_compose.main.data.response.Currency
import com.example.currency_converter_mvi_compose.main.data.response.CurrencyRate
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Result

// todo store locally
@Singleton
class CurrencyConverterRepository
@Inject constructor(
    private val service: CurrencyRateService
) : SafeApiCallRepository {
    private val dollarValueMap = HashMap<String, Double>()
    private val availableCurrencies = mutableListOf<Currency>()

    fun getDollarValueMap() = dollarValueMap.toMap()

    fun getAvailableCurrenciesStored() = availableCurrencies.toList()

    suspend fun getAvailableCurrencies(): Result<List<Currency>> {
        val result = apiCall { service.getAvailableCurrencies().currencies }
            .onSuccess {
                availableCurrencies.clear()
                availableCurrencies.addAll(it)
            }

        return result
    }

    suspend fun getAllCurrencyRates(): Result<List<CurrencyRate>> {
        val result = apiCall { service.getCurrencyRates().rates }
            .onSuccess {
                it.forEach { currency ->
                    dollarValueMap[currency.symbol] = currency.rate
                }
            }

        return result
    }

    fun getDefaultCurrency() = Currency("JPY", "Japanese Yen")
}