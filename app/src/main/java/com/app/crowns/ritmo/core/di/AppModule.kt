package com.app.crowns.ritmo.core.di

import com.app.crowns.ritmo.feature.habit.data.repository.HabitRepositoryImpl
import com.app.crowns.ritmo.feature.habit.domain.repository.HabitRepository
import com.app.crowns.ritmo.feature.onboarding.data.repository.OnboardingRepositoryImpl
import com.app.crowns.ritmo.feature.onboarding.domain.repository.OnboardingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun bindOnboardingRepository(
        impl: OnboardingRepositoryImpl
    ): OnboardingRepository

    @Binds
    abstract fun bindHabitRepository(
        impl: HabitRepositoryImpl
    ): HabitRepository
}