package com.example.currency_converter_mvi_compose.main.view.ui

import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import com.example.currency_converter_mvi_compose.main.view.MainScreenContract

@Composable
fun AmountField(
    onEventSent: (MainScreenContract.Event) -> Unit
) {
    TextField(
        value = "1.000000",
        onValueChange = { value ->
            onEventSent(MainScreenContract.Event.AmountChanging(value.toDouble()))
        }
    )
}