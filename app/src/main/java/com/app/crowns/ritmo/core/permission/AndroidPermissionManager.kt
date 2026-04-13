package com.app.crowns.ritmo.core.permission

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidPermissionManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) : PermissionManager {
    private val _notificationsGranted = MutableStateFlow(false)
    override val notificationsGranted = _notificationsGranted.asStateFlow()

    private val _exactAlarmsGranted = MutableStateFlow(false)
    override val exactAlarmsGranted = _exactAlarmsGranted.asStateFlow()

    override fun checkPermissions() {
        _notificationsGranted.value = checkNotifications()
        _exactAlarmsGranted.value = checkExactAlarms()
    }

    private fun checkNotifications(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private fun checkExactAlarms(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }
}