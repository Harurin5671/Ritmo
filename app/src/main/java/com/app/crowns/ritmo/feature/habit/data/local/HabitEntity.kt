package com.app.crowns.ritmo.feature.habit.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val category: String,
    val type: String,
    val status: String,
    val dayMoment: String,

    val scheduledHour: Int?,
    val scheduledMinute: Int?,

    val intervalMinutes: Int?,
    val dailyGoalAmount: Float?,
    val dailyGoalUnit: String?,
    val amountPerInterval: Float?,
    val currentAmount: Float?,

    val isSnoozed: Boolean,
    val snoozeUntilMillis: Long?,

    val estimatedMinutes: Int,
    val activeDays: String,
    val isHardDayIncluded: Boolean,
    val linkedHabitIds: String,

    val currentStreak: Int,
    val longestStreak: Int,
    val createdAt: Long,
    val nextOccurrenceMillis: Long? = null
)