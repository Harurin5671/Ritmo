package com.app.crowns.ritmo.feature.habit.presentation.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.crowns.ritmo.core.logging.AppLogger
import com.app.crowns.ritmo.core.notification.AlarmScheduler
import com.app.crowns.ritmo.feature.habit.domain.model.DayMoment
import com.app.crowns.ritmo.feature.habit.domain.model.Habit
import com.app.crowns.ritmo.feature.habit.domain.model.HabitCategory
import com.app.crowns.ritmo.feature.habit.domain.model.HabitStatus
import com.app.crowns.ritmo.feature.habit.domain.model.HabitType
import com.app.crowns.ritmo.feature.habit.domain.usecase.CreateHabitUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

sealed interface CreateHabitEvent {
    data object HabitSaved : CreateHabitEvent
}

@HiltViewModel
class CreateHabitViewModel @Inject constructor(
    private val createHabitUseCase: CreateHabitUseCase,
    private val alarmScheduler: AlarmScheduler,
    private val logger: AppLogger
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateHabitUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<CreateHabitEvent>()
    val events = _events.asSharedFlow()

    fun onNameChanged(value: String) {
        _uiState.update { it.copy(name = value, nameError = null) }
    }

    fun onCategorySelected(category: HabitCategory) {
        logger.d("CreateHabitViewModel → category selected: $category")
        _uiState.update { it.copy(selectedCategory = category, categoryError = null) }
    }

    fun onTypeSelected(type: HabitType) {
        logger.d("CreateHabitViewModel → type selected: $type")
        _uiState.update { it.copy(selectedType = type) }
    }

    fun onDayMomentSelected(moment: DayMoment) {
        _uiState.update { it.copy(selectedDayMoment = moment) }
    }

    fun onScheduledTimeChanged(hour: Int, minute: Int) {
        _uiState.update { it.copy(scheduledHour = hour, scheduledMinute = minute) }
    }

    fun onIntervalMinutesChanged(minutes: Int) {
        _uiState.update { it.copy(intervalMinutes = minutes) }
    }

    fun onDailyGoalChanged(amount: Float, unit: String) {
        _uiState.update { it.copy(dailyGoalAmount = amount, dailyGoalUnit = unit) }
    }

    fun onAmountPerIntervalChanged(amount: Float) {
        _uiState.update { it.copy(amountPerInterval = amount) }
    }

    fun onEstimatedMinutesChanged(minutes: Int) {
        _uiState.update { it.copy(estimatedMinutes = minutes) }
    }

    fun onActiveDayToggled(day: Int) {
        val current = _uiState.value.activeDays
        val updated = if (day in current) current - day else current + day
        _uiState.update { it.copy(activeDays = updated) }
    }

    fun onHardDayToggled() {
        _uiState.update { it.copy(isHardDayIncluded = !it.isHardDayIncluded) }
    }

    fun onSnoozeToggled() {
        _uiState.update { it.copy(snoozeEnabled = !it.snoozeEnabled) }
    }

    fun onSave() {
        val state = _uiState.value
        logger.d("CreateHabitViewModel → saving habit: name=${state.name}, type=${state.selectedType}, hour=${state.scheduledHour}, minute=${state.scheduledMinute}, interval=${state.intervalMinutes}")
        if (!validate(state)) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, saveError = null) }
            try {
                var habit = buildHabit(state)

                // Calculate next occurrence before saving
                val now = System.currentTimeMillis()
                val triggerTime = when (habit.type) {
                    HabitType.FIXED -> {
                        // For FIXED, find the next available time today or tomorrow (11, 16, 20)
                        calculateNextFixedTime(now)
                    }

                    HabitType.FREE -> {
                        if (habit.scheduledHour != null && habit.scheduledMinute != null) {
                            buildTriggerTime(habit.scheduledHour, habit.scheduledMinute)
                        } else null
                    }

                    HabitType.INTERVAL -> {
                        // Start with the interval from now
                        now + ((habit.intervalMinutes ?: 60) * 60 * 1000L)
                    }
                }

                habit = habit.copy(nextOccurrenceMillis = triggerTime)
                val habitId = createHabitUseCase(habit)

                triggerTime?.let {
                    alarmScheduler.scheduleHabitReminder(
                        habitId = habitId,
                        habitName = habit.name,
                        triggerTimeMillis = it
                    )
                    logger.i("CreateHabitViewModel → alarm scheduled for '${habit.name}' at $it")
                }

                logger.i("CreateHabitViewModel → habit created successfully: ${state.name}")
                _uiState.update { it.copy(isSaving = false) }
                _events.emit(CreateHabitEvent.HabitSaved)
            } catch (e: Exception) {
                logger.e(throwable = e, message = "CreateHabitViewModel → error saving habit")
                _uiState.update {
                    it.copy(isSaving = false, saveError = "Could not save habit. Try again.")
                }
            }
        }
    }

    private fun calculateNextFixedTime(nowMillis: Long): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = nowMillis
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)

        val targetHour = when {
            currentHour < 11 -> 11
            currentHour < 16 -> 16
            currentHour < 20 -> 20
            else -> {
                calendar.add(Calendar.DAY_OF_YEAR, 1)
                11
            }
        }

        calendar.set(Calendar.HOUR_OF_DAY, targetHour)
        calendar.set(Calendar.MINUTE, 0)

        return calendar.timeInMillis
    }

    private fun buildTriggerTime(hour: Int, minute: Int): Long {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }
        return calendar.timeInMillis
    }

    private fun validate(state: CreateHabitUiState): Boolean {
        var valid = true
        if (state.name.isBlank()) {
            _uiState.update { it.copy(nameError = "Habit name is required") }
            valid = false
        }
        if (state.selectedCategory == null) {
            _uiState.update { it.copy(categoryError = "Please select a category") }
            valid = false
        }
        return valid
    }

    private fun buildHabit(state: CreateHabitUiState): Habit = Habit(
        name = state.name.trim(),
        category = state.selectedCategory!!,
        type = state.selectedType,
        status = HabitStatus.PENDING,
        dayMoment = state.selectedDayMoment,
        scheduledHour = state.scheduledHour,
        scheduledMinute = state.scheduledMinute,
        intervalMinutes = state.intervalMinutes,
        dailyGoalAmount = state.dailyGoalAmount,
        dailyGoalUnit = state.dailyGoalUnit,
        amountPerInterval = state.amountPerInterval,
        currentAmount = 0f,
        estimatedMinutes = state.estimatedMinutes,
        activeDays = state.activeDays,
        isHardDayIncluded = state.isHardDayIncluded,
        snoozeEnabled = state.snoozeEnabled,
        createdAt = System.currentTimeMillis()
    )
}