package com.app.crowns.ritmo.feature.dashboard.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.crowns.ritmo.feature.dashboard.presentation.DashboardTab

@Composable
fun DashboardBottomBar(
    activeTab: DashboardTab,
    onTabSelected: (DashboardTab) -> Unit
) {
    val shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 24.dp,
                shape = shape,
                ambientColor = Color(0xFF7C6FFF).copy(alpha = 0.06f),
                spotColor = Color(0xFF7C6FFF).copy(alpha = 0.06f)
            )
            .clip(shape)
            .background(Color(0xFF1A1A24).copy(alpha = 0.85f))
            .drawWithContent {
                drawContent()
                drawLine(
                    color = Color(0xFFF2F2F7).copy(alpha = 0.10f),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 1.dp.toPx()
                )
            }
            .padding(vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1A1A24).copy(alpha = 0.4f)),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomBarItem(
                selected = activeTab == DashboardTab.HOME,
                icon = Icons.Default.Home,
                label = "Home",
                onClick = { onTabSelected(DashboardTab.HOME) }
            )
            BottomBarItem(
                selected = activeTab == DashboardTab.PROGRESS,
                icon = Icons.Outlined.Insights,
                label = "Progress",
                onClick = { onTabSelected(DashboardTab.PROGRESS) }
            )
            BottomBarItem(
                selected = activeTab == DashboardTab.WATER,
                icon = Icons.Outlined.WaterDrop,
                label = "Water",
                onClick = { onTabSelected(DashboardTab.WATER) }
            )
            BottomBarItem(
                selected = activeTab == DashboardTab.SETTINGS,
                icon = Icons.Outlined.Settings,
                label = "Settings",
                onClick = { onTabSelected(DashboardTab.SETTINGS) }
            )
        }
    }
}

@Composable
fun BottomBarItem(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (selected) Color(0xFF7C6FFF)
            else Color(0xFFF2F2F7).copy(alpha = 0.4f)
        )
        Text(
            label,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
            color = if (selected) Color(0xFF7C6FFF)
            else Color(0xFFF2F2F7).copy(alpha = 0.4f)
        )
        Box(
            modifier = Modifier
                .size(4.dp)
                .clip(CircleShape)
                .background(
                    if (selected) Color(0xFF7C6FFF)
                    else Color.Transparent
                )
        )
    }
}

@Preview
@Composable
fun DashboardBottomBarPreview() {
    DashboardBottomBar(
        activeTab = DashboardTab.HOME,
        onTabSelected = {}
    )
}