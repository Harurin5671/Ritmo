package com.app.crowns.ritmo.feature.onboarding.presentation

import com.app.crowns.ritmo.feature.onboarding.domain.model.StarterTemplate

data class OnboardingUiState(
    val currentPage: Int = 0,
    val userName: String = "",
    val userNameError: String? = null,
    val selectedTemplate: StarterTemplate = StarterTemplate.BASIC_CARE,
    val notificationsGranted: Boolean = false,
    val exactAlarmsGranted: Boolean = false,
    val isSaving: Boolean = false,
    val isCompleted: Boolean = false,
    val notificationPermissionDeniedOnce: Boolean = false,
) {
    val canFinish: Boolean
        get() = notificationsGranted && exactAlarmsGranted && !isSaving
}