package com.example.currency_converter_mvi_compose.domain

import com.example.currency_converter_mvi_compose.TestData
import com.example.currency_converter_mvi_compose.main.data.model.Amount
import com.example.currency_converter_mvi_compose.main.data.model.CurrencyRate
import com.example.currency_converter_mvi_compose.main.domain.ComputeAmountsUseCase
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ComputeAmountsUseCaseTest {

    private val useCase = ComputeAmountsUseCase()

    @Test
    fun `should return failure when empty list is supplied`() {
        var result: Result<List<Amount>>?

        runBlocking {
            result = useCase.getAmounts(
                baseCurrencyAmount = 1.0,
                currencyRates = emptyList()
            )
        }

        assert(result!!.isFailure)
    }

    @Test
    fun `should compute and round amounts correctly`() {
        var result: Result<List<Amount>>?

        runBlocking {
            result = useCase.getAmounts(
                baseCurrencyAmount = 10.0,
                currencyRates = TestData.currencyRateList
            )
        }

        val expectedResult = Result.success(
            listOf(
                Amount(CurrencyRate("USD", 1.0), 10.0),
                Amount(CurrencyRate("JPY", 0.007), 1428.571),
                Amount(CurrencyRate("EUR", 1.0), 10.0)
            )
        )

        assertEquals(result, expectedResult)
    }

}