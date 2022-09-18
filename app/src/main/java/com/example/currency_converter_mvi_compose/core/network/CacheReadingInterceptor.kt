package com.example.currency_converter_mvi_compose.core.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import okhttp3.Interceptor
import okhttp3.Response

class CacheReadingInterceptor(
    private val context: Context
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (!isOnline()) {
            val maxStaleSeconds = 60 * 60 * 24 * 30
            request = request
                .newBuilder()
                .header(
                    "Cache-Control",
                    "public, only-if-cached, max-stale=$maxStaleSeconds"
                )
                .removeHeader("Pragma")
                .build()
        }
        return chain.proceed(request)
    }

    private fun isOnline(): Boolean {
        var isConnected = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }
}