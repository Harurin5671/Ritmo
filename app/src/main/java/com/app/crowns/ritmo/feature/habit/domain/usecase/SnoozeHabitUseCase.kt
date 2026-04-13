package com.app.crowns.ritmo.feature.habit.domain.usecase

import com.app.crowns.ritmo.core.logging.AppLogger
import com.app.crowns.ritmo.feature.habit.domain.repository.HabitRepository
import javax.inject.Inject

class SnoozeHabitUseCase @Inject constructor(
    private val repository: HabitRepository,
    private val logger: AppLogger
) {
    suspend operator fun invoke(habitId: Long, snoozeMinutes: Int) {
        val untilMillis = System.currentTimeMillis() + (snoozeMinutes * 60 * 1000L)
        logger.i("SnoozeHabitUseCase → habitId=$habitId snoozeMinutes=$snoozeMinutes until=$untilMillis")
        repository.snoozeHabit(habitId, untilMillis)
    }
}