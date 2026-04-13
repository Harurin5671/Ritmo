package com.app.crowns.ritmo.feature.dashboard.presentation

import com.app.crowns.ritmo.feature.habit.domain.model.Habit

data class DashboardUiState(
    val userName: String = "",
    val activeTab: DashboardTab = DashboardTab.HOME,
    val contentState: DashboardContentState = DashboardContentState.Loading
)

enum class DashboardTab {
    HOME, PROGRESS, WATER, SETTINGS
}

sealed interface DashboardContentState {
    data object Loading: DashboardContentState
    data object Empty: DashboardContentState
    data class  Error(val message: String): DashboardContentState
    data class Success(val data: DashboardData): DashboardContentState
}

data class DashboardData(
    val morningHabits: List<Habit>,
    val afternoonHabits: List<Habit>,
    val nightHabits: List<Habit>,
    val totalHabits: Int,
    val completedHabits: Int,
    val isHardDayMode: Boolean = false
) {
    val completionPercentage: Float
        get() = if (totalHabits == 0) 0f else completedHabits.toFloat() / totalHabits
}