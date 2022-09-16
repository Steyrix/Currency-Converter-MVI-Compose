package com.example.currency_converter_mvi_compose.core.network

import okhttp3.Interceptor
import okhttp3.Response

class QueryParameterInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val url = request.url()
            .newBuilder()
            .addQueryParameter("app_id", "1eb269224d074b83924241a2e277ccf7")
            .build()

        request = request
            .newBuilder()
            .url(url)
            .build()

        return chain.proceed(request)
    }
}