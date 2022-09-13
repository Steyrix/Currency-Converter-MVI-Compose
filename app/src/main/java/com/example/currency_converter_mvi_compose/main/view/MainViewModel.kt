package com.example.currency_converter_mvi_compose.main.view

import androidx.lifecycle.viewModelScope
import com.example.currency_converter_mvi_compose.main.data.CurrencyConverterRepository
import com.example.currency_converter_mvi_compose.main.domain.ComputeCurrencyRatesUseCase
import com.example.currency_converter_mvi_compose.view.BaseViewModel
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: CurrencyConverterRepository,
    private val currencyRatesComputer: ComputeCurrencyRatesUseCase
) : BaseViewModel<
        MainScreenContract.State,
        MainScreenContract.Event,
        MainScreenContract.Effect>() {

    init {
        getAvailableCurrencies()
        getCurrencyRates()
    }

    override fun setInitialState(): MainScreenContract.State {
        return MainScreenContract.State(
            currentCurrency = repository.getDefaultCurrency(),
            currencies = emptyList(),
            rates = emptyList(),
            isLoading = true,
            isError = false
        )
    }

    override fun onEventReceived(event: MainScreenContract.Event) {
        when(event) {
            is MainScreenContract.Event.AmountChanging -> {

            }
            is MainScreenContract.Event.CurrencySelection -> {

            }
        }
    }

    private fun getAvailableCurrencies() {
        viewModelScope.launch {
            repository.getAvailableCurrencies()
                .onSuccess {
                    setState {
                        copy(
                            currentCurrency = currencies.first(),
                            currencies = it,
                            isLoading = false
                        )
                    }
                }
                .onFailure {
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
                    setState { copy(isError = true) }
                }
        }
    }

    private fun getRatesForCurrency() {
        viewModelScope.launch {
            currencyRatesComputer.getRatesForCurrency(getState().value.currentCurrency)
                .onSuccess {
                    setState { copy(rates = it) }
                }
                .onFailure {
                    setState { copy(isError = true) }
                }
        }
    }
}