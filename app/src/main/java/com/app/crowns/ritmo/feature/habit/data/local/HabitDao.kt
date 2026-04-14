package com.app.crowns.ritmo.feature.habit.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.app.crowns.ritmo.feature.habit.data.local.HabitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Query("SELECT * FROM habits ORDER BY scheduledHour ASC, scheduledMinute ASC")
    fun getAllHabits(): Flow<List<HabitEntity>>

    @Query("SELECT * FROM habits WHERE id = :id")
    suspend fun getHabitById(id: Long): HabitEntity?

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertHabit(habit: HabitEntity): Long

    @Update
    suspend fun updateHabit(habit: HabitEntity)

    @Delete
    suspend fun deleteHabit(habit: HabitEntity)

    @Query("UPDATE habits SET status = :status where id = :id")
    suspend fun updateStatus(id: Long, status: String)

    @Query("UPDATE habits SET status = 'MISSED' WHERE id = :id")
    suspend fun markAsMissed(id: Long)

    @Query("UPDATE habits SET isSnoozed = :isSnoozed, snoozeUntilMillis = :until WHERE id = :id")
    suspend fun updateSnooze(id: Long, isSnoozed: Boolean, until: Long?)

    @Query("UPDATE habits SET currentAmount = :amount WHERE id = :id")
    suspend fun updateCurrentAmount(id: Long, amount: Float)

    @Query("UPDATE habits SET status = 'PENDING', isSnoozed = 0, snoozeUntilMillis = NULL, currentAmount = 0")
    suspend fun resetDailyStatus()
}