package com.app.crowns.ritmo.feature.onboarding.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.crowns.ritmo.ui.components.AppCard

private val TextSecondary = Color(0xFFC8C4D7)

@Composable
fun PermissionCard(
    icon: ImageVector,
    title: String,
    description: String,
    isGranted: Boolean,
    accentColor: Color,
    onAllow: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = Color(0xFF22222F),
    warningText: String? = null,
    onWarningClick: (() -> Unit)? = null
) {
    AppCard(modifier = modifier, containerColor = containerColor) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = accentColor.copy(alpha = 0.10f),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = accentColor, modifier = Modifier.size(28.dp))
            }
            Column(Modifier
                .weight(1f)
                .padding(top = 4.dp)) {
                Text(
                    title, style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold, color = Color.White
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    description, style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary, lineHeight = 18.sp
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        if (isGranted) GrantedRow(accentColor) else DeniedColumn(
            onAllow,
            warningText,
            onWarningClick
        )
    }
}