package com.app.crowns.ritmo.feature.onboarding.domain.usecase

import com.app.crowns.ritmo.feature.onboarding.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsOnboardingCompletedUseCase @Inject constructor(
    private val repository: OnboardingRepository
) {
    operator fun invoke(): Flow<Boolean> = repository.isOnboardingCompleted
}