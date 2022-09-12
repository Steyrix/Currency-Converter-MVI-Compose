package com.example.currency_converter_mvi_compose.main.view

import android.icu.util.Currency
import com.example.currency_converter_mvi_compose.view.ViewEffect
import com.example.currency_converter_mvi_compose.view.ViewEvent

class MainScreenContract {
    sealed class MainScreenEvent : ViewEvent {
        data class AmountChanging(val newAmount: Float) : MainScreenEvent()

        data class CurrencySelection(val newCurrency: Currency) : MainScreenEvent()

        object ExchangeRateUpdating : MainScreenEvent()
    }

    sealed class MainScreenEffect : ViewEffect {

        sealed class Update : MainScreenEffect() {
            object Failed : Update()

            data class Succeeded(val newRate: Float) : Update()
        }
    }
}