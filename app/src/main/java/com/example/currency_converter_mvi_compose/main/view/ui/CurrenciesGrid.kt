package com.example.currency_converter_mvi_compose.main.view.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.currency_converter_mvi_compose.main.data.response.CurrencyRate

@ExperimentalFoundationApi
@Composable
fun CurrenciesGrid(
    currencies: List<CurrencyRate>,
    isLoading: Boolean,
    isError: Boolean
) {
    LazyVerticalGrid(
        cells = GridCells.Adaptive(minSize = 64.dp)
    ) {
        if (isError) {
            items(1) {
                Box {
                    Text(text = "Network error")
                }
            }
        } else {
            items(currencies.size) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box {
                        Text(text = currencies[it].symbol)
                        Text(text = "${currencies[it].rate}")
                    }
                }
            }
        }
    }

    if (isLoading) CircularProgressIndicator()
}