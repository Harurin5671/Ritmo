package com.app.crowns.ritmo.feature.habit.presentation.components.create

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.crowns.ritmo.ui.theme.OnSurfaceDim
import com.app.crowns.ritmo.ui.theme.Primary
import com.app.crowns.ritmo.ui.theme.TextPrimary

@Composable
fun HabitNameInput(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    error: String? = null,
    maxLength: Int = 50
) {
    val textStyle = TextStyle(
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        color = TextPrimary
    )

    Column(modifier = modifier) {
        BasicTextField(
            value = value,
            onValueChange = { if (it.length <= maxLength) onValueChange(it) },
            textStyle = textStyle,
            cursorBrush = SolidColor(Primary),
            singleLine = true,
            decorationBox = { innerTextField ->
                Box {
                    if (value.isEmpty()) {
                        Text(
                            text = "What's this habit called?",
                            style = textStyle.copy(
                                color = TextPrimary.copy(alpha = 0.20f)
                            )
                        )
                    }
                    innerTextField()
                }
            }
        )

        // Counter
        Text(
            text = "${value.length} / $maxLength",
            style = TextStyle(
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = OnSurfaceDim,
                letterSpacing = 2.sp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            textAlign = TextAlign.End
        )

        if (error != null) {
            Text(
                text = error,
                color = Color(0xFFF87171),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}