package com.example.currency_converter_mvi_compose.data.repository

import com.example.currency_converter_mvi_compose.TestData
import com.example.currency_converter_mvi_compose.main.data.CurrencyRateService
import com.example.currency_converter_mvi_compose.main.data.model.Currency
import com.example.currency_converter_mvi_compose.main.data.repository.CurrencyConverterRepository
import io.mockk.*
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Test

import retrofit2.Response

class CurrencyConverterRepositoryTest {

    private val service = mockk<CurrencyRateService>()

    private val repo = CurrencyConverterRepository(service)

    private val currenciesMap = mapOf(
        "USD" to "United States Dollar",
        "JPY" to "Japanese Yen",
        "EUR" to "Euro"
    )

    @Test
    fun `should call getAvailableCurrencies and save to list`() {
        val expectedList = TestData.currenciesList

        coEvery { service.getAvailableCurrencies() }
            .returns(Response.success(currenciesMap))

        runBlocking {
            repo.getAvailableCurrencies()
        }

        coVerify(exactly = 1) {
            service.getAvailableCurrencies()
        }
        assertEquals(repo.getAvailableCurrenciesStored(), expectedList)
    }

    @Test
    fun `should clear list on success result`() {
        coEvery { service.getAvailableCurrencies() }
            .returns(Response.success(currenciesMap))

        runBlocking {
            repo.getAvailableCurrencies()
            repo.getAvailableCurrencies()
        }

        assertEquals(repo.getAvailableCurrenciesStored(), TestData.currenciesList)
    }

    @Test
    fun `should return failure on getAvailableCurrencies fail`() {
        coEvery { service.getAvailableCurrencies() }
            .returns(Response.error(500, ResponseBody.create(null, "")))

        var result: Result<List<Currency>>?

        runBlocking {
            result = repo.getAvailableCurrencies()
        }

        assert(result!!.isFailure)
    }

    @Test
    fun `should call get currency rates and save to map`() {
        coEvery { service.getCurrencyRates() }
            .returns(Response.success(TestData.currencyRatesResponse))

        runBlocking {
            repo.getAllCurrencyRates()
        }

        coVerify(exactly = 1) {
            service.getCurrencyRates(base = "USD")
        }
        assertEquals(repo.getDollarValueMap(), TestData.currencyRatesResponse.rates)
    }

    @Test
    fun `should clear map on success`() {
        coEvery { service.getCurrencyRates() }
            .returns(Response.success(TestData.currencyRatesResponse))

        runBlocking {
            repo.getAllCurrencyRates()
            repo.getAllCurrencyRates()
        }

        assertEquals(repo.getDollarValueMap(), TestData.currencyRatesResponse.rates)
    }

    @Test
    fun `should return failure on getCurrencyRates fail`() {
        coEvery { service.getCurrencyRates() }
            .returns(Response.error(500, ResponseBody.create(null, "")))

        var result: Result<Unit>?

        runBlocking {
            result = repo.getAllCurrencyRates()
        }

        assert(result!!.isFailure)
    }
}