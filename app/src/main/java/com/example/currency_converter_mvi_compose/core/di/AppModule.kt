package com.example.currency_converter_mvi_compose.core.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object AppModule {

    private const val PREFS_FILE_NAME = "CURRENCY_CONVERTER_PREFS"

    @Provides
    @Singleton
    fun provideSharedPreferences(
        context: Application
    ): SharedPreferences {
        return context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
    }
}