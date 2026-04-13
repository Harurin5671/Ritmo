package com.app.crowns.ritmo.feature.habit.presentation.components.create

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.Link
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.crowns.ritmo.ui.theme.OnSurfaceDim
import com.app.crowns.ritmo.ui.theme.Surface
import com.app.crowns.ritmo.ui.theme.TextPrimary

@Composable
fun LinkedHabitsRow(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Surface)
            .clickable { /* expand / navigate */ }
            .padding(horizontal = 24.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Link,
                contentDescription = null,
                tint = OnSurfaceDim,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "Linked Habits",
                style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            )
        }
        Icon(
            imageVector = Icons.Rounded.ExpandMore,
            contentDescription = "Expand",
            tint = OnSurfaceDim,
            modifier = Modifier.size(24.dp)
        )
    }
}