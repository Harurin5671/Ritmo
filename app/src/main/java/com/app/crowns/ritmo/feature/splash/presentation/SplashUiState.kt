package com.app.crowns.ritmo.feature.splash.presentation

sealed interface SplashUiState {
    data object Loading: SplashUiState
    data object NavigateToOnboarding: SplashUiState
    data object NavigateToDashboard: SplashUiState
}