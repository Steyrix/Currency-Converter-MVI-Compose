package com.example.currency_converter_mvi_compose.main.domain

import com.example.currency_converter_mvi_compose.main.data.response.Currency
import com.example.currency_converter_mvi_compose.main.data.repository.CurrencyConverterRepository
import com.example.currency_converter_mvi_compose.main.data.response.CurrencyRate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ComputeCurrencyRatesUseCase
@Inject constructor(
    private val repository: CurrencyConverterRepository
) {

    suspend fun getRatesForCurrency(currency: Currency): Result<List<CurrencyRate>> {
        val map = repository.getDollarValueMap()
        val availableCurrencies = repository.getAvailableCurrenciesStored()

        return computationCall {
            computeCurrencyRates(map, availableCurrencies, currency)
        }
    }

    private fun computeCurrencyRates(
        dollarValueMap: Map<String, Double>,
        availableCurrencies: List<Currency>,
        currency: Currency
    ): List<CurrencyRate> {
        val illegalStateException = IllegalStateException("Has not enough data")

        val targetToUSD = dollarValueMap[currency.symbol]
            ?: throw illegalStateException
        val rates = mutableListOf<CurrencyRate>()

        availableCurrencies.forEach {
            val currentToUsd = dollarValueMap[it.symbol]
                ?: throw illegalStateException
            rates.add(
                CurrencyRate(it.symbol, targetToUSD / currentToUsd)
            )
        }

        if (rates.isEmpty()) throw illegalStateException

        return rates
    }

    private suspend fun <T> computationCall(
        call: suspend () -> T
    ): Result<T> = runCatching {
        withContext(Dispatchers.Default) {
            call.invoke()
        }
    }
}