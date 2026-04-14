package com.app.crowns.ritmo.feature.habit.domain.model

data class Habit(
    val id: Long = 0,
    val name: String,
    val category: HabitCategory,
    val type: HabitType,
    val status: HabitStatus = HabitStatus.PENDING,
    val dayMoment: DayMoment = DayMoment.ANYTIME,

    // Para FIXED y FREE
    val scheduledHour: Int? = null,        // 8 (hora del día)
    val scheduledMinute: Int? = null,     // 30

    // Para INTERVAL
    val intervalMinutes: Int? = null,     // cada 60 minutos
    val dailyGoalAmount: Float? = null,   // 3.0 litros
    val dailyGoalUnit: String? = null,    // "L", "ml"
    val amountPerInterval: Float? = null, // 0.5 litros cada vez
    val currentAmount: Float? = null,     // cuánto lleva hoy

    // Snooze
    val isSnoozed: Boolean = false,
    val snoozeUntilMillis: Long? = null,

    // Configuration
    val estimatedMinutes: Int = 2,
    val activeDays: Set<Int> = setOf(1,2,3,4,5,6,7), // 1=Lun … 7=Dom
    val isHardDayIncluded: Boolean = true,
    val snoozeEnabled: Boolean = false,
    val linkedHabitIds: List<Long> = emptyList(),

    // Streak
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val nextOccurrenceMillis: Long? = null
)