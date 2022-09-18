package com.example.currency_converter_mvi_compose.core.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
object AppModule {

    @Provides
    fun provideContext(
        application: Application
    ): Context {
        return application.applicationContext
    }
}