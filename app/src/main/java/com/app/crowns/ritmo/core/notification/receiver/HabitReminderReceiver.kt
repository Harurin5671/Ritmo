package com.app.crowns.ritmo.core.notification.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.app.crowns.ritmo.core.notification.NotificationExtras
import com.app.crowns.ritmo.core.notification.RitmoNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HabitReminderReceiver: BroadcastReceiver() {

    @Inject lateinit var notificationManager: RitmoNotificationManager

    override fun onReceive(context: Context, intent: Intent) {
        val habitId = intent.getLongExtra(NotificationExtras.HABIT_ID, -1L)
        val habitName = intent.getStringExtra(NotificationExtras.HABIT_NAME) ?: return

        if (habitId == -1L) return

        notificationManager.showHabitReminder(
            habitId = habitId,
            habitName = habitName,
            scheduledTime = "Now"
        )
    }
}