package com.app.crowns.ritmo.core.notification.model

enum class RitmoNotificationChannel(
    val channelId: String,
    val channelName: String,
    val description: String,
    val importance: Int
) {
    HABIT_REMINDERS(
        channelId = "ritmo_habit_reminders",
        channelName = "Habit Reminders",
        description = "Reminders for you scheduled habits",
        importance = android.app.NotificationManager.IMPORTANCE_HIGH
    ),
    WATER_REMINDERS(
        channelId = "ritmo_water_reminders",
        channelName = "Hydration Reminders",
        description = "Reminders to drink water throughout the day",
        importance = android.app.NotificationManager.IMPORTANCE_DEFAULT
    ),
    DAILY_SUMMARY(
        channelId = "ritmo_daily_summary",
        channelName = "Daily Summary",
        description = "Your nightly habit summary",
        importance = android.app.NotificationManager.IMPORTANCE_LOW
    )
}