package com.app.crowns.ritmo.feature.habit.presentation.components.create

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.crowns.ritmo.feature.habit.domain.model.HabitType
import com.app.crowns.ritmo.feature.habit.presentation.create.displayName
import com.app.crowns.ritmo.ui.theme.OnSurfaceMedium
import com.app.crowns.ritmo.ui.theme.PrimaryContainerHigh
import com.app.crowns.ritmo.ui.theme.SurfaceElevated
import com.app.crowns.ritmo.ui.theme.TextPrimary

@Composable
fun HabitTypeSection(
    selected: HabitType,
    onTypeSelected: (HabitType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SectionLabel("Habit Type")

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(SurfaceElevated)
                .padding(6.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                HabitType.entries.forEach { type ->
                    val isSelected = selected == type
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(22.dp))
                            .then(
                                if (isSelected) Modifier
                                    .shadow(8.dp, RoundedCornerShape(22.dp), spotColor = PrimaryContainerHigh.copy(alpha = 0.3f))
                                    .background(PrimaryContainerHigh)
                                else Modifier
                            )
                            .clickable { onTypeSelected(type) }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = type.displayName(),
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) TextPrimary else OnSurfaceMedium
                            )
                        )
                    }
                }
            }
        }
    }
}