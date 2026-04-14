package com.app.crowns.ritmo.feature.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.crowns.ritmo.core.datastore.UserPreferencesDataStore
import com.app.crowns.ritmo.core.logging.AppLogger
import com.app.crowns.ritmo.core.notification.AlarmScheduler
import com.app.crowns.ritmo.feature.habit.domain.model.DayMoment
import com.app.crowns.ritmo.feature.habit.domain.model.Habit
import com.app.crowns.ritmo.feature.habit.domain.model.HabitStatus
import com.app.crowns.ritmo.feature.habit.domain.usecase.CompleteHabitUseCase
import com.app.crowns.ritmo.feature.habit.domain.usecase.GetTodayHabitsUseCase
import com.app.crowns.ritmo.feature.habit.domain.usecase.SnoozeHabitUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.filter

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getTodayHabitsUseCase: GetTodayHabitsUseCase,
    private val completeHabitUseCase: CompleteHabitUseCase,
    private val snoozeHabitUseCase: SnoozeHabitUseCase,
    private val alarmScheduler: AlarmScheduler,
    private val dataStore: UserPreferencesDataStore,
    private val logger: AppLogger
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadDashboard()
    }

    private fun loadDashboard() {
        viewModelScope.launch {
            combine(
                getTodayHabitsUseCase(),
                dataStore.userName
            ) { habits, userName ->
                logger.d("DashboardViewModel → habits=${habits.size} userName=$userName, $habits")
                val contentState = if (habits.isEmpty()) {
                    DashboardContentState.Empty
                } else {
                    DashboardContentState.Success(
                        data = buildDashboardData(habits)
                    )
                }
                DashboardUiState(
                    userName = userName,
                    contentState = contentState
                )
            }
                .catch { e ->
                    logger.e(
                        throwable = e,
                        message = "DashboardViewModel → error loading dashboard"
                    )
                    _uiState.update { it.copy(contentState = DashboardContentState.Error("Could not load your habits")) }
                }
                .collect { state ->
                    _uiState.value = state
                }
        }
    }

    private fun buildDashboardData(habits: List<Habit>): DashboardData {
        val morning = habits.filter { it.dayMoment == DayMoment.MORNING }
        val afternoon = habits.filter { it.dayMoment == DayMoment.AFTERNOON }
        val night = habits.filter { it.dayMoment == DayMoment.NIGHT }
        val completed = habits.count { it.status == HabitStatus.COMPLETED }

        return DashboardData(
            morningHabits = morning,
            afternoonHabits = afternoon,
            nightHabits = night,
            totalHabits = habits.size,
            completedHabits = completed
        )
    }

    fun onTabSelected(tab: DashboardTab) {
        logger.d("DashboardViewModel → tab selected: $tab")
        _uiState.update { it.copy(activeTab = tab) }
    }

    fun onCompleteHabit(habitId: Long) {
        viewModelScope.launch {
            try {
                completeHabitUseCase(habitId)
                logger.i("DashboardViewModel → habit completed id=$habitId")
            } catch (e: Exception) {
                logger.e(
                    throwable = e,
                    message = "DashboardViewModel → error completing habit id=$habitId"
                )
            }
        }
    }

    fun onSnoozeHabit(habitId: Long, snoozeMinutes: Int) {
        viewModelScope.launch {
            try {
                snoozeHabitUseCase(habitId, snoozeMinutes)
                logger.i("DashboardViewModel → habit snoozed id=$habitId minutes=$snoozeMinutes")
            } catch (e: Exception) {
                logger.e(
                    throwable = e,
                    message = "DashboardViewModel → error snoozing habit id=$habitId"
                )
            }
        }
    }

    fun onRetry() {
        logger.d("DashboardViewModel → retrying load")
        _uiState.update { it.copy(contentState = DashboardContentState.Loading) }
        loadDashboard()
    }

    fun onTestNotification() {
        logger.i("DashboardViewModel → scheduling test notification in 1 minute")
        alarmScheduler.scheduleHabitReminder(
            habitId = 999,
            habitName = "Test Habit (1 min)",
            triggerTimeMillis = System.currentTimeMillis() + 60 * 1000L
        )
    }
}