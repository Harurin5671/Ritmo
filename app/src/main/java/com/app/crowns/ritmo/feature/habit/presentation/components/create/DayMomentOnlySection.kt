package com.app.crowns.ritmo.feature.habit.presentation.components.create

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.crowns.ritmo.feature.habit.domain.model.DayMoment
import com.app.crowns.ritmo.feature.habit.presentation.create.displayName
import com.app.crowns.ritmo.ui.theme.OnSurfaceMedium
import com.app.crowns.ritmo.ui.theme.Primary
import com.app.crowns.ritmo.ui.theme.SurfaceContainerHigh

@Composable
fun DayMomentOnlySection(
    selectedMoment: DayMoment,
    onMomentSelected: (DayMoment) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionLabel("Day Moment")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
}