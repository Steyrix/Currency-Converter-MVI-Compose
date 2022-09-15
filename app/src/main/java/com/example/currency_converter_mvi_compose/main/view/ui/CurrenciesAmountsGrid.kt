package com.example.currency_converter_mvi_compose.main.view.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.currency_converter_mvi_compose.main.data.model.Amount

@ExperimentalFoundationApi
@Composable
fun CurrenciesAmountsGrid(
    amounts: List<Amount>,
    isLoading: Boolean,
    isError: Boolean
) {
    LazyVerticalGrid(
        cells = GridCells.Adaptive(minSize = 120.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (isError) {
            items(1) {
                Box {
                    Text(text = "Network error")
                }
            }
        } else {
            items(amounts.size) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .border(
                                2.dp,
                                MaterialTheme.colors.primary,
                                MaterialTheme.shapes.medium
                            )
                            .background(
                                MaterialTheme.colors.secondary
                            )
                            .padding(
                                8.dp, 8.dp
                            )
                    ) {
                        Column {
                            Text(text = amounts[it].currencyRate.symbol)
                            Text(text = "${amounts[it].currencyRate.rate}")
                            Text(text = "${amounts[it].amount}")
                        }
                    }
                }
            }
        }
    }

    if (isLoading) CircularProgressIndicator()
}