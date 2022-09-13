package com.example.currency_converter_mvi_compose.main.view

import android.icu.util.Currency
import com.example.currency_converter_mvi_compose.view.ViewEffect
import com.example.currency_converter_mvi_compose.view.ViewEvent
import com.example.currency_converter_mvi_compose.view.ViewState

class MainScreenContract {
    data class State(
        val isLoading: Boolean = false
    ) : ViewState

    sealed class Event : ViewEvent {
        data class AmountChanging(val newAmount: Float) : Event()

        data class CurrencySelection(val newCurrency: Currency) : Event()

        object ExchangeRateUpdating : Event()
    }

    sealed class Effect : ViewEffect {

        sealed class Update : Effect() {
            object Failed : Update()

            data class Succeeded(val newRate: Float) : Update()
        }
    }
}