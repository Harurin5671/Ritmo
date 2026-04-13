package com.app.crowns.ritmo.core.permission

import kotlinx.coroutines.flow.StateFlow

interface PermissionManager {
    val notificationsGranted: StateFlow<Boolean>
    val exactAlarmsGranted: StateFlow<Boolean>
    fun checkPermissions()
}