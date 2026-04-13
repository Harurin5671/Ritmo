package com.app.crowns.ritmo.feature.onboarding.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OnboardingLayout(
    modifier: Modifier = Modifier,
    buttonText: String = "Continue",
    onNext: () -> Unit,
    isButtonEnabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            content()
        }
        Button(
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth(),
            onClick = onNext,
            enabled = isButtonEnabled
        ) {
            Text(text = buttonText, style = MaterialTheme.typography.titleLarge)
        }
    }
}