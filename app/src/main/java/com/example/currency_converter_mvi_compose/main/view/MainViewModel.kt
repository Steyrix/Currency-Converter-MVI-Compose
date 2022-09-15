package com.example.currency_converter_mvi_compose.main.view

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.currency_converter_mvi_compose.main.data.repository.CurrencyConverterRepository
import com.example.currency_converter_mvi_compose.main.data.model.Currency
import com.example.currency_converter_mvi_compose.main.domain.ComputeAmountsUseCase
import com.example.currency_converter_mvi_compose.main.domain.ComputeCurrencyRatesUseCase
import com.example.currency_converter_mvi_compose.view.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel
@Inject constructor(
    private val repository: CurrencyConverterRepository,
    private val currencyRatesComputer: ComputeCurrencyRatesUseCase,
    private val amountComputer: ComputeAmountsUseCase
) : BaseViewModel<
        MainScreenContract.State,
        MainScreenContract.Event,
        MainScreenContract.Effect>() {

    init {
        getAvailableCurrencies()
    }

    override fun setInitialState(): MainScreenContract.State {
        return MainScreenContract.State(
            currentCurrency = CurrencyConverterRepository.DEFAULT_CURRENCY,
            currencies = emptyList(),
            rates = emptyList(),
            isLoading = true,
            isError = false
        )
    }

    override fun onEventReceived(event: MainScreenContract.Event) {
        when (event) {
            is MainScreenContract.Event.AmountChanging -> {
                getAmounts()
            }
            is MainScreenContract.Event.CurrencySelection -> {
                setNewCurrency(event.newCurrency)
            }
        }
    }

    private fun getAvailableCurrencies() {
        viewModelScope.launch {
            repository.getAvailableCurrencies()
                .onSuccess {
                    setState {
                        copy(
                            currencies = it,
                            isLoading = false
                        )
                    }
                    getCurrencyRates()
                }
                .onFailure {
                    Log.d("GGG", "getAvailableCurrencies f")
                    setState { copy(isError = true, isLoading = false) }
                }
        }
    }

    private fun getCurrencyRates() {
        viewModelScope.launch {
            repository.getAllCurrencyRates()
                .onSuccess {
                    getRatesForCurrency()
                }
                .onFailure {
                    Log.d("GGG", "getCurrencyRates f")
                    setState { copy(isError = true) }
                }
        }
    }

    private fun getRatesForCurrency() {
        viewModelScope.launch {
            currencyRatesComputer.getRatesForCurrency(getState().value.currentCurrency)
                .onSuccess {
                    setState { copy(rates = it) }
                    getAmounts()
                }
                .onFailure {
                    Log.d("GGG", "getRatesForCurrency f")
                    setState { copy(isError = true) }
                }
        }
    }

    private fun getAmounts() {
        viewModelScope.launch {
            amountComputer.getAmounts(
                baseCurrencyAmount = getState().value.baseAmount,
                currencyRates = getState().value.rates
            )
                .onSuccess {
                    setState { copy(amounts = it) }
                }
                .onFailure {
                    setState { copy(isError = true) }
                }
        }
    }

    private fun setNewCurrency(currency: Currency) {
        setState {
            copy(currentCurrency = currency)
        }
        getCurrencyRates()
    }

    private fun setNewAmount(amount: Double) {
        setState {
            copy(baseAmount = amount)
        }
        getAmounts()
    }
}