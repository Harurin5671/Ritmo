package com.app.crowns.ritmo.feature.habit.presentation.components.create

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.app.crowns.ritmo.ui.theme.OnSurfaceDim
import com.app.crowns.ritmo.ui.theme.Primary
import com.app.crowns.ritmo.ui.theme.SurfaceContainerHigh

@Composable
fun MiniToggle(
    checked: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Switch(
        checked = checked,
        onCheckedChange = { onToggle() },
        modifier = modifier,
        colors = SwitchDefaults.colors(
            checkedThumbColor = Color.White,
            checkedTrackColor = Primary,
            uncheckedThumbColor = OnSurfaceDim,
            uncheckedTrackColor = SurfaceContainerHigh,
            uncheckedBorderColor = Color.Transparent
        )
    )
}