package com.app.crowns.ritmo.feature.dashboard.presentation.states

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.crowns.ritmo.feature.dashboard.presentation.DashboardData
import com.app.crowns.ritmo.feature.dashboard.presentation.components.DashboardHabitCard
import com.app.crowns.ritmo.feature.dashboard.presentation.components.DashboardHardDayBanner
import com.app.crowns.ritmo.feature.dashboard.presentation.components.DashboardStatsGrid
import com.app.crowns.ritmo.feature.habit.domain.model.HabitCategory

@Composable
fun DashboardSuccessState(
    modifier: Modifier = Modifier,
    data: DashboardData,
    onCompleteHabit: (Long) -> Unit,
    onSnoozeHabit: (Long, Int) -> Unit,
    onToggleHardDay: () -> Unit
) {
    val allHabits = data.morningHabits + data.afternoonHabits + data.nightHabits

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (data.isHardDayMode) {
            item {
                DashboardHardDayBanner(onTurnOff = onToggleHardDay)
            }
        }

        item {
            DashboardStatsGrid(
                completionPercentage = data.completionPercentage,
                completedHabits = data.completedHabits,
                totalHabits = data.totalHabits
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Active Habits",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "TODAY",
                    style = MaterialTheme.typography.labelSmall.copy(
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        items(items = allHabits, key = { it.id }) { habit ->
            DashboardHabitCard(
                habit = habit,
                onComplete = { onCompleteHabit(habit.id) },
                onSnooze = { onSnoozeHabit(habit.id, 30) }
            )
        }

        item { Spacer(Modifier.height(80.dp)) }
    }
}