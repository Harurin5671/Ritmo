package com.app.crowns.ritmo.feature.habit.presentation.components.create

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import com.app.crowns.ritmo.ui.theme.OnSurfaceDim
import com.app.crowns.ritmo.ui.theme.OnSurfaceMedium
import com.app.crowns.ritmo.ui.theme.Primary
import com.app.crowns.ritmo.ui.theme.PrimaryContainerHigh
import com.app.crowns.ritmo.ui.theme.SurfaceContainerHigh
import com.app.crowns.ritmo.ui.theme.SurfaceElevated
import com.app.crowns.ritmo.ui.theme.TextPrimary

@Composable
fun ActiveDaysSection(
    activeDays: Set<Int>,
    onDayToggled: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val days = listOf(1 to "M", 2 to "T", 3 to "W", 4 to "T", 5 to "F", 6 to "S", 7 to "S")
    val allSelected = activeDays.size == 7
    val weekdaysSelected = activeDays == setOf(1, 2, 3, 4, 5)

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Header row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SectionLabel("Active Days")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Weekdays quick-select
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(if (weekdaysSelected) Primary else SurfaceContainerHigh)
                        .clickable { (1..5).forEach { onDayToggled(it) } }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "WEEKDAYS",
                        style = TextStyle(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                            color = if (weekdaysSelected) TextPrimary else OnSurfaceMedium
                        )
                    )
                }
                // All days quick-select
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(if (allSelected) Primary else SurfaceContainerHigh)
                        .clickable { (1..7).forEach { onDayToggled(it) } }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "ALL DAYS",
                        style = TextStyle(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                            color = if (allSelected) TextPrimary else OnSurfaceMedium
                        )
                    )
                }
            }
        }

        // Day circles
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            days.forEach { (day, label) ->
                val isActive = day in activeDays
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .then(
                            if (isActive) Modifier.shadow(
                                8.dp,
                                CircleShape,
                                spotColor = PrimaryContainerHigh.copy(alpha = 0.3f)
                            ) else Modifier
                        )
                        .clip(CircleShape)
                        .background(if (isActive) PrimaryContainerHigh else SurfaceElevated)
                        .clickable { onDayToggled(day) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isActive) TextPrimary else OnSurfaceDim
                        )
                    )
                }
            }
        }
    }
}