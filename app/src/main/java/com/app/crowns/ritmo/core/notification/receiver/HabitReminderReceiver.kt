package com.app.crowns.ritmo.core.notification.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.app.crowns.ritmo.core.logging.AppLogger
import com.app.crowns.ritmo.core.notification.AlarmScheduler
import com.app.crowns.ritmo.core.notification.NotificationExtras
import com.app.crowns.ritmo.core.notification.RitmoNotificationManager
import com.app.crowns.ritmo.feature.habit.domain.repository.HabitRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class HabitReminderReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationManager: RitmoNotificationManager

    @Inject
    lateinit var habitRepository: HabitRepository

    @Inject
    lateinit var alarmScheduler: AlarmScheduler

    @Inject
    lateinit var logger: AppLogger

    override fun onReceive(context: Context, intent: Intent) {
        val habitId = intent.getLongExtra(NotificationExtras.HABIT_ID, -1L)
        val habitName = intent.getStringExtra(NotificationExtras.HABIT_NAME) ?: return

        if (habitId == -1L) return

        // --- NIGHT WINDOW CHECK (00:00 - 09:00) ---
        val now = Calendar.getInstance()
        val hour = now.get(Calendar.HOUR_OF_DAY)

        if (hour < 9) {
            val targetTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 9)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis

            logger.i("HabitReminderReceiver → Quiet window detected ($hour:00). Rescheduling habitId=$habitId to 09:00 AM")
            alarmScheduler.scheduleHabitReminder(habitId, habitName, targetTime)
            return
        }
        // ------------------------------------------

        notificationManager.showHabitReminder(
            habitId = habitId,
            habitName = habitName,
            scheduledTime = "Now"
        )

        // Schedule the missed check (half-time)
        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val habit = habitRepository.getHabitById(habitId)
                if (habit != null && habit.estimatedMinutes > 0) {
                    alarmScheduler.scheduleMissedCheck(habitId, habit.estimatedMinutes)
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}