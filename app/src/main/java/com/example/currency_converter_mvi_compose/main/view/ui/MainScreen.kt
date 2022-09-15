package com.example.currency_converter_mvi_compose.main.view.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.currency_converter_mvi_compose.main.view.MainScreenContract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@ExperimentalFoundationApi
@Composable
fun MainScreen(
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

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .padding(16.dp, 0.dp),
        horizontalAlignment = Alignment.End
    ) {
        AmountField(
            onEventSent = { event -> onEventSent(event) }
        )

        CurrencySelector(
            current = state.currentCurrency,
            currencies = state.currencies,
            onEventSent = onEventSent
        )

        CurrenciesAmountsGrid(
            amounts = state.amounts,
            isLoading = state.isLoading,
            isError = state.isError
        )
    }
}