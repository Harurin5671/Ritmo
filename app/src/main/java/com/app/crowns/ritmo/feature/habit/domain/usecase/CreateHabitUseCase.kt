package com.app.crowns.ritmo.feature.habit.domain.usecase

import com.app.crowns.ritmo.core.logging.AppLogger
import com.app.crowns.ritmo.feature.habit.domain.model.Habit
import com.app.crowns.ritmo.feature.habit.domain.repository.HabitRepository
import javax.inject.Inject

class CreateHabitUseCase @Inject constructor(
    private val repository: HabitRepository,
    private val logger: AppLogger
) {
    suspend operator fun invoke(habit: Habit): Long {
        logger.i("CreateHabitUseCase → creating habit: ${habit.name}")
        return repository.insertHabit(habit)
    }
}