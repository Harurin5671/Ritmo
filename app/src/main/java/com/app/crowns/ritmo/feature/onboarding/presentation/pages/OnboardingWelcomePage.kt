package com.app.crowns.ritmo.feature.onboarding.presentation.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.crowns.ritmo.feature.onboarding.presentation.components.OnboardingLayout
import com.app.crowns.ritmo.ui.theme.TextSecondary

@Composable
fun OnboardingWelcomePage(
    onNext: () -> Unit,
) {
    OnboardingLayout(
        onNext = onNext,
        buttonText = "Get Started"
    ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Build habits that stick",
                    style = MaterialTheme.typography.headlineLarge,
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Ritmo helps you track daily routines with\n" +
                            "smart reminders and progress insights.",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
            }
    }
}