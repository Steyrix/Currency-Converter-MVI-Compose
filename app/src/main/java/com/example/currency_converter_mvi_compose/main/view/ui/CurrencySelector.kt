package com.example.currency_converter_mvi_compose.main.view.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
        modifier = Modifier
            .clickable { expanded = true }
            .border(
                2.dp,
                MaterialTheme.colors.primary,
                MaterialTheme.shapes.medium
            )
            .padding(8.dp)
    )
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        currencies.forEachIndexed { _, currency ->
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