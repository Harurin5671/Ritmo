package com.app.crowns.ritmo.feature.onboarding.domain.usecase

import com.app.crowns.ritmo.feature.onboarding.domain.model.StarterTemplate
import com.app.crowns.ritmo.feature.onboarding.domain.repository.OnboardingRepository
import javax.inject.Inject

class CompleteOnboardingUseCase @Inject constructor(
    private val repository: OnboardingRepository
) {
    suspend operator fun invoke(name: String, template: StarterTemplate) {
        repository.saveUserName(name)
        repository.saveStarterTemplate(template)
        repository.completeOnboarding()
    }
}