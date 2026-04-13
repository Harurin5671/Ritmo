package com.app.crowns.ritmo.feature.onboarding.presentation.pages

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.app.crowns.ritmo.feature.onboarding.presentation.components.OnboardingLayout

@Composable
fun OnboardingNamePage(
    name: String,
    error: String? = null,
    onNameChanged: (String) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val isNameValid = name.trim().length >= 4
    val keyboardController = LocalSoftwareKeyboardController.current

    OnboardingLayout(
        onNext = onNext,
        isButtonEnabled = isNameValid
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { keyboardController?.hide() }
                    )
                },
        ) {
            Text(
                text = "What should we call you?",
                style = MaterialTheme.typography.headlineLarge,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Your name helps us personalize your daily rhythm.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFC8C4D7)
            )
            Spacer(Modifier.height(48.dp))
            OutlinedTextField(
                value = name,
                onValueChange = onNameChanged,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFF1B1B20),
                    unfocusedBorderColor = Color(0xFF1B1B20),
                    focusedContainerColor = Color(0xFF1B1B20).copy(alpha = 0.2f),
                    errorTextColor = MaterialTheme.colorScheme.error,
                    errorBorderColor = MaterialTheme.colorScheme.error
                ),
                isError = error != null,
                supportingText = {
                    if (error != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = null
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = error,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            )
        }
    }
}