package com.app.crowns.ritmo.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.app.crowns.ritmo.feature.dashboard.presentation.DashboardScreen
import com.app.crowns.ritmo.feature.habit.presentation.create.CreateHabitScreen
import com.app.crowns.ritmo.feature.onboarding.presentation.OnboardingScreen
import com.app.crowns.ritmo.feature.splash.presentation.AppSplashScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier
) {

    val backStack = rememberAppNavBackStack(AppDestination.Splash)

    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = {
            backStack.back()
        },
        entryProvider = entryProvider {
            entry<AppDestination.Splash> {
                AppSplashScreen(
                    onNavigateToDashboard = {
                        backStack.clearAndNavigateTo(AppDestination.Dashboard)
                    },
                    onNavigateToOnboarding = {
                        backStack.clearAndNavigateTo(AppDestination.Onboarding)
                    }
                )
            }
            entry<AppDestination.Onboarding> {
                OnboardingScreen(
                    onFinished = {
                        backStack.clearAndNavigateTo(AppDestination.Dashboard)
                    }
                )
            }
            entry<AppDestination.Dashboard> {
                DashboardScreen(
                    onNavigateToCreateHabit = {
                        backStack.navigateTo(AppDestination.CreateHabit)
                    }
                )
            }
            entry<AppDestination.CreateHabit> {
                CreateHabitScreen(
                    onBack = { backStack.back() },
                    onHabitCreated = { backStack.back() }
                )
            }
        }
    )
}