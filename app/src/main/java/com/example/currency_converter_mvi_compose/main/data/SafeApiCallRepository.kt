package com.example.currency_converter_mvi_compose.main.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface SafeApiCallRepository {
    suspend fun <T> apiCall(
        call: suspend () -> T
    ): Result<T> = runCatching {
        withContext(Dispatchers.IO) {
            call.invoke()
        }
    }
}