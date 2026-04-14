package com.app.crowns.ritmo.feature.habit.data.mapper

import com.app.crowns.ritmo.feature.habit.data.local.HabitEntity
import com.app.crowns.ritmo.feature.habit.domain.model.DayMoment
import com.app.crowns.ritmo.feature.habit.domain.model.Habit
import com.app.crowns.ritmo.feature.habit.domain.model.HabitCategory
import com.app.crowns.ritmo.feature.habit.domain.model.HabitStatus
import com.app.crowns.ritmo.feature.habit.domain.model.HabitType

fun HabitEntity.toDomain(): Habit = Habit(
    id = id,
    name = name,
    category = HabitCategory.valueOf(category),
    type = HabitType.valueOf(type),
    status = HabitStatus.valueOf(status),
    dayMoment = DayMoment.valueOf(dayMoment),
    scheduledHour = scheduledHour,
    scheduledMinute = scheduledMinute,
    intervalMinutes = intervalMinutes,
    dailyGoalAmount = dailyGoalAmount,
    dailyGoalUnit = dailyGoalUnit,
    amountPerInterval = amountPerInterval,
    currentAmount = currentAmount,
    isSnoozed = isSnoozed,
    snoozeUntilMillis = snoozeUntilMillis,
    estimatedMinutes = estimatedMinutes,
    activeDays = activeDays
        .split(",")
        .mapNotNull { it.trim().toIntOrNull() }
        .toSet(),
    isHardDayIncluded = isHardDayIncluded,
    linkedHabitIds = linkedHabitIds
        .split(",")
        .mapNotNull { it.trim().toLongOrNull() },
    currentStreak = currentStreak,
    longestStreak = longestStreak,
    createdAt = createdAt,
    nextOccurrenceMillis = nextOccurrenceMillis
)

fun Habit.toEntity(): HabitEntity = HabitEntity(
    id = id,
    name = name,
    category = category.name,
    type = type.name,
    status = status.name,
    dayMoment = dayMoment.name,
    scheduledHour = scheduledHour,
    scheduledMinute = scheduledMinute,
    intervalMinutes = intervalMinutes,
    dailyGoalAmount = dailyGoalAmount,
    dailyGoalUnit = dailyGoalUnit,
    amountPerInterval = amountPerInterval,
    currentAmount = currentAmount,
    isSnoozed = isSnoozed,
    snoozeUntilMillis = snoozeUntilMillis,
    estimatedMinutes = estimatedMinutes,
    activeDays = activeDays.joinToString(","),
    isHardDayIncluded = isHardDayIncluded,
    linkedHabitIds = linkedHabitIds.joinToString(","),
    currentStreak = currentStreak,
    longestStreak = longestStreak,
    createdAt = createdAt,
    nextOccurrenceMillis = nextOccurrenceMillis
)