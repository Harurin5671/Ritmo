package com.app.crowns.ritmo.feature.habit.domain.usecase

import com.app.crowns.ritmo.core.logging.AppLogger
import com.app.crowns.ritmo.feature.habit.domain.repository.HabitRepository
import javax.inject.Inject

class CompleteHabitUseCase @Inject constructor(
    private val repository: HabitRepository,
    private val logger: AppLogger
) {
    suspend operator fun invoke(habitId: Long) {
        logger.i("CompleteHabitUseCase → completing habitId=$habitId")
        repository.completeHabit(habitId)
    }
}