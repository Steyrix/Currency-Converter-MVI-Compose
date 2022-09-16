package com.example.currency_converter_mvi_compose.main.data.repository

import android.content.SharedPreferences
import android.util.Log
import com.example.currency_converter_mvi_compose.main.data.CurrencyRateService
import com.example.currency_converter_mvi_compose.main.data.model.Currency
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Result
import kotlin.collections.HashMap

@Singleton
class CurrencyConverterRepository
@Inject constructor(
    private val service: CurrencyRateService,
    private val sharedPreferences: SharedPreferences
) : SafeApiCallRepository {

    companion object {
        private const val DOLLAR_VALUE_MAP_KEY = "DOLLAR_VALUE_MAP"
        private const val AVAILABLE_CURRENCIES_KEY = "AVAILABLE_CURRENCIES"
        private val exception = IOException("Network request failed")
        val DEFAULT_CURRENCY = Currency("JPY", "Japanese Yen")

    }

    private val gson = Gson()

    private val dollarValueMap = HashMap<String, Double>()
    private val availableCurrencies = mutableListOf<Currency>()

    fun getDollarValueMap() = dollarValueMap.toMap()

    fun getAvailableCurrenciesStored() = availableCurrencies.toList()

    suspend fun getAvailableCurrencies(): Result<List<Currency>> {
        apiCall { service.getAvailableCurrencies() }
            .onSuccess {
                availableCurrencies.clear()
                it.forEach { entry ->
                    availableCurrencies.add(Currency(entry.key, entry.value))
                }
                saveAvailableCurrenciesLocally()

                return Result.success(availableCurrencies)
            }
            .onFailure {
                val saved = getListFromPrefs()
                if (saved.isNotEmpty()) {
                    availableCurrencies.clear()
                    availableCurrencies.addAll(saved)
                    return Result.success(saved)
                }
            }
        return Result.failure(exception)
    }

    suspend fun getAllCurrencyRates(): Result<Unit> {
        apiCall { service.getCurrencyRates() }
            .onSuccess {
                dollarValueMap.clear()
                dollarValueMap.putAll(it.rates)
                saveMapLocally()

                return Result.success(Unit)
            }
            .onFailure {
                val saved = getMapFromPrefs()
                Log.d("GGGMAP", saved.toString())

                if (saved.isNotEmpty()) {
                    dollarValueMap.clear()
                    dollarValueMap.putAll(saved)
                }

                return Result.success(Unit)
            }

        return Result.failure(exception)
    }

    private fun saveMapLocally() {
        if (dollarValueMap.isEmpty()) return
        val mapJson = gson.toJson(dollarValueMap)

        sharedPreferences
            .edit()
            .putString(DOLLAR_VALUE_MAP_KEY, mapJson)
            .apply()
    }

    private fun saveAvailableCurrenciesLocally() {
        if (availableCurrencies.isEmpty()) return
        val listJson = gson.toJson(availableCurrencies)

        sharedPreferences
            .edit()
            .putString(AVAILABLE_CURRENCIES_KEY, listJson)
            .apply()
    }

    private fun getMapFromPrefs(): HashMap<String, Double> {
        val mapJson = sharedPreferences.getString(DOLLAR_VALUE_MAP_KEY, "")
        if (mapJson.isNullOrEmpty()) return hashMapOf()

        val type = object : TypeToken<HashMap<String, Double>>() {}.type

        return gson.fromJson(mapJson, type)
    }

    private fun getListFromPrefs(): List<Currency> {
        val listJson = sharedPreferences.getString(AVAILABLE_CURRENCIES_KEY, "")
        if (listJson.isNullOrEmpty()) return emptyList()

        val type = object : TypeToken<List<Currency>>() {}.type

        return gson.fromJson(listJson, type)
    }
}