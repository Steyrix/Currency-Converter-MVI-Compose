package com.example.currency_converter_mvi_compose

import com.example.currency_converter_mvi_compose.main.data.model.Amount
import com.example.currency_converter_mvi_compose.main.data.model.Currency
import com.example.currency_converter_mvi_compose.main.data.model.CurrencyRate
import com.example.currency_converter_mvi_compose.main.data.repository.CurrencyConverterRepository
import com.example.currency_converter_mvi_compose.main.domain.ComputeAmountsUseCase
import com.example.currency_converter_mvi_compose.main.domain.ComputeCurrencyRatesUseCase
import com.example.currency_converter_mvi_compose.main.view.MainScreenContract
import com.example.currency_converter_mvi_compose.main.view.MainViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val rule = MainCoroutineRule()

    private val repo = mockk<CurrencyConverterRepository>()
    private val currencyRatesComputer = mockk<ComputeCurrencyRatesUseCase>()
    private val amountComputer = mockk<ComputeAmountsUseCase>()

    private val expectedCurrency = TestData.currencyJPY

    @Test
    fun `should create correct initial state and call outer methods`() {

        val expectedState = MainScreenContract.State(
            currentCurrency = expectedCurrency,
            currencies = emptyList(),
            rates = emptyList(),
            isRefreshing = true,
            isError = false
        )

        val viewModel = MainViewModel(
            repo,
            currencyRatesComputer,
            amountComputer
        )

        val actualState = viewModel.setInitialState()

        assertEquals(actualState, expectedState)
    }

    @Test
    fun `should walk over success flow correctly`() {
        setupReturnsForInit()

        val viewModel = MainViewModel(
            repo,
            currencyRatesComputer,
            amountComputer
        )

        verifyInit()

        val actualState = viewModel.getState().value

        val expectedState = MainScreenContract.State(
            baseAmount = 1.0,
            currentCurrency = TestData.currencyJPY,
            currencies = TestData.currenciesList,
            rates = TestData.currencyRatesToJPY,
            amounts = TestData.amountsToOneJPY,
            isError = false,
            isRefreshing = false
        )

        assertEquals(actualState, expectedState)
    }

    @Test
    fun `should handle getAvailableCurrencies fail`() {
        coEvery { repo.getAvailableCurrencies() }.returns(
            Result.failure(Exception())
        )

        val viewModel = MainViewModel(
            repo,
            currencyRatesComputer,
            amountComputer
        )
        viewModel.setInitialState()

        coVerify(exactly = 1) {
            repo.getAvailableCurrencies()
        }
        coVerify(exactly = 0) {
            repo.getAllCurrencyRates()
        }

        val actualState = viewModel.getState().value

        assert(actualState.isError)
        assert(!actualState.isRefreshing)
        assert(actualState.currencies.isEmpty())
    }

    @Test
    fun `should handle getCurrencyRates fail`() {
        coEvery { repo.getAvailableCurrencies() }.returns(
            Result.success(TestData.currenciesList)
        )
        coEvery { repo.getAllCurrencyRates() }.returns(
            Result.failure(Exception())
        )

        val viewModel = MainViewModel(
            repo,
            currencyRatesComputer,
            amountComputer
        )

        coVerify(exactly = 1) {
            repo.getAvailableCurrencies()
            repo.getAllCurrencyRates()
        }
        coVerify(exactly = 0) {
            currencyRatesComputer.getRatesForCurrency(any())
        }

        val actualState = viewModel.getState().value

        assert(actualState.isError)
        assert(!actualState.isRefreshing)
        assertEquals(actualState.currencies, TestData.currenciesList)
    }

    @Test
    fun `should handle getRatesForCurrency fail`() {
        coEvery { repo.getAvailableCurrencies() }.returns(
            Result.success(TestData.currenciesList)
        )
        coEvery { repo.getAllCurrencyRates() }.returns(
            Result.success(Unit)
        )
        coEvery { currencyRatesComputer.getRatesForCurrency(expectedCurrency) }.returns(
            Result.failure(Exception())
        )

        val viewModel = MainViewModel(
            repo,
            currencyRatesComputer,
            amountComputer
        )
        val initialState = viewModel.setInitialState()

        coVerify(exactly = 1) {
            repo.getAvailableCurrencies()
            repo.getAllCurrencyRates()
            currencyRatesComputer.getRatesForCurrency(initialState.currentCurrency)
        }
        coVerify(exactly = 0) {
            amountComputer.getAmounts(any(), any())
        }

        val actualState = viewModel.getState().value

        assert(actualState.isError)
        assert(!actualState.isRefreshing)
        assertEquals(actualState.currencies, TestData.currenciesList)
        assert(actualState.rates.isEmpty())
    }

    @Test
    fun `should handle getAmounts fail`() {
        coEvery { repo.getAvailableCurrencies() }.returns(
            Result.success(TestData.currenciesList)
        )
        coEvery { repo.getAllCurrencyRates() }.returns(
            Result.success(Unit)
        )
        coEvery { currencyRatesComputer.getRatesForCurrency(expectedCurrency) }.returns(
            Result.success(TestData.currencyRatesToJPY)
        )
        coEvery { amountComputer.getAmounts(1.0, TestData.currencyRatesToJPY) }.returns(
            Result.failure(Exception())
        )

        val viewModel = MainViewModel(
            repo,
            currencyRatesComputer,
            amountComputer
        )
        val initialState = viewModel.setInitialState()

        coVerify(exactly = 1) {
            repo.getAvailableCurrencies()
            repo.getAllCurrencyRates()
            currencyRatesComputer.getRatesForCurrency(initialState.currentCurrency)
            amountComputer.getAmounts(initialState.baseAmount, TestData.currencyRatesToJPY)
        }

        val actualState = viewModel.getState().value

        assert(actualState.isError)
        assert(!actualState.isRefreshing)
        assertEquals(actualState.currencies, TestData.currenciesList)
        assertEquals(actualState.rates, TestData.currencyRatesToJPY)
        assert(actualState.amounts.isEmpty())
    }

    @Test
    fun `should handle currency selection event`() {
        val expectedNewRates = listOf(
            CurrencyRate("EUR", 1.0),
            CurrencyRate("JPY", 142.857)
        )
        val expectedNewAmounts = listOf(
            Amount(CurrencyRate("EUR", 1.0), 1.0),
            Amount(CurrencyRate("JPY", 142.857), 142.857)
        )
        val expectedNewCurrency = Currency("USD", "United States Dollar")

        setupReturnsForInit()

        coEvery { currencyRatesComputer.getRatesForCurrency(expectedNewCurrency) }.returns(
            Result.success(expectedNewRates)
        )
        coEvery { amountComputer.getAmounts(1.0, expectedNewRates) }.returns(
            Result.success(expectedNewAmounts)
        )

        val viewModel = MainViewModel(
            repo,
            currencyRatesComputer,
            amountComputer
        )
        val initialState = viewModel.setInitialState()

        viewModel.onEventReceived(MainScreenContract.Event.CurrencySelection(expectedNewCurrency))

        coVerify(exactly = 2) {
            repo.getAllCurrencyRates()
        }
        coVerify(exactly = 1) {
            currencyRatesComputer.getRatesForCurrency(expectedNewCurrency)
            amountComputer.getAmounts(initialState.baseAmount, expectedNewRates)
        }

        val actualState = viewModel.getState().value
        assert(!actualState.isError)
        assert(!actualState.isRefreshing)
        assertEquals(actualState.currentCurrency, expectedNewCurrency)
        assertEquals(actualState.rates, expectedNewRates)
        assertEquals(actualState.amounts, expectedNewAmounts)
    }

    @Test
    fun `should handle amount changing event`() {
        val expectedNewAmount = 10.0
        val expectedNewAmounts = listOf(
            Amount(CurrencyRate("USD", 0.007), 0.07),
            Amount(CurrencyRate("EUR", 0.007), 0.07)
        )

        setupReturnsForInit()

        val viewModel = MainViewModel(
            repo,
            currencyRatesComputer,
            amountComputer
        )

        coEvery { amountComputer.getAmounts(expectedNewAmount, TestData.currencyRatesToJPY) }.returns(
            Result.success(expectedNewAmounts)
        )

        viewModel.onEventReceived(MainScreenContract.Event.AmountChanging(expectedNewAmount))

        coVerify(exactly = 1) {
            amountComputer.getAmounts(expectedNewAmount, TestData.currencyRatesToJPY)
        }

        val actualState = viewModel.getState().value
        assert(!actualState.isError)
        assert(!actualState.isRefreshing)
        assertEquals(actualState.currentCurrency, TestData.currencyJPY)
        assertEquals(actualState.rates, TestData.currencyRatesToJPY)
        assertEquals(actualState.amounts, expectedNewAmounts)
    }

    @Test
    fun `should handle refreshing event`() {
        setupReturnsForInit()

        val viewModel = MainViewModel(
            repo,
            currencyRatesComputer,
            amountComputer
        )

        viewModel.onEventReceived(MainScreenContract.Event.Refreshing)

        verifyInit(exactly = 2)

        val actualState = viewModel.getState().value

        val expectedState = MainScreenContract.State(
            baseAmount = 1.0,
            currentCurrency = TestData.currencyJPY,
            currencies = TestData.currenciesList,
            rates = TestData.currencyRatesToJPY,
            amounts = TestData.amountsToOneJPY,
            isError = false,
            isRefreshing = false
        )

        assertEquals(actualState, expectedState)
    }

    private fun setupReturnsForInit() {
        coEvery { repo.getAvailableCurrencies() }.returns(
            Result.success(TestData.currenciesList)
        )
        coEvery { repo.getAllCurrencyRates() }.returns(
            Result.success(Unit)
        )
        coEvery { currencyRatesComputer.getRatesForCurrency(expectedCurrency) }.returns(
            Result.success(TestData.currencyRatesToJPY)
        )
        coEvery { amountComputer.getAmounts(1.0, TestData.currencyRatesToJPY) }.returns(
            Result.success(TestData.amountsToOneJPY)
        )
    }

    private fun verifyInit(exactly: Int = 1) {
        coVerify(exactly = exactly) {
            repo.getAvailableCurrencies()
            repo.getAllCurrencyRates()
            currencyRatesComputer.getRatesForCurrency(TestData.currencyJPY)
            amountComputer.getAmounts(1.0, TestData.currencyRatesToJPY)
        }
    }
}