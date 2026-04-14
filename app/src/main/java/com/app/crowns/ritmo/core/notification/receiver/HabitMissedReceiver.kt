package com.app.crowns.ritmo.core.notification.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.app.crowns.ritmo.core.logging.AppLogger
import com.app.crowns.ritmo.core.notification.AlarmScheduler
import com.app.crowns.ritmo.core.notification.NotificationExtras
import com.app.crowns.ritmo.core.notification.RitmoNotificationManager
import com.app.crowns.ritmo.feature.habit.domain.model.HabitStatus
import com.app.crowns.ritmo.feature.habit.domain.model.HabitType
import com.app.crowns.ritmo.feature.habit.domain.repository.HabitRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HabitMissedReceiver : BroadcastReceiver() {

    @Inject
    lateinit var habitRepository: HabitRepository

    @Inject
    lateinit var alarmScheduler: AlarmScheduler

    @Inject
    lateinit var notificationManager: RitmoNotificationManager

    @Inject
    lateinit var logger: AppLogger

    override fun onReceive(context: Context, intent: Intent) {
        val habitId = intent.getLongExtra(NotificationExtras.HABIT_ID, -1L)
        if (habitId == -1L) return

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val habit = habitRepository.getHabitById(habitId)
                if (habit != null) {
                    logger.w("HabitMissedReceiver → Habit '${habit.name}' missed (estimated time expired)")

                    // 1. Mark as missed in DB
                    habitRepository.updateStatus(habitId, HabitStatus.MISSED.name)

                    // 2. Clear notification
                    notificationManager.cancelNotification(habitId)

                    // 3. Schedule next occurrence if it's an interval habit
                    if (habit.type == HabitType.INTERVAL) {
                        val intervalMillis = (habit.intervalMinutes ?: 60) * 60 * 1000L
                        val nextTrigger = System.currentTimeMillis() + intervalMillis

                        habitRepository.updateHabit(habit.copy(nextOccurrenceMillis = nextTrigger))

                        alarmScheduler.scheduleHabitReminder(
                            habitId = habit.id,
                            habitName = habit.name,
                            triggerTimeMillis = nextTrigger
                        )
                        logger.i("HabitMissedReceiver → scheduled next interval after MISS for '${habit.name}' in ${habit.intervalMinutes}m")
                    }
                }
            } catch (e: Exception) {
                logger.e(e, "HabitMissedReceiver → error")
            } finally {
                pendingResult.finish()
            }
        }
    }
}