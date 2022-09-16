package com.example.currency_converter_mvi_compose.main.view.ui

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.KeyboardType
import com.example.currency_converter_mvi_compose.main.view.MainScreenContract

@Composable
fun AmountField(
    onEventSent: (MainScreenContract.Event) -> Unit
) {
    var amount by remember { mutableStateOf("1.0") }

    TextField(
        value = amount,
        onValueChange = { newValue ->
            if (newValue.isNotEmpty()) {
                onEventSent(MainScreenContract.Event.AmountChanging(newValue.toDouble()))
            }
            amount = newValue
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}