package com.example.currency_converter_mvi_compose.main.view

import com.example.currency_converter_mvi_compose.main.data.model.Amount
import com.example.currency_converter_mvi_compose.main.data.model.Currency
import com.example.currency_converter_mvi_compose.main.data.model.CurrencyRate
import com.example.currency_converter_mvi_compose.core.view.ViewEffect
import com.example.currency_converter_mvi_compose.core.view.ViewEvent
import com.example.currency_converter_mvi_compose.core.view.ViewState

class MainScreenContract {

    data class State(
        val baseAmount: Double = 1.0,
        val currentCurrency: Currency = Currency("", ""),
        val currencies: List<Currency> = emptyList(),
        val rates: List<CurrencyRate> = emptyList(),
        val amounts: List<Amount> = emptyList(),
        val isError: Boolean = false,
        val isRefreshing: Boolean = false
    ) : ViewState

    sealed class Event : ViewEvent {
        data class AmountChanging(val newAmount: Double) : Event()

        data class CurrencySelection(val newCurrency: Currency) : Event()

        object Refreshing : Event()
    }

    sealed class Effect : ViewEffect {

        sealed class NetworkCall : Effect() {

            object Failed : NetworkCall()

            object Succeeded : NetworkCall()
        }
    }
}