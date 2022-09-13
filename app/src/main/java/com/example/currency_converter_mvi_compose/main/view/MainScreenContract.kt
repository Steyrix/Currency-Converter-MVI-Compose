package com.example.currency_converter_mvi_compose.main.view

import com.example.currency_converter_mvi_compose.main.data.response.Currency
import com.example.currency_converter_mvi_compose.main.data.response.CurrencyRate
import com.example.currency_converter_mvi_compose.view.ViewEffect
import com.example.currency_converter_mvi_compose.view.ViewEvent
import com.example.currency_converter_mvi_compose.view.ViewState

class MainScreenContract {

    data class State(
        val currentCurrency: Currency = Currency("", ""),
        val currencies: List<Currency> = emptyList(),
        val rates: List<CurrencyRate> = emptyList(),
        val isLoading: Boolean = false,
        val isError: Boolean = false
    ) : ViewState

    sealed class Event : ViewEvent {
        data class AmountChanging(val newAmount: Float) : Event()

        data class CurrencySelection(val newCurrency: Currency) : Event()

        object ExchangeRateUpdating : Event()
    }

    sealed class Effect : ViewEffect {
        sealed class NetworkCall : Effect() {
            object Failed : NetworkCall()

            object Succeeded : NetworkCall()
        }
    }
}