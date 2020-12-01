package com.koshake1.notes.viewmodel

sealed class SplashViewState {
    class Error(val error: Throwable) : SplashViewState()
    object Auth : SplashViewState()
}