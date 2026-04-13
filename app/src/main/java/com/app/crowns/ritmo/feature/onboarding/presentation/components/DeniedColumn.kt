package com.app.crowns.ritmo.feature.onboarding.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.app.crowns.ritmo.ui.theme.Warning

@Composable
fun DeniedColumn(
    onAllow: () -> Unit,
    warningText: String?,
    onWarningClick: (() -> Unit)?
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        if (warningText != null) {
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(Icons.Filled.Warning, null, tint = Warning, modifier = Modifier.size(14.dp))
                Text(
                    warningText, style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Medium, color = Warning,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onWarningClick?.invoke() }
                )
            }
        }
        GradientButton(text = "Allow", onClick = onAllow)
    }
}