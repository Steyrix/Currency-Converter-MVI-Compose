package com.example.currency_converter_mvi_compose.main.view.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.currency_converter_mvi_compose.main.view.MainScreenContract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@ExperimentalFoundationApi
@Composable
private fun MainScreen(
    state: MainScreenContract.State,
    effectFlow: Flow<MainScreenContract.Effect>?,
    onEventSent: (MainScreenContract.Event) -> Unit
) {
    LaunchedEffect(true) {
        effectFlow?.onEach { effect ->
            when(effect) {
                is MainScreenContract.Effect.NetworkCall.Failed -> {}
                is MainScreenContract.Effect.NetworkCall.Succeeded -> {}
            }
        }?.collect()
    }

    AmountField(
        onEventSent = { event -> onEventSent(event) }
    )

    CurrencySelector(
        current = state.currentCurrency,
        currencies = state.currencies,
        onEventSent = onEventSent
    )

    CurrenciesGrid(
        currencies = state.rates,
        isLoading = state.isLoading,
        isError = state.isError
    )
}