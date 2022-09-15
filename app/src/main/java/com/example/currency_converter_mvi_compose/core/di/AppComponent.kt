package com.example.currency_converter_mvi_compose.core.di

import android.app.Application
import androidx.compose.foundation.ExperimentalFoundationApi
import com.example.currency_converter_mvi_compose.main.di.MainNetworkingModule
import com.example.currency_converter_mvi_compose.main.view.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [MainNetworkingModule::class])
interface AppComponent {
    @OptIn(ExperimentalFoundationApi::class)
    fun inject(activity: MainActivity)

    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun application(app: Application): Builder
    }
}