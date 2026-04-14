package com.app.crowns.ritmo.feature.habit.presentation.components.create

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.crowns.ritmo.feature.habit.domain.model.HabitCategory
import com.app.crowns.ritmo.ui.theme.OnSurfaceMedium
import com.app.crowns.ritmo.ui.theme.Surface
import com.app.crowns.ritmo.ui.theme.TextPrimary

@Composable
fun IntervalSection(
    category: HabitCategory?,
    intervalMinutes: Int,
    dailyGoalAmount: Float,
    amountPerInterval: Float,
    dailyGoalUnit: String,
    onIntervalChanged: (Int) -> Unit,
    onGoalChanged: (Float, String) -> Unit,
    onAmountPerIntervalChanged: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Surface)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        SectionLabel("Repeat every")
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconButton(onClick = { if (intervalMinutes > 1) onIntervalChanged(intervalMinutes - 1) }) {
                Icon(Icons.Outlined.Remove, contentDescription = "Less", tint = OnSurfaceMedium)
            }

            val displayTime = when {
                intervalMinutes < 60 -> "${intervalMinutes}min"
                intervalMinutes % 60 == 0 -> "${intervalMinutes / 60}h"
                else -> "${intervalMinutes / 60}h ${intervalMinutes % 60}min"
            }

            Text(
                text = displayTime,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            )

            IconButton(onClick = { onIntervalChanged(intervalMinutes + 1) }) {
                Icon(Icons.Outlined.Add, contentDescription = "More", tint = OnSurfaceMedium)
            }
        }
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            IconButton(onClick = { if (intervalMinutes > 30) onIntervalChanged(intervalMinutes - 30) }) {
//                Icon(Icons.Outlined.Remove, contentDescription = "Less", tint = OnSurfaceMedium)
//            }
//
//            val displayTime = when {
//                intervalMinutes < 60 -> "${intervalMinutes}min"
//                intervalMinutes % 60 == 0 -> "${intervalMinutes / 60}h"
//                else -> "${intervalMinutes / 60}h ${intervalMinutes % 60}min"
//            }
//
//            Text(
//                text = displayTime,
//                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
//            )
//            IconButton(onClick = { onIntervalChanged(intervalMinutes + 30) }) {
//                Icon(Icons.Outlined.Add, contentDescription = "More", tint = OnSurfaceMedium)
//            }
//        }

        if (category == HabitCategory.HYDRATION) {
            SectionLabel("Daily goal")
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(onClick = {
                    if (dailyGoalAmount > 0.5f) onGoalChanged(
                        dailyGoalAmount - 0.5f,
                        dailyGoalUnit
                    )
                }) {
                    Icon(Icons.Outlined.Remove, contentDescription = "Less", tint = OnSurfaceMedium)
                }
                Text(
                    text = "$dailyGoalAmount $dailyGoalUnit",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                IconButton(onClick = { onGoalChanged(dailyGoalAmount + 0.5f, dailyGoalUnit) }) {
                    Icon(Icons.Outlined.Add, contentDescription = "More", tint = OnSurfaceMedium)
                }
            }

            SectionLabel("Amount per drink")
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(onClick = {
                    if (amountPerInterval > 0.1f) onAmountPerIntervalChanged(
                        amountPerInterval - 0.1f
                    )
                }) {
                    Icon(Icons.Outlined.Remove, contentDescription = "Less", tint = OnSurfaceMedium)
                }
                Text(
                    text = "${"%.1f".format(amountPerInterval)} $dailyGoalUnit",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                IconButton(onClick = { onAmountPerIntervalChanged(amountPerInterval + 0.1f) }) {
                    Icon(Icons.Outlined.Add, contentDescription = "More", tint = OnSurfaceMedium)
                }
            }
        }
    }
}

@Composable
private fun SectionLabel(label: String) {
    Text(
        text = label,
        style = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = OnSurfaceMedium
        )
    )
}