package com.example.currency_converter_mvi_compose.domain

import com.example.currency_converter_mvi_compose.main.domain.roundTo
import junit.framework.Assert.assertEquals
import org.junit.Test

class DoubleExtTest {

    @Test
    fun `should round correctly`() {
        val values = listOf(
            1.5065,
            1.5064,
            1.0
        )

        val result = mutableListOf<Double>()
        val expectedResult = mutableListOf(
            1.507, 1.506, 1.0
        )

        values.forEach {
            result.add(it.roundTo(3))
        }

        assertEquals(result, expectedResult)
    }
}