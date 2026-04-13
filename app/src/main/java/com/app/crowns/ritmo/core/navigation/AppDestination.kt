package com.app.crowns.ritmo.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface AppDestination: NavKey {
    @Serializable
    data object Splash: AppDestination
    @Serializable
    data object Onboarding: AppDestination

    @Serializable
    data object Dashboard: AppDestination
    @Serializable
    data object CreateHabit: AppDestination
}