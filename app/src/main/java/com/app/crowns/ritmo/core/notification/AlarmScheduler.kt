package com.app.crowns.ritmo.core.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.app.crowns.ritmo.core.logging.AppLogger
import com.app.crowns.ritmo.core.notification.receiver.HabitMissedReceiver
import com.app.crowns.ritmo.core.notification.receiver.HabitReminderReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
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
        scheduleHabitReminder(habitId, habitName, triggerTime)
    }

    fun scheduleHabitReminder(
        habitId: Long,
        habitName: String,
        triggerTimeMillis: Long
    ) {
        logger.d(
            "AlarmScheduler → Preparing to schedule reminder for habitId=$habitId ($habitName) at $triggerTimeMillis (${
                java.util.Date(
                    triggerTimeMillis
                )
            })"
        )
        val pendingIntent = buildPendingIntent(habitId, habitName)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val canSchedule = alarmManager.canScheduleExactAlarms()
            logger.d("AlarmScheduler → Build.VERSION.SDK_INT >= S. canScheduleExactAlarms=$canSchedule")
            if (!canSchedule) {
                logger.e(
                    null,
                    "AlarmScheduler → ERROR: cannot schedule exact alarms, permission missing!"
                )
                return
            }
        }

        try {
            val alarmClockInfo = AlarmManager.AlarmClockInfo(triggerTimeMillis, pendingIntent)
            alarmManager.setAlarmClock(
                alarmClockInfo,
                pendingIntent
            )
            logger.i("AlarmScheduler → SUCCESS: scheduled (ALARM_CLOCK) habitId=$habitId at triggerTime=$triggerTimeMillis")
        } catch (e: Exception) {
            logger.e(e, "AlarmScheduler → CRITICAL ERROR scheduling habitId=$habitId")
        }
    }

    fun cancelHabitReminder(habitId: Long, habitName: String) {
        logger.d("AlarmScheduler → Request to cancel reminder for habitId=$habitId")
        val pendingIntent = buildPendingIntent(habitId, habitName)
        alarmManager.cancel(pendingIntent)
        logger.i("AlarmScheduler → SUCCESS: cancelled habitId=$habitId")
    }

    fun snoozeHabitReminder(
        habitId: Long,
        habitName: String,
        snoozeMinutes: Int
    ) {
        val triggerTime = System.currentTimeMillis() + (snoozeMinutes * 60 * 1000L)
        logger.d("AlarmScheduler → Request to snooze habitId=$habitId for $snoozeMinutes min (Trigger at $triggerTime)")
        val pendingIntent = buildPendingIntent(habitId, habitName)

        val alarmClockInfo = AlarmManager.AlarmClockInfo(triggerTime, pendingIntent)
        alarmManager.setAlarmClock(
            alarmClockInfo,
            pendingIntent
        )
        logger.i("AlarmScheduler → SUCCESS: snoozed (ALARM_CLOCK) habitId=$habitId for $snoozeMinutes min")
    }

    fun scheduleMissedCheck(habitId: Long, estimatedMinutes: Int) {
        val triggerTime = System.currentTimeMillis() + (estimatedMinutes * 60 * 1000L)
        logger.d("AlarmScheduler → Preparing missed check for habitId=$habitId in $estimatedMinutes min (Trigger at $triggerTime)")
        val intent = Intent(context, HabitMissedReceiver::class.java).apply {
            putExtra(NotificationExtras.HABIT_ID, habitId)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            habitId.toInt() + 10000, // Unique ID for missed check
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            pendingIntent
        )
        logger.i("AlarmScheduler → SUCCESS: scheduled missed check for habitId=$habitId")
    }

    fun cancelMissedCheck(habitId: Long) {
        logger.d("AlarmScheduler → Request to cancel missed check for habitId=$habitId")
        val intent = Intent(context, HabitMissedReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            habitId.toInt() + 10000,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            logger.i("AlarmScheduler → SUCCESS: cancelled missed check for habitId=$habitId")
        } else {
            logger.d("AlarmScheduler → No active missed check found to cancel for habitId=$habitId")
        }
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
        return calendar.timeInMillis
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