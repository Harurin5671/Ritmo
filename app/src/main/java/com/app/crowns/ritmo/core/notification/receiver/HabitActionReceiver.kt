package com.app.crowns.ritmo.core.notification.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.app.crowns.ritmo.core.logging.AppLogger
import com.app.crowns.ritmo.core.notification.AlarmScheduler
import com.app.crowns.ritmo.core.notification.NotificationActions
import com.app.crowns.ritmo.core.notification.NotificationExtras
import com.app.crowns.ritmo.core.notification.RitmoNotificationManager
import com.app.crowns.ritmo.feature.habit.domain.model.DayMoment
import com.app.crowns.ritmo.feature.habit.domain.model.Habit
import com.app.crowns.ritmo.feature.habit.domain.model.HabitCategory
import com.app.crowns.ritmo.feature.habit.domain.model.HabitStatus
import com.app.crowns.ritmo.feature.habit.domain.model.HabitType
import com.app.crowns.ritmo.feature.habit.domain.repository.HabitRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class HabitActionReceiver : BroadcastReceiver() {

    @Inject
    lateinit var habitRepository: HabitRepository

    @Inject
    lateinit var notificationManager: RitmoNotificationManager

    @Inject
    lateinit var alarmScheduler: AlarmScheduler

    @Inject
    lateinit var logger: AppLogger

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        val habitId = intent.getLongExtra(NotificationExtras.HABIT_ID, -1L)

        logger.d("HabitActionReceiver → onReceive: action=$action, habitId=$habitId")

        if (habitId == -1L) return

        // Special case for test notification
        if (habitId == 999L) {
            logger.i("HabitActionReceiver → test notification action: $action")
            notificationManager.cancelNotification(habitId)
            return
        }

        when (action) {
            NotificationActions.ACTION_DONE -> {
                logger.i("HabitActionReceiver → ACTION_DONE habitId=$habitId")
                val pendingResult = goAsync()
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val habit = habitRepository.getHabitById(habitId) ?: return@launch

                        // 1. Cancel notification and missed check
                        notificationManager.cancelNotification(habitId)
                        alarmScheduler.cancelMissedCheck(habitId)

                        // 2. Update progress if it's Hydration/Interval
                        var isCompletedForToday = false
                        if (habit.category == HabitCategory.HYDRATION &&
                            habit.type == HabitType.INTERVAL
                        ) {

                            val increment = habit.amountPerInterval ?: 0.5f
                            val newAmount = (habit.currentAmount ?: 0f) + increment
                            habitRepository.updateCurrentAmount(habitId, newAmount)

                            val goal = habit.dailyGoalAmount ?: 0f
                            if (newAmount >= goal) {
                                isCompletedForToday = true
                                habitRepository.updateStatus(habitId, HabitStatus.COMPLETED.name)
                            }
                        } else if (habit.type == HabitType.FIXED) {
                            // Logic for FIXED habits with multiple moments
                            habitRepository.completeHabit(habitId)
                            isCompletedForToday = habit.dayMoment == DayMoment.NIGHT
                        } else {
                            // Standard complete for other types
                            habitRepository.completeHabit(habitId)
                            isCompletedForToday = true
                        }

                        // 3. Reschedule
                        val nextTrigger = calculateNextOccurrence(habit, isCompletedForToday)

                        habitRepository.updateHabit(habit.copy(nextOccurrenceMillis = nextTrigger))

                        alarmScheduler.scheduleHabitReminder(
                            habitId = habit.id,
                            habitName = habit.name,
                            triggerTimeMillis = nextTrigger
                        )
                        logger.i("HabitActionReceiver → scheduled next alarm for '${habit.name}' at $nextTrigger")
                    } catch (e: Exception) {
                        logger.e(e, "HabitActionReceiver → Error processing ACTION_DONE")
                    } finally {
                        pendingResult.finish()
                    }
                }
            }

            NotificationActions.ACTION_SNOOZE -> {
                logger.i("HabitActionReceiver → ACTION_SNOOZE habitId=$habitId")
                val pendingResult = goAsync()
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val habit = habitRepository.getHabitById(habitId)
                        if (habit != null) {
                            val snoozeMinutes = 5 // Default snooze
                            alarmScheduler.snoozeHabitReminder(habit.id, habit.name, snoozeMinutes)
                            notificationManager.cancelNotification(habit.id)
                        } else {
                            // If habit not found (like test), just cancel notification
                            notificationManager.cancelNotification(habitId)
                        }
                    } catch (e: Exception) {
                        logger.e(e, "HabitActionReceiver → Error processing ACTION_SNOOZE")
                    } finally {
                        pendingResult.finish()
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateNextOccurrence(
        habit: Habit,
        isCompletedForToday: Boolean
    ): Long {
        val now = Calendar.getInstance()
        val nextOccurrence = Calendar.getInstance()

        when (habit.type) {
            HabitType.FIXED -> {
                // Determine next moment
                when (habit.dayMoment) {
                    DayMoment.MORNING -> {
                        // After morning, next is afternoon (today)
                        nextOccurrence.set(Calendar.HOUR_OF_DAY, 16) // Afternoon 4 PM
                        nextOccurrence.set(Calendar.MINUTE, 0)
                    }

                    DayMoment.AFTERNOON -> {
                        // After afternoon, next is night (today)
                        nextOccurrence.set(Calendar.HOUR_OF_DAY, 20) // Night 8 PM
                        nextOccurrence.set(Calendar.MINUTE, 0)
                    }

                    else -> {
                        // After night or anything else, next is morning tomorrow
                        nextOccurrence.add(Calendar.DAY_OF_YEAR, 1)
                        nextOccurrence.set(Calendar.HOUR_OF_DAY, 11) // Morning 11 AM
                        nextOccurrence.set(Calendar.MINUTE, 0)
                    }
                }
            }

            HabitType.INTERVAL -> {
                if (isCompletedForToday) {
                    // Target 9 AM tomorrow
                    nextOccurrence.add(Calendar.DAY_OF_YEAR, 1)
                    nextOccurrence.set(Calendar.HOUR_OF_DAY, 9)
                    nextOccurrence.set(Calendar.MINUTE, 0)
                } else {
                    val intervalMillis = (habit.intervalMinutes ?: 60) * 60 * 1000L
                    nextOccurrence.timeInMillis = now.timeInMillis + intervalMillis

                    // Check if next occurrence is in a new day
                    if (nextOccurrence.get(Calendar.DAY_OF_YEAR) != now.get(Calendar.DAY_OF_YEAR)) {
                        nextOccurrence.set(Calendar.HOUR_OF_DAY, 9)
                        nextOccurrence.set(Calendar.MINUTE, 0)
                    }
                }
            }

            HabitType.FREE -> {
                nextOccurrence.add(Calendar.DAY_OF_YEAR, 1)
                nextOccurrence.set(Calendar.HOUR_OF_DAY, habit.scheduledHour ?: 9)
                nextOccurrence.set(Calendar.MINUTE, habit.scheduledMinute ?: 0)
            }
        }

        nextOccurrence.set(Calendar.SECOND, 0)
        nextOccurrence.set(Calendar.MILLISECOND, 0)

        // Ensure it's not in the past
        if (nextOccurrence.timeInMillis <= now.timeInMillis) {
            nextOccurrence.add(Calendar.DAY_OF_YEAR, 1)
        }

        return nextOccurrence.timeInMillis
    }
}