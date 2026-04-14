package com.app.crowns.ritmo.core.notification.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.app.crowns.ritmo.core.logging.AppLogger
import com.app.crowns.ritmo.core.notification.AlarmScheduler
import com.app.crowns.ritmo.feature.habit.domain.model.HabitType
import com.app.crowns.ritmo.feature.habit.domain.repository.HabitRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var habitRepository: HabitRepository

    @Inject
    lateinit var alarmScheduler: AlarmScheduler

    @Inject
    lateinit var logger: AppLogger

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return
        logger.i("BootReceiver → device rebooted, re-scheduling alarms")

        CoroutineScope(Dispatchers.IO).launch {
            val habits = habitRepository.getAllHabits().first()
            habits.forEach { habit ->
                val now = System.currentTimeMillis()
                val nextOccurrence = habit.nextOccurrenceMillis

                if (nextOccurrence != null && nextOccurrence > now) {
                    // Still in the future, just reschedule
                    alarmScheduler.scheduleHabitReminder(
                        habitId = habit.id,
                        habitName = habit.name,
                        triggerTimeMillis = nextOccurrence
                    )
                } else if (habit.type == HabitType.FIXED || habit.type == HabitType.FREE) {
                    // For fixed/free, if missed, schedule for next available day at same time
                    if (habit.scheduledHour != null && habit.scheduledMinute != null) {
                        alarmScheduler.scheduleHabitReminder(
                            habitId = habit.id,
                            habitName = habit.name,
                            hour = habit.scheduledHour,
                            minute = habit.scheduledMinute
                        )
                    }
                } else if (habit.type == HabitType.INTERVAL) {
                    // For intervals, if we missed it, schedule one starting now + interval
                    val nextInterval = now + ((habit.intervalMinutes ?: 60) * 60 * 1000L)
                    alarmScheduler.scheduleHabitReminder(
                        habitId = habit.id,
                        habitName = habit.name,
                        triggerTimeMillis = nextInterval
                    )
                }
            }
            logger.i("BootReceiver → re-scheduled ${habits.size} alarms")
        }
    }
}