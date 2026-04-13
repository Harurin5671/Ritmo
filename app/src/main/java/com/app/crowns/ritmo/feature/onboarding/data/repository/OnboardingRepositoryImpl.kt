package com.app.crowns.ritmo.feature.onboarding.data.repository

import com.app.crowns.ritmo.core.datastore.UserPreferencesDataStore
import com.app.crowns.ritmo.core.logging.AppLogger
import com.app.crowns.ritmo.feature.onboarding.domain.model.StarterTemplate
import com.app.crowns.ritmo.feature.onboarding.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val dataStore: UserPreferencesDataStore,
    private val logger: AppLogger
) : OnboardingRepository {
    override val isOnboardingCompleted: Flow<Boolean> =
        dataStore.isOnboardingCompleted
    override val userName: Flow<String> =
        dataStore.userName

    override suspend fun completeOnboarding() = dataStore.setOnboardingCompleted()

    override suspend fun saveUserName(name: String) = dataStore.saveUserName(name)

    override suspend fun saveStarterTemplate(template: StarterTemplate) =
        dataStore.saveStarterTemplate(template.name)

}