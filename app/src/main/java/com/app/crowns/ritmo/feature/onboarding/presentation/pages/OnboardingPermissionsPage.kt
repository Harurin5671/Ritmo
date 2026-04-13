package com.app.crowns.ritmo.feature.onboarding.presentation.pages

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.app.crowns.ritmo.feature.onboarding.presentation.components.OnboardingLayout
import com.app.crowns.ritmo.feature.onboarding.presentation.components.PermissionCard
import com.app.crowns.ritmo.ui.effects.OnResumeEffect
import com.app.crowns.ritmo.ui.theme.Primary
import androidx.core.net.toUri

@Composable
fun OnboardingPermissionsPage(
    notificationsGranted: Boolean,
    exactAlarmsGranted: Boolean,
    notificationDeniedOnce: Boolean,
    onPermissionResult: (Boolean) -> Unit,
    onRefreshPermissions: () -> Unit,
    onFinish: () -> Unit,
    onBack: () -> Unit,
    isSaving: Boolean,
    isButtonEnabled: Boolean
) {
    val context = LocalContext.current

    OnResumeEffect {
        onRefreshPermissions()
    }

    // ── Notification runtime-permission launcher (Android 13+) ──
    val notificationLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        onPermissionResult(granted)
    }

    // ── Generic Settings launcher (exact alarms / app-info) ──
    val settingsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        onPermissionResult(notificationsGranted)
    }

    OnboardingLayout(
        onNext = onFinish,
        buttonText = if (isSaving) "Saving..." else "Finish Setup",
        isButtonEnabled = isButtonEnabled
    ) {
        Column {
            Text(
                text = "Set up your reminders",
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Consistency is key. We'll help you stay on track with gentle nudges.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFFC8C4D7)
            )
            Spacer(Modifier.height(48.dp))

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                // ── 1. Notifications ──
                PermissionCard(
                    icon = Icons.Outlined.Notifications,
                    title = "Notifications",
                    description = "Get reminded when it's time for your habits",
                    isGranted = notificationsGranted,
                    accentColor = Primary,
                    onAllow = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            if (notificationDeniedOnce) {
                                // Already denied once → open App Settings
                                settingsLauncher.launch(
                                    Intent(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.fromParts("package", context.packageName, null)
                                    )
                                )
                            } else {
                                notificationLauncher.launch(
                                    Manifest.permission.POST_NOTIFICATIONS
                                )
                            }
                        }
                    },
                    warningText = if (notificationDeniedOnce && !notificationsGranted)
                        "Denied — tap to open Settings" else null,
                    onWarningClick = {
                        settingsLauncher.launch(
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", context.packageName, null)
                            )
                        )
                    }
                )

                // ── 2. Exact Alarms (only relevant on Android 12+) ──
                PermissionCard(
                    icon = Icons.Outlined.Alarm,
                    title = "Exact Alarms",
                    description = "Schedule precise reminders at the right time",
                    isGranted = exactAlarmsGranted,
                    accentColor = Primary,
                    onAllow = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            settingsLauncher.launch(
                                Intent(
                                    Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
                                    "package:${context.packageName}".toUri()
                                )
                            )
                        }
                    }
                )
            }
        }
    }
}