package com.app.crowns.ritmo.feature.dashboard.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.crowns.ritmo.feature.habit.domain.model.Habit
import com.app.crowns.ritmo.feature.habit.domain.model.HabitStatus
import com.app.crowns.ritmo.feature.habit.presentation.create.color
import com.app.crowns.ritmo.feature.habit.presentation.utils.emoji

@Composable
fun DashboardHabitCard(
    habit: Habit,
    onComplete: () -> Unit,
    onSnooze: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryColor = habit.category.color()
    val isCompleted = habit.status == HabitStatus.COMPLETED
    val isSnoozed = habit.isSnoozed
    val lefData = LeftBorder(
        color = categoryColor
    )

    DashboardCard(
        modifier = modifier.fillMaxWidth(),
        leftBorder = lefData
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Ícono de categoría
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(categoryColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = habit.category.emoji(),
                    fontSize = 20.sp
                )
            }

            Spacer(Modifier.width(12.dp))

            // Nombre y estado
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = habit.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = if (isCompleted) Color(0xFF9898A8) else Color(0xFFF2F2F7)
                )
                Spacer(Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Status chip
                    StatusChip(status = habit.status, isSnoozed = isSnoozed)

                    // Hora
//                    if (habit.scheduledHour != null) {
//                        Text(
//                            text = formatTime(habit.scheduledHour, habit.scheduledMinute ?: 0),
//                            style = MaterialTheme.typography.labelSmall,
//                            color = Color(0xFF9898A8)
//                        )
//                    }
                }
            }

            // Botón de completar
            if (!isCompleted) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF22222F)),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = onComplete) {
                        Icon(
                            imageVector = Icons.Rounded.Check,
                            contentDescription = "Complete",
                            tint = Color(0xFF9898A8),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF4ADE80).copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = null,
                        tint = Color(0xFF4ADE80),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }

//    Surface(
//        modifier = modifier.fillMaxWidth(),
//        shape = RoundedCornerShape(16.dp),
//        color = Color(0xFF1A1A24)
//    ) {
//
//    }
}

@Composable
fun StatusChip(
    status: HabitStatus,
    isSnoozed: Boolean,
    modifier: Modifier = Modifier
) {
    val (label, bgColor, textColor) = when {
        isSnoozed -> Triple("Snoozed", Color(0xFF2D2850), Color(0xFF7C6FFF))
        status == HabitStatus.COMPLETED -> Triple("Done", Color(0xFF1A3A2A), Color(0xFF4ADE80))
        status == HabitStatus.MISSED -> Triple("Missed", Color(0xFF3A1A1A), Color(0xFFF87171))
        else -> Triple("Pending", Color(0xFF2A2A1A), Color(0xFFFBBF24))
    }

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = bgColor,
        modifier = modifier
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = textColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}
