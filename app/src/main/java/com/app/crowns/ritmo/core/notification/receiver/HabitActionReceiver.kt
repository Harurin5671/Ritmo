package com.app.crowns.ritmo.core.notification.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.app.crowns.ritmo.core.logging.AppLogger
import com.app.crowns.ritmo.core.notification.AlarmScheduler
import com.app.crowns.ritmo.core.notification.NotificationActions
import com.app.crowns.ritmo.core.notification.NotificationExtras
import com.app.crowns.ritmo.core.notification.RitmoNotificationManager
import com.app.crowns.ritmo.feature.habit.domain.repository.HabitRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HabitActionReceiver: BroadcastReceiver() {

    @Inject lateinit var habitRepository: HabitRepository
    @Inject lateinit var notificationManager: RitmoNotificationManager
    @Inject lateinit var alarmScheduler: AlarmScheduler
    @Inject lateinit var logger: AppLogger

    override fun onReceive(context: Context, intent: Intent) {
        val habitId = intent.getLongExtra(NotificationExtras.HABIT_ID, -1L)
        if (habitId == -1L) return

        when (intent.action) {
            NotificationActions.ACTION_DONE-> {
                logger.i("HabitActionReceiver → ACTION_DONE habitId=$habitId")
                val pendingResult = goAsync()
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val habit = habitRepository.getHabitById(habitId)
                        habit?.let {
//                            alarmScheduler
                        }
                    } finally { }
                }
            }
            NotificationActions.ACTION_SNOOZE -> {
                val snoozeMinutes = intent.getIntExtra(NotificationExtras.SNOOZE_MINUTES, 30)
                logger.i("HabitActionReceiver → ACTION_SNOOZE habitId=$habitId minutes=$snoozeMinutes")
            }
        }
    }
}