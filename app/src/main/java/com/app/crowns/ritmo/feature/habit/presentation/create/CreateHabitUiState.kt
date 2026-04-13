package com.app.crowns.ritmo.feature.habit.presentation.create

import com.app.crowns.ritmo.feature.habit.domain.model.DayMoment
import com.app.crowns.ritmo.feature.habit.domain.model.HabitCategory
import com.app.crowns.ritmo.feature.habit.domain.model.HabitType

data class CreateHabitUiState(
    val name: String = "",
    val nameError: String? = null,
    val selectedCategory: HabitCategory? = null,
    val categoryError: String? = null,
    val selectedType: HabitType = HabitType.FIXED,
    val selectedDayMoment: DayMoment = DayMoment.MORNING,
    val scheduledHour: Int? = null,
    val scheduledMinute: Int? = null,
    val intervalMinutes: Int? = null,
    val dailyGoalAmount: Float? = null,
    val dailyGoalUnit: String = "L",
    val estimatedMinutes: Int = 2,
    val activeDays: Set<Int> = setOf(1, 2, 3, 4, 5, 6, 7),
    val isHardDayIncluded: Boolean = true,
    val snoozeEnabled: Boolean = false,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val saveError: String? = null
) {
    val canSave: Boolean
        get() = name.isNotBlank() && selectedCategory != null && !isSaving
}