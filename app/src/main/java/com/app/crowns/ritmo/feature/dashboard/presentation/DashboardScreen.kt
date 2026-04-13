package com.app.crowns.ritmo.feature.dashboard.presentation

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.crowns.ritmo.feature.dashboard.presentation.components.DashboardAddHabit
import com.app.crowns.ritmo.feature.dashboard.presentation.components.DashboardBottomBar
import com.app.crowns.ritmo.feature.dashboard.presentation.components.DashboardTopBar
import com.app.crowns.ritmo.feature.dashboard.presentation.states.DashboardEmptyState
import com.app.crowns.ritmo.feature.dashboard.presentation.states.DashboardErrorState
import com.app.crowns.ritmo.feature.dashboard.presentation.states.DashboardLoadingState
import com.app.crowns.ritmo.feature.dashboard.presentation.states.DashboardSuccessState
import com.app.crowns.ritmo.feature.hydration.presentation.HydrationScreen
import com.app.crowns.ritmo.feature.progress.presentation.ProgressScreen
import com.app.crowns.ritmo.feature.settings.presentation.SettingsScreen
import com.app.crowns.ritmo.ui.theme.RitmoTheme

@Composable
fun DashboardScreen(
    onNavigateToCreateHabit: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { DashboardTopBar(userName = uiState.userName) },
        bottomBar = {
            DashboardBottomBar(
                activeTab = uiState.activeTab,
                onTabSelected = viewModel::onTabSelected
            )
        },
        floatingActionButton = {
            if (uiState.activeTab == DashboardTab.HOME) {
                DashboardAddHabit(
                    onClick = onNavigateToCreateHabit
                )
            }
        },
        modifier = Modifier
            .navigationBarsPadding()
    ) { paddingValues ->
        when (uiState.activeTab) {
            DashboardTab.HOME -> HomeTabContent(
                modifier = Modifier.padding(paddingValues),
                contentState = uiState.contentState,
                onCreateHabit = onNavigateToCreateHabit,
                onRetry = viewModel::onRetry,
                onCompleteHabit = viewModel::onCompleteHabit,
                onSnoozeHabit = viewModel::onSnoozeHabit,
                onToggleHardDay = {}
            )

            DashboardTab.PROGRESS -> ProgressScreen()
            DashboardTab.WATER -> HydrationScreen()
            DashboardTab.SETTINGS -> SettingsScreen()
        }
    }
}

@Composable
private fun HomeTabContent(
    modifier: Modifier = Modifier,
    contentState: DashboardContentState,
    onCreateHabit: () -> Unit,
    onRetry: () -> Unit,
    onCompleteHabit: (Long) -> Unit,
    onSnoozeHabit: (Long, Int) -> Unit,
    onToggleHardDay: () -> Unit
) {
    when (contentState) {
        is DashboardContentState.Loading -> DashboardLoadingState()
        is DashboardContentState.Empty -> DashboardEmptyState(onCreateHabit = onCreateHabit)
        is DashboardContentState.Error -> DashboardErrorState(
            message = contentState.message,
            onRetry = onRetry
        )

        is DashboardContentState.Success -> DashboardSuccessState(
            modifier = modifier,
            data = contentState.data,
            onCompleteHabit = onCompleteHabit,
            onSnoozeHabit = onSnoozeHabit,
            onToggleHardDay = onToggleHardDay
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DashboardScreenLoadingPreview() {
    RitmoTheme {
        Scaffold(
            topBar = { DashboardTopBar(userName = "Carlos") },
            bottomBar = {
                DashboardBottomBar(
                    activeTab = DashboardTab.HOME,
                    onTabSelected = {}
                )
            }
        ) { paddingValues ->
            DashboardLoadingState()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DashboardScreenEmptyPreview() {
    RitmoTheme {
        Scaffold(
            topBar = { DashboardTopBar(userName = "Carlos") },
            bottomBar = {
                DashboardBottomBar(
                    activeTab = DashboardTab.HOME,
                    onTabSelected = {}
                )
            }
        ) { paddingValues ->
            DashboardEmptyState(onCreateHabit = {})
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DashboardScreenErrorPreview() {
    RitmoTheme {
        Scaffold(
            topBar = { DashboardTopBar(userName = "Carlos") },
            bottomBar = {
                DashboardBottomBar(
                    activeTab = DashboardTab.HOME,
                    onTabSelected = {}
                )
            }
        ) { paddingValues ->
            DashboardErrorState(
                message = "Could not load your habits",
                onRetry = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DashboardScreenSuccessPreview() {
    RitmoTheme {
        Scaffold(
            topBar = { DashboardTopBar(userName = "Carlos") },
            bottomBar = {
                DashboardBottomBar(
                    activeTab = DashboardTab.HOME,
                    onTabSelected = {}

                )
            }
        ) { paddingValues ->
            Text(
                text = "Welcome back,",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(24.dp)
            )
        }
    }
}