package com.app.crowns.ritmo.feature.habit.presentation.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.crowns.ritmo.feature.habit.domain.model.DayMoment
import com.app.crowns.ritmo.feature.habit.domain.model.HabitCategory
import com.app.crowns.ritmo.feature.habit.domain.model.HabitType
import com.app.crowns.ritmo.feature.habit.presentation.components.HabitTopBar
import com.app.crowns.ritmo.feature.habit.presentation.components.create.ActiveDaysSection
import com.app.crowns.ritmo.feature.habit.presentation.components.create.CategorySection
import com.app.crowns.ritmo.feature.habit.presentation.components.create.DayMomentOnlySection
import com.app.crowns.ritmo.feature.habit.presentation.components.create.EstimatedTimeCard
import com.app.crowns.ritmo.feature.habit.presentation.components.create.FixedTimeSection
import com.app.crowns.ritmo.feature.habit.presentation.components.create.HabitNameInput
import com.app.crowns.ritmo.feature.habit.presentation.components.create.HabitTypeSection
import com.app.crowns.ritmo.feature.habit.presentation.components.create.HardDayCard
import com.app.crowns.ritmo.feature.habit.presentation.components.create.IntervalSection
import com.app.crowns.ritmo.feature.habit.presentation.components.create.LinkedHabitsRow

private val Primary = Color(0xFF7C6FFF)
private val PrimaryContainer = Color(0xFF3D3580)
private val OnPrimary = Color.White

private val DarkBg = Color(0xFF22222F)
private val Secondary = Color(0xFFFFD93D)

// ─── Category colors (matching HTML) ─────────────────────────────────────────
fun HabitCategory.color(): Color = when (this) {
    HabitCategory.HYGIENE -> Color(0xFF7C6FFF)
    HabitCategory.HYDRATION -> Color(0xFF4ECDC4)
    HabitCategory.HEALTH -> Color(0xFFFF6B6B)
    HabitCategory.EXERCISE -> Color(0xFFF97316)
    HabitCategory.NUTRITION -> Color(0xFFFFD93D)
    HabitCategory.MENTAL_WELLNESS -> Color(0xFF6BCB77)
    HabitCategory.SLEEP -> Color(0xFF6366F1)
    HabitCategory.CUSTOM -> Color(0xFF9898A8)
}

fun HabitCategory.displayName(): String = when (this) {
    HabitCategory.HYGIENE -> "Hygiene"
    HabitCategory.HYDRATION -> "Hydration"
    HabitCategory.HEALTH -> "Health"
    HabitCategory.EXERCISE -> "Exercise"
    HabitCategory.NUTRITION -> "Nutrition"
    HabitCategory.MENTAL_WELLNESS -> "Mind"
    HabitCategory.SLEEP -> "Sleep"
    HabitCategory.CUSTOM -> "Custom"
}

fun HabitType.displayName(): String = when (this) {
    HabitType.FIXED -> "Fixed time"
    HabitType.INTERVAL -> "Interval"
    HabitType.FREE -> "Free"
}

fun DayMoment.displayName(): String = when (this) {
    DayMoment.MORNING -> "Breakfast"
    DayMoment.AFTERNOON -> "Lunch"
    DayMoment.NIGHT -> "Dinner"
    DayMoment.ANYTIME -> "None"
}

// ─── Main Screen ─────────────────────────────────────────────────────────────

@Composable
fun CreateHabitScreen(
    onBack: () -> Unit,
    onHabitCreated: () -> Unit,
    viewModel: CreateHabitViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is CreateHabitEvent.HabitSaved -> onHabitCreated()
            }
        }
    }

    LaunchedEffect(state.saveError) {
        state.saveError?.let { snackBarHostState.showSnackbar(it) }
    }

    Scaffold(
        topBar = {
            HabitTopBar(
                onBack = onBack,
                onSave = { viewModel.onSave() },
                isSaveEnabled = state.canSave,
                isSaving = state.isSaving
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) },
        containerColor = Color(0xFF13131D)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp, bottom = 128.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // 1. Habit Name
            HabitNameInput(
                value = state.name,
                onValueChange = viewModel::onNameChanged,
                error = state.nameError
            )

            // 2. Category Selector
            CategorySection(
                selected = state.selectedCategory,
                error = state.categoryError,
                onCategorySelected = viewModel::onCategorySelected
            )

            // 3. Habit Type
            HabitTypeSection(
                selected = state.selectedType,
                onTypeSelected = viewModel::onTypeSelected
            )

            // 4. Conditional section
            when (state.selectedType) {
                HabitType.FIXED -> {
                    FixedTimeSection(
                        selectedMoment = state.selectedDayMoment,
                        onMomentSelected = viewModel::onDayMomentSelected,
                        scheduledHour = state.scheduledHour ?: 8,
                        scheduledMinute = state.scheduledMinute ?: 0,
                        onTimeChanged = viewModel::onScheduledTimeChanged,
                        snoozeEnabled = state.snoozeEnabled,
                        onSnoozeToggle = viewModel::onSnoozeToggled
                    )
                }

                HabitType.FREE -> {
                    // Free only shows day moment
                    DayMomentOnlySection(
                        selectedMoment = state.selectedDayMoment,
                        onMomentSelected = viewModel::onDayMomentSelected
                    )
                }

                HabitType.INTERVAL -> {
                    IntervalSection(
                        category = state.selectedCategory,
                        intervalMinutes = state.intervalMinutes ?: 60,
                        dailyGoalAmount = state.dailyGoalAmount ?: 3f,
                        amountPerInterval = state.amountPerInterval ?: 0.5f,
                        dailyGoalUnit = state.dailyGoalUnit,
                        onIntervalChanged = viewModel::onIntervalMinutesChanged,
                        onGoalChanged = viewModel::onDailyGoalChanged,
                        onAmountPerIntervalChanged = viewModel::onAmountPerIntervalChanged
                    )
                }
            }

            // 5. Active Days
            ActiveDaysSection(
                activeDays = state.activeDays,
                onDayToggled = viewModel::onActiveDayToggled
            )

            // 6. Estimated Duration
            EstimatedTimeCard(
                minutes = state.estimatedMinutes,
                onMinutesChanged = viewModel::onEstimatedMinutesChanged
            )

            // 7. Linked Habits
            LinkedHabitsRow()

            // 8. Hard Day Mode
            HardDayCard(
                enabled = state.isHardDayIncluded,
                onToggle = { viewModel.onHardDayToggled() }
            )
        }
    }
}
