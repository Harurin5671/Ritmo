package com.app.crowns.ritmo.feature.onboarding.domain.repository

import com.app.crowns.ritmo.feature.onboarding.domain.model.StarterTemplate
import kotlinx.coroutines.flow.Flow

interface OnboardingRepository {
    val isOnboardingCompleted: Flow<Boolean>
    val userName: Flow<String>
    suspend fun completeOnboarding()
    suspend fun saveUserName(name: String)
    suspend fun saveStarterTemplate(template: StarterTemplate)
}