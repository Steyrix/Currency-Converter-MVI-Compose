package com.example.currency_converter_mvi_compose.main.view.ui

import androidx.compose.foundation.clickable
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.currency_converter_mvi_compose.main.data.model.Currency
import com.example.currency_converter_mvi_compose.main.view.MainScreenContract

@Composable
fun CurrencySelector(
    current: Currency,
    currencies: List<Currency>,
    onEventSent: (MainScreenContract.Event) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }
    var selectedCurrency by remember { mutableStateOf(current) }

    Text(
        text = selectedCurrency.name,
        modifier = Modifier.clickable { expanded = true }
    )
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        currencies.forEachIndexed { index, currency ->
            DropdownMenuItem(
                onClick = {
                    onEventSent(MainScreenContract.Event.CurrencySelection(currency))
                    selectedCurrency = currency
                    expanded = false
                }
            ) {
                Text(text = currency.symbol + "/" + currency.name)
            }
        }
    }
}