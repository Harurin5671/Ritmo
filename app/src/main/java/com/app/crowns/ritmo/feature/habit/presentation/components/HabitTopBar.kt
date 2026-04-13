package com.app.crowns.ritmo.feature.habit.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitTopBar(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onSave: () -> Unit,
    isSaveEnabled: Boolean = true,
    isSaving: Boolean = false,
) {
    TopAppBar(
        navigationIcon = {
            IconButton(
                onClick = onBack
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                )
            }
        },
        title = { Text("New Habit", style = MaterialTheme.typography.headlineSmall) },
        actions = {
            if (isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(20.dp)
                        .padding(end = 16.dp),
                    strokeWidth = 2.dp,
                    color = Color(0xFF7C6FFF)
                )
            } else {
                TextButton(
                    onClick = onSave,
                    enabled = isSaveEnabled
                ) {
                    Text(
                        text = "Save",
                        color = if (isSaveEnabled) Color(0xFF7C6FFF)
                        else Color(0xFFF2F2F7).copy(alpha = 0.3f)
                    )
                }
            }
        },
        modifier = modifier
    )
}