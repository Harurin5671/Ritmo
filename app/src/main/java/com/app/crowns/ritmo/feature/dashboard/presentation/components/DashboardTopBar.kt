package com.app.crowns.ritmo.feature.dashboard.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardTopBar(
    modifier: Modifier = Modifier,
    userName: String
) {
    TopAppBar(
        title = {
            Column() {
                Text(
                    text = "Good morning, $userName 👋🏻",
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = 20.sp),
                    letterSpacing = (-0.5).sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Saturday, April 4",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF9898A8)
                )
            }
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notification",
                    modifier = Modifier.size(30.dp)
                )
            }
        },
        modifier = modifier
    )
}