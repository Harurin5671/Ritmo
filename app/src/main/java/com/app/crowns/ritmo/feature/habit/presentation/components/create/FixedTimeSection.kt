package com.app.crowns.ritmo.feature.habit.presentation.components.create

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.crowns.ritmo.feature.habit.domain.model.DayMoment
import com.app.crowns.ritmo.feature.habit.presentation.create.displayName
import com.app.crowns.ritmo.ui.theme.OnSurfaceDim
import com.app.crowns.ritmo.ui.theme.OnSurfaceMedium
import com.app.crowns.ritmo.ui.theme.OutlineVariant
import com.app.crowns.ritmo.ui.theme.Primary
import com.app.crowns.ritmo.ui.theme.Surface
import com.app.crowns.ritmo.ui.theme.SurfaceContainerHigh
import com.app.crowns.ritmo.ui.theme.TextPrimary
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun FixedTimeSection(
    selectedMoment: DayMoment,
    onMomentSelected: (DayMoment) -> Unit,
    scheduledHour: Int,
    scheduledMinute: Int,
    onTimeChanged: (Int, Int) -> Unit,
    snoozeEnabled: Boolean,
    onSnoozeToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val timePickerDialog = android.app.TimePickerDialog(
        context,
        { _, hour, minute -> onTimeChanged(hour, minute) },
        scheduledHour,
        scheduledMinute,
        false // is24HourView
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Surface)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Time row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Schedule,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(24.dp)
                )
                Column {
                    Text(
                        text = "Pick time",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = OnSurfaceMedium
                        )
                    )
                    val timeText = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, scheduledHour)
                        set(Calendar.MINUTE, scheduledMinute)
                    }.let {
                        val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
                        format.format(it.time)
                    }
                    Text(
                        text = timeText,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                }
            }
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(SurfaceContainerHigh)
                    .clickable { timePickerDialog.show() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Edit,
                    contentDescription = "Edit time",
                    tint = Primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Day Moment
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "DAY MOMENT",
                style = TextStyle(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnSurfaceDim,
                    letterSpacing = 2.sp
                )
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DayMoment.entries.forEach { moment ->
                    val isSelected = selectedMoment == moment
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .then(
                                if (isSelected) Modifier
                                    .border(1.dp, Primary, RoundedCornerShape(50))
                                    .background(Primary.copy(alpha = 0.10f))
                                else Modifier.background(SurfaceContainerHigh)
                            )
                            .clickable { onMomentSelected(moment) }
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = moment.displayName(),
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Primary else OnSurfaceMedium
                            )
                        )
                    }
                }
            }
        }

        // Snooze toggle
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(OutlineVariant)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Snooze capability",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                )
                Text(
                    text = "Allow 5-min reminders if missed",
                    style = TextStyle(fontSize = 12.sp, color = OnSurfaceDim)
                )
            }
            Spacer(Modifier.width(12.dp))
            MiniToggle(checked = snoozeEnabled, onToggle = onSnoozeToggle)
        }
    }
}