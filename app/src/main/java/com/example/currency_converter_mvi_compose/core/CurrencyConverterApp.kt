package com.example.currency_converter_mvi_compose.core

import android.app.Application
import com.example.currency_converter_mvi_compose.core.di.AppComponent
import com.example.currency_converter_mvi_compose.core.di.DaggerAppComponent

class CurrencyConverterApp : Application() {
    val appComponent: AppComponent = DaggerAppComponent
        .builder()
        .application(this)
        .build()
}