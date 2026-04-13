package com.app.crowns.ritmo.feature.habit.presentation.utils

import androidx.compose.ui.graphics.Color
import com.app.crowns.ritmo.feature.habit.domain.model.HabitCategory
import com.app.crowns.ritmo.ui.theme.CategoryCustom
import com.app.crowns.ritmo.ui.theme.CategoryExercise
import com.app.crowns.ritmo.ui.theme.CategoryHealth
import com.app.crowns.ritmo.ui.theme.CategoryHydration
import com.app.crowns.ritmo.ui.theme.CategoryHygiene
import com.app.crowns.ritmo.ui.theme.CategoryMentalWellness
import com.app.crowns.ritmo.ui.theme.CategoryNutrition
import com.app.crowns.ritmo.ui.theme.CategorySleep

fun HabitCategory.color(): Color = when (this) {
    HabitCategory.HYGIENE -> CategoryHygiene
    HabitCategory.HYDRATION -> CategoryHydration
    HabitCategory.HEALTH -> CategoryHealth
    HabitCategory.EXERCISE -> CategoryExercise
    HabitCategory.NUTRITION -> CategoryNutrition
    HabitCategory.MENTAL_WELLNESS -> CategoryMentalWellness
    HabitCategory.SLEEP -> CategorySleep
    HabitCategory.CUSTOM -> CategoryCustom
}

fun HabitCategory.emoji(): String = when (this) {
    HabitCategory.HYGIENE -> "🪥"
    HabitCategory.HYDRATION -> "💧"
    HabitCategory.HEALTH -> "💊"
    HabitCategory.EXERCISE -> "🏃"
    HabitCategory.NUTRITION -> "🥗"
    HabitCategory.MENTAL_WELLNESS -> "🧘"
    HabitCategory.SLEEP -> "😴"
    HabitCategory.CUSTOM -> "⭐"
}