package com.app.crowns.ritmo.feature.habit.domain.repository

import com.app.crowns.ritmo.feature.habit.domain.model.Habit
import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    fun getAllHabits(): Flow<List<Habit>>
    suspend fun getHabitById(id: Long): Habit?
    suspend fun insertHabit(habit: Habit): Long
    suspend fun updateHabit(habit: Habit)
    suspend fun deleteHabit(habit: Habit)
    suspend fun completeHabit(id: Long)
    suspend fun updateStatus(id: Long, status: String)
    suspend fun updateCurrentAmount(id: Long, amount: Float)
    suspend fun snoozeHabit(id: Long, untilMillis: Long)
    suspend fun resetDailyStatus()
}