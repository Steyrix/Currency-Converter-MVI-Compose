package com.example.currency_converter_mvi_compose.main.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.currency_converter_mvi_compose.core.CurrencyConverterApp
import com.example.currency_converter_mvi_compose.main.view.ui.MainScreen
import com.example.currency_converter_mvi_compose.ui.theme.CurrencyConverterMVIComposeTheme
import javax.inject.Inject

@ExperimentalFoundationApi
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        (applicationContext as CurrencyConverterApp).appComponent.inject(this)
        setContent {
            CurrencyConverterMVIComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    MainScreen(
                        state = viewModel.getState().value,
                        effectFlow = viewModel.currentEffect,
                        onEventSent = {
                            viewModel.onEventReceived(it)
                        }
                    )
                }
            }
        }
    }
}