package com.app.crowns.ritmo.feature.dashboard.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.app.crowns.ritmo.ui.theme.Surface

data class LeftBorder(
    val color: Color,
    val width: Dp = 4.dp
)

@Composable
fun DashboardCard(
    modifier: Modifier = Modifier,
    color: Color = Surface,
    cornerRadius: Dp = 20.dp,
    alignment: Alignment = Alignment.Center,
    leftBorder: LeftBorder? = null,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(cornerRadius))
            .background(color = color)
            .then(
                if (leftBorder != null) {
                    Modifier.drawWithContent {
                        drawContent()
                        drawRect(
                            color = leftBorder.color,
                            topLeft = Offset.Zero,
                            size = Size(
                                width = leftBorder.width.toPx(),
                                height = size.height
                            )
                        )
                    }
                } else Modifier
            ),
        contentAlignment = alignment
    ) {
        content()
    }
}