package com.example.currency_converter_mvi_compose.main.view

import androidx.lifecycle.viewModelScope
import com.example.currency_converter_mvi_compose.main.data.repository.CurrencyConverterRepository
import com.example.currency_converter_mvi_compose.main.data.model.Currency
import com.example.currency_converter_mvi_compose.main.domain.ComputeAmountsUseCase
import com.example.currency_converter_mvi_compose.main.domain.ComputeCurrencyRatesUseCase
import com.example.currency_converter_mvi_compose.core.view.BaseViewModel
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
            isRefreshing = true,
            isError = false
        )
    }

    override fun onEventReceived(event: MainScreenContract.Event) {
        when (event) {
            is MainScreenContract.Event.AmountChanging -> {
                setNewAmount(event.newAmount)
            }
            is MainScreenContract.Event.CurrencySelection -> {
                setNewCurrency(event.newCurrency)
            }
            is MainScreenContract.Event.Refreshing -> {
                setState { copy(isRefreshing = true) }
                getAvailableCurrencies()
            }
        }
    }

    private fun getAvailableCurrencies() {
        viewModelScope.launch {
            repository.getAvailableCurrencies()
                .onSuccess {
                    setState {
                        copy(
                            currencies = it
                        )
                    }
                    getCurrencyRates()
                }
                .onFailure {
                    setState { copy(isError = true, isRefreshing = false) }
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
                    setState { copy(isError = true, isRefreshing = false) }
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
                    setState { copy(isError = true, isRefreshing = false) }
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
                    setState { copy(amounts = it, isRefreshing = false) }
                }
                .onFailure {
                    setState { copy(isError = true, isRefreshing = false) }
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