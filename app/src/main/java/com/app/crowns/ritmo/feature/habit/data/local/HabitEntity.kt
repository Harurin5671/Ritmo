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
    val currentAmount: Float?,

    val isSnoozed: Boolean,
    val snoozeUntilMillis: Long?,

    val estimatedMinutes: Int,
    val activeDays: String,         // "1,2,3,4,5,6,7" guardado como string
    val isHardDayIncluded: Boolean,
    val linkedHabitIds: String,     // "1,2,3" guardado como string

    val currentStreak: Int,
    val longestStreak: Int,
    val createdAt: Long
)