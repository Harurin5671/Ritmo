package com.app.crowns.ritmo.core.notification

import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.app.crowns.ritmo.MainActivity
import com.app.crowns.ritmo.R
import com.app.crowns.ritmo.core.logging.AppLogger
import com.app.crowns.ritmo.core.notification.model.RitmoNotificationChannel
import com.app.crowns.ritmo.core.notification.receiver.HabitActionReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RitmoNotificationManager @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val logger: AppLogger
) {
    private val notificationManager = NotificationManagerCompat.from(context)

    fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            RitmoNotificationChannel.entries.forEach { channel ->
                val notificationChannel = NotificationChannel(
                    channel.channelId,
                    channel.channelName,
                    channel.importance
                ).apply {
                    description = channel.description
                    enableLights(true)
                    enableVibration(true)
                }
                notificationManager.createNotificationChannel(notificationChannel)
                logger.d("RitmoNotificationManager → channel created: ${channel.channelId}")
            }
        }
    }

    fun showHabitReminder(
        habitId: Long,
        habitName: String,
        scheduledTime: String
    ) {
        logger.i("RitmoNotificationManager → showing reminder for habitId=$habitId name=$habitName")

        val tapIntent = buildTapIntent(habitId)
        val doneIntent = buildActionIntent(habitId, NotificationActions.ACTION_DONE)
        val snoozeIntent = buildActionIntent(habitId, NotificationActions.ACTION_SNOOZE)

        val notification = NotificationCompat.Builder(
            context,
            RitmoNotificationChannel.HABIT_REMINDERS.channelId
        )
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(habitName)
            .setContentText("Scheduled for $scheduledTime")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .addAction(
                0,
                "🎉 Done",
                doneIntent
            )
            .addAction(
                0,
                "⏰ Snooze 30 min",
                snoozeIntent
            )
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Time to: $habitName\nScheduled for $scheduledTime")
            )
            .setColorized(true)
            .setColor(0xFF7C6FFF.toInt())
            .build()

        if (notificationManager.areNotificationsEnabled()) {
            notificationManager.notify(habitId.toInt(), notification)
        } else {
            logger.w("RitmoNotificationManager → notifications disabled, cannot show reminder")
        }
    }

    fun cancelNotification(habitId: Long) {
        logger.d("RitmoNotificationManager → cancelling notification habitId=$habitId")
        notificationManager.cancel(habitId.toInt())
    }

    fun showNightSummary(completed: Int, total: Int) {
        logger.i("RitmoNotificationManager → showing night summary $completed/$total")

        val tapIntent = buildTapIntent(habitId = -1L)
        val notification = NotificationCompat.Builder(
            context,
            RitmoNotificationChannel.DAILY_SUMMARY.channelId
        )
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Daily Summary")
            .setContentText("You completed $completed of $total habits today")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(true)
            .setContentIntent(tapIntent)
            .build()

        if (notificationManager.areNotificationsEnabled()) {
            notificationManager.notify(SUMMARY_NOTIFICATION_ID, notification)
        }
    }

    private fun buildTapIntent(habitId: Long): PendingIntent {
        val intent = Intent(context, MainActivity::class.java). apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra(NotificationExtras.HABIT_ID, habitId)
        }
        return PendingIntent.getActivity(
            context,
            habitId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun buildActionIntent(habitId: Long, action: String): PendingIntent {
        val intent = Intent(context, HabitActionReceiver::class.java).apply {
            this.action = action
            putExtra(NotificationExtras.HABIT_ID, habitId)
        }

        return PendingIntent.getBroadcast(
            context,
            (habitId.toInt() * 10) + action.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    companion object {
        const val SUMMARY_NOTIFICATION_ID = 9999
    }
}

object NotificationActions {
    const val ACTION_DONE   = "com.app.crowns.ritmo.ACTION_HABIT_DONE"
    const val ACTION_SNOOZE = "com.app.crowns.ritmo.ACTION_HABIT_SNOOZE"
}

object NotificationExtras {
    const val HABIT_ID   = "habit_id"
    const val HABIT_NAME = "habit_name"
    const val SNOOZE_MINUTES = "snooze_minutes"
}