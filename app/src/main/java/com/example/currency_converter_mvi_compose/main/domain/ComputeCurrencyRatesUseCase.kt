package com.example.currency_converter_mvi_compose.main.domain

import com.example.currency_converter_mvi_compose.main.data.model.Currency
import com.example.currency_converter_mvi_compose.main.data.repository.CurrencyConverterRepository
import com.example.currency_converter_mvi_compose.main.data.model.CurrencyRate
import javax.inject.Inject

class ComputeCurrencyRatesUseCase
@Inject constructor(
    private val repository: CurrencyConverterRepository
) : SafeComputationCallUseCase {

    companion object {
        private val exception = IllegalStateException("Has not enough data to compute rates")
    }

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
        currentCurrency: Currency
    ): List<CurrencyRate> {

        val targetToUSD = dollarValueMap[currentCurrency.symbol]
            ?: throw exception
        val rates = mutableListOf<CurrencyRate>()

        availableCurrencies.forEach {
            if (it != currentCurrency) {
                val currentToUsd = dollarValueMap[it.symbol] ?: 1.0
                val rateValue = (targetToUSD / currentToUsd).roundTo(3)

                rates.add(
                    CurrencyRate(it.symbol, rateValue)
                )
            }
        }

        if (rates.isEmpty()) throw exception

        return rates
    }
}