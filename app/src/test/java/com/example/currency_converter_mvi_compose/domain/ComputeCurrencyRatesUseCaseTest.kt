package com.example.currency_converter_mvi_compose.domain

import com.example.currency_converter_mvi_compose.TestData
import com.example.currency_converter_mvi_compose.main.data.model.Currency
import com.example.currency_converter_mvi_compose.main.data.model.CurrencyRate
import com.example.currency_converter_mvi_compose.main.data.repository.CurrencyConverterRepository
import com.example.currency_converter_mvi_compose.main.domain.ComputeCurrencyRatesUseCase
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ComputeCurrencyRatesUseCaseTest {

    private val repo = mockk<CurrencyConverterRepository>()

    private val useCase = ComputeCurrencyRatesUseCase(repo)

    @Test
    fun `should fail if map is empty`() {
        every { repo.getDollarValueMap() }.returns(hashMapOf())
        every { repo.getAvailableCurrenciesStored() }.returns(
            listOf(TestData.currencyJPY)
        )

        var result: Result<List<CurrencyRate>>?

        runBlocking {
            result = useCase.getRatesForCurrency(TestData.currencyJPY)
        }

        assert(result!!.isFailure)
    }

    @Test
    fun `should fail if available currencies list is empty`() {
        every { repo.getDollarValueMap() }.returns(
            hashMapOf("USD" to 1.0)
        )
        every { repo.getAvailableCurrenciesStored() }.returns(emptyList())

        var result: Result<List<CurrencyRate>>?

        runBlocking {
            result = useCase.getRatesForCurrency(TestData.currencyJPY)
        }

        assert(result!!.isFailure)
    }

    @Test
    fun `should compute currency rates`() {
        val expectedMap = HashMap<String, Double>().apply {
            putAll(TestData.currencyRatesResponse.rates)
        }
        val expectedList = TestData.currenciesList
        val expectedResult = Result.success(TestData.currencyRatesToJPY)

        every { repo.getDollarValueMap() }.returns(expectedMap)
        every { repo.getAvailableCurrenciesStored() }.returns(expectedList)

        var result: Result<List<CurrencyRate>>?

        runBlocking {
            result = useCase.getRatesForCurrency(TestData.currencyJPY)
        }

        assertEquals(result, expectedResult)
    }
}