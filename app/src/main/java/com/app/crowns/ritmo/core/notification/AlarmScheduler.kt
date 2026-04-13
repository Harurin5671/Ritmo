package com.app.crowns.ritmo.core.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import com.app.crowns.ritmo.core.logging.AppLogger
import com.app.crowns.ritmo.core.notification.receiver.HabitReminderReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmScheduler @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val logger: AppLogger
) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun scheduleHabitReminder(
        habitId: Long,
        habitName: String,
        hour: Int,
        minute: Int
    ) {
        val triggerTime = buildTriggerTime(hour, minute)
        val pendingIntent = buildPendingIntent(habitId, habitName)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                logger.w("AlarmScheduler → cannot schedule exact alarms, permission missing")
                return
            }
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            pendingIntent
        )
        logger.i("AlarmScheduler → scheduled habitId=$habitId at $hour:$minute triggerTime=$triggerTime")
    }

    fun cancelHabitReminder(habitId: Long, habitName: String) {
        val pendingIntent = buildPendingIntent(habitId, habitName)
        alarmManager.cancel(pendingIntent)
        logger.i("AlarmScheduler → cancelled habitId=$habitId")
    }

    fun snoozeHabitReminder(
        habitId: Long,
        habitName: String,
        snoozeMinutes: Int
    ) {
        val triggerTime = System.currentTimeMillis() + (snoozeMinutes * 60 * 1000L)
        val pendingIntent = buildPendingIntent(habitId, habitName)

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            pendingIntent
        )
        logger.i("AlarmScheduler → snoozed habitId=$habitId for $snoozeMinutes min")
    }

    private fun buildTriggerTime(hour: Int, minute: Int): Long {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }
        return  calendar.timeInMillis
    }

    private fun buildPendingIntent(habitId: Long, habitName: String): PendingIntent {
        val intent = Intent(context, HabitReminderReceiver::class.java).apply {
            putExtra(NotificationExtras.HABIT_ID, habitId)
            putExtra(NotificationExtras.HABIT_NAME, habitName)
        }
        return PendingIntent.getBroadcast(
            context,
            habitId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}