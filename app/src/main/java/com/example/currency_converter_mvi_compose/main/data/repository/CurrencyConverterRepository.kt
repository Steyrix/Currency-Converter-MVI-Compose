package com.example.currency_converter_mvi_compose.main.data.repository

import com.example.currency_converter_mvi_compose.main.data.CurrencyRateService
import com.example.currency_converter_mvi_compose.main.data.model.Currency
import com.example.currency_converter_mvi_compose.main.data.response.CurrencyRatesResponse
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Result

// todo store locally
@Singleton
class CurrencyConverterRepository
@Inject constructor(
    private val service: CurrencyRateService
) : SafeApiCallRepository {

    companion object {
        val DEFAULT_CURRENCY = Currency("JPY", "Japanese Yen")
    }

    private val dollarValueMap = HashMap<String, Double>()
    private val availableCurrencies = mutableListOf<Currency>()

    fun getDollarValueMap() = dollarValueMap.toMap()

    fun getAvailableCurrenciesStored() = availableCurrencies.toList()

    suspend fun getAvailableCurrencies(): Result<List<Currency>> {
        apiCall { service.getAvailableCurrencies() }
            .onSuccess {
                availableCurrencies.clear()
                it.forEach { entry ->
                    availableCurrencies.add(Currency(entry.key, entry.value))
                }
                return Result.success(availableCurrencies)
            }

        return Result.failure(Exception())
    }

    suspend fun getAllCurrencyRates(): Result<CurrencyRatesResponse> {
        val result = apiCall { service.getCurrencyRates() }
            .onSuccess {
                dollarValueMap.clear()
                dollarValueMap.putAll(it.rates)
            }

        return result
    }
}