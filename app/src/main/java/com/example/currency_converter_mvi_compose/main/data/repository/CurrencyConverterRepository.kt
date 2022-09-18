package com.example.currency_converter_mvi_compose.main.data.repository

import com.example.currency_converter_mvi_compose.main.data.CurrencyRateService
import com.example.currency_converter_mvi_compose.main.data.model.Currency
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Result
import kotlin.collections.HashMap

@Singleton
class CurrencyConverterRepository
@Inject constructor(
    private val service: CurrencyRateService
) : SafeApiCallRepository {

    companion object {
        private val exception = IOException("Network request failed")
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

        return Result.failure(exception)
    }

    suspend fun getAllCurrencyRates(): Result<Unit> {
        apiCall { service.getCurrencyRates() }
            .onSuccess {
                dollarValueMap.clear()
                dollarValueMap.putAll(it.rates)
                return Result.success(Unit)
            }

        return Result.failure(exception)
    }
}