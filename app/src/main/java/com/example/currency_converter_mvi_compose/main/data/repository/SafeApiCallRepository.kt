package com.example.currency_converter_mvi_compose.main.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

interface SafeApiCallRepository {
    suspend fun <T : Any> apiCall(
        call: suspend () -> Response<T>
    ): Result<T> =
        withContext(Dispatchers.IO) {
            val response = call.invoke()

            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception())
            }
        }

}