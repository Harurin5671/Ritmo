package com.app.crowns.ritmo.feature.habit.domain.usecase

import com.app.crowns.ritmo.core.logging.AppLogger
import com.app.crowns.ritmo.feature.habit.domain.model.Habit
import com.app.crowns.ritmo.feature.habit.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import javax.inject.Inject

class GetTodayHabitsUseCase @Inject constructor(
    private val repository: HabitRepository,
    private val logger: AppLogger
) {
    operator fun invoke(): Flow<List<Habit>> {
        val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        logger.d("GetTodayHabitsUseCase → today dayOfWeek=$today")
        return repository.getAllHabits().map { habits ->
            habits
                .filter { today in it.activeDays }
                .also { logger.d("GetTodayHabitsUseCase → filtered ${it.size} habits for today") }
        }
    }
}