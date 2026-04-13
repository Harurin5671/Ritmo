package com.app.crowns.ritmo.feature.onboarding.presentation.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.crowns.ritmo.feature.onboarding.domain.model.StarterTemplate
import com.app.crowns.ritmo.feature.onboarding.presentation.components.OnboardingLayout
import com.app.crowns.ritmo.feature.onboarding.presentation.components.TemplateCard
import com.app.crowns.ritmo.feature.onboarding.presentation.components.TemplateEmptyCard

data class TemplateItem(
    val template: StarterTemplate,
    val title: String,
    val icons: List<Pair<ImageVector, Color>>,
    val habits: List<Pair<String, Color>>
)

@Composable
fun OnboardingTemplatePage(
    selected: StarterTemplate,
    onTemplateSelected: (StarterTemplate) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    OnboardingLayout(
        onNext = onNext
    ) {
        val templates = remember {
            listOf(
                TemplateItem(
                    template = StarterTemplate.BASIC_CARE,
                    title = "Basic Care",
                    icons = listOf(
                        Icons.Rounded.CleanHands to Color(0xFF7C6FFF),
                        Icons.Rounded.WaterDrop to Color(0xFF4ECDC4)
                    ),
                    habits = listOf(
                        "Teeth brushing" to Color(0xFF7C6FFF),
                        "Daily hydration" to Color(0xFF4ECDC4),
                        "Morning stretch" to Color(0xFF7C6FFF)
                    )
                ),
                TemplateItem(
                    template = StarterTemplate.ACTIVE_LIFESTYLE,
                    title = "Active Lifestyle",
                    icons = listOf(
                        Icons.Rounded.FitnessCenter to Color(0xFFF97316),
                        Icons.Rounded.HealthAndSafety to Color(0xFF4ADE80)
                    ),
                    habits = listOf(
                        "30min Cardio" to Color(0xFFF97316),
                        "Protein intake" to Color(0xFF4ADE80),
                        "Step count" to Color(0xFFF97316)
                    )
                ),
                TemplateItem(
                    template = StarterTemplate.STUDENT_FOCUS,
                    title = "Student Focus",
                    icons = listOf(
                        Icons.Rounded.Psychology to Color(0xFFEC4899),
                        Icons.Rounded.Bedtime to Color(0xFF6366F1)
                    ),
                    habits = listOf(
                        "Deep work block" to Color(0xFFEC4899),
                        "8h Sleep cycle" to Color(0xFF6366F1),
                        "Reading session" to Color(0xFFEC4899)
                    )
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Pick your starting\nroutine",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(32.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(templates) { item ->
                    TemplateCard(
                        title = item.title,
                        icons = item.icons,
                        habits = item.habits,
                        isSelected = selected == item.template,
                        onClick = { onTemplateSelected(item.template) },
                        modifier = Modifier.height(170.dp)
                    )
                }
                item {
                    TemplateEmptyCard(
                        isSelected = selected == StarterTemplate.START_EMPTY,
                        onClick = { onTemplateSelected(StarterTemplate.START_EMPTY) },
                        modifier = Modifier.height(170.dp)
                    )
                }
            }
        }
    }
}