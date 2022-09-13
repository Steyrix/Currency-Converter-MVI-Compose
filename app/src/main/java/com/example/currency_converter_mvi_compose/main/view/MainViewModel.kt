package com.example.currency_converter_mvi_compose.main.view

import com.example.currency_converter_mvi_compose.view.BaseViewModel

class MainViewModel : BaseViewModel<
        MainScreenContract.State,
        MainScreenContract.Event,
        MainScreenContract.Effect>() {

    override fun setInitialState(): MainScreenContract.State {
        TODO("Not yet implemented")
    }

    override fun onEventReceived(event: MainScreenContract.Event) {
        TODO("Not yet implemented")
    }
}