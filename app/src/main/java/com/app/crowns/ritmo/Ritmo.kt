package com.app.crowns.ritmo

import android.app.Application
import android.content.pm.ApplicationInfo
import com.app.crowns.ritmo.core.notification.RitmoNotificationManager
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class Ritmo : Application() {

    @Inject lateinit var notificationManager: RitmoNotificationManager

    override fun onCreate() {
        super.onCreate()
        val isDebuggable = (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        if (isDebuggable) {
            Timber.plant(Timber.DebugTree())
        }
        notificationManager.createNotificationChannels()
    }
}