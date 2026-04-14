package com.app.crowns.ritmo.feature.habit.data.repository

import com.app.crowns.ritmo.core.logging.AppLogger
import com.app.crowns.ritmo.feature.habit.domain.model.Habit
import com.app.crowns.ritmo.feature.habit.domain.model.HabitStatus
import com.app.crowns.ritmo.feature.habit.domain.repository.HabitRepository
import com.app.crowns.ritmo.feature.habit.data.local.HabitDao
import com.app.crowns.ritmo.feature.habit.data.mapper.toDomain
import com.app.crowns.ritmo.feature.habit.data.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HabitRepositoryImpl @Inject constructor(
    private val dao: HabitDao,
    private val logger: AppLogger
) : HabitRepository {
    override fun getAllHabits(): Flow<List<Habit>> {
        logger.d("HabitRepository → getAllHabits")
        return dao.getAllHabits().map { entities ->
            logger.d("HabitRepository → getAllHabits → ${entities.size}")
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getHabitById(id: Long): Habit? {
        logger.d("HabitRepository → getHabitById id=$id")
        return dao.getHabitById(id)?.toDomain()
    }

    override suspend fun insertHabit(habit: Habit): Long {
        logger.i("HabitRepository → insertHabit name=${habit.name}")
        return dao.insertHabit(habit.toEntity())
    }

    override suspend fun updateHabit(habit: Habit) {
        logger.i("HabitRepository → updateHabit id=${habit.id}")
        dao.updateHabit(habit.toEntity())
    }

    override suspend fun deleteHabit(habit: Habit) {
        logger.w("HabitRepository → deleteHabit id=${habit.id} name=${habit.name}")
        dao.deleteHabit(habit.toEntity())
    }

    override suspend fun completeHabit(id: Long) {
        logger.i("HabitRepository → completeHabit id=$id")
        dao.updateStatus(id, HabitStatus.COMPLETED.name)
    }

    override suspend fun updateStatus(id: Long, status: String) {
        logger.i("HabitRepository → updateStatus id=$id status=$status")
        dao.updateStatus(id, status)
    }

    override suspend fun updateCurrentAmount(id: Long, amount: Float) {
        logger.i("HabitRepository → updateCurrentAmount id=$id amount=$amount")
        dao.updateCurrentAmount(id, amount)
    }

    override suspend fun snoozeHabit(id: Long, untilMillis: Long) {
        logger.i("HabitRepository → snoozeHabit id=$id until=$untilMillis")
        dao.updateSnooze(id, isSnoozed = true, until = untilMillis)
    }

    override suspend fun resetDailyStatus() {
        logger.w("HabitRepository → resetDailyStatus — resetting all habits for new day")
        dao.resetDailyStatus()
    }
}