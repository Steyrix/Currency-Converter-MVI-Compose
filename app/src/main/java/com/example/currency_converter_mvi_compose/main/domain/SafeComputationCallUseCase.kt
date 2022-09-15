package com.example.currency_converter_mvi_compose.main.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface SafeComputationCallUseCase {
    suspend fun <T> computationCall(
        call: suspend () -> T
    ): Result<T> = runCatching {
        withContext(Dispatchers.Default) {
            call.invoke()
        }
    }
}