package com.app.crowns.ritmo.core.notification.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.app.crowns.ritmo.core.logging.AppLogger
import com.app.crowns.ritmo.core.notification.AlarmScheduler
import com.app.crowns.ritmo.feature.habit.domain.repository.HabitRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver: BroadcastReceiver() {

    @Inject lateinit var habitRepository: HabitRepository
    @Inject lateinit var alarmScheduler: AlarmScheduler
    @Inject lateinit var logger: AppLogger

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return
        logger.i("BootReceiver → device rebooted, re-scheduling alarms")

        CoroutineScope(Dispatchers.IO).launch {
            val habits = habitRepository.getAllHabits().first()
            habits.forEach { habit ->
                if (habit.scheduledHour != null && habit.scheduledMinute != null) {
                    alarmScheduler.scheduleHabitReminder(
                        habitId = habit.id,
                        habitName = habit.name,
                        hour = habit.scheduledHour,
                        minute = habit.scheduledMinute
                    )
                }
            }
            logger.i("BootReceiver → re-scheduled ${habits.size} alarms")
        }
    }
}