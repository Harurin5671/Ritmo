package com.app.crowns.ritmo.feature.dashboard.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.crowns.ritmo.ui.theme.SurfaceElevated

@Composable
fun DashboardStatsGrid(
    completionPercentage: Float,
    completedHabits: Int,
    totalHabits: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Completion card
        Box(
            modifier = Modifier.weight(1f)
        ) {
            DashboardCard(
                color = SurfaceElevated
            ) {
                Box(modifier = Modifier.padding(16.dp)) {
                    Column {
                        Text(
                            text = "${(completionPercentage * 100).toInt()}%",
                            style = MaterialTheme.typography.displaySmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Goal completion",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF9898A8)
                        )
                    }
                }
            }
        }
//        Surface(
//            modifier = Modifier
//                .weight(1f)
//                .height(120.dp),
//            shape = RoundedCornerShape(16.dp),
//            color = Color(0xFF22222F)
//        ) {
//
//        }

        Box(modifier = Modifier.weight(1f)) {
            DashboardCard(
                color = SurfaceElevated,
                leftBorder = LeftBorder(
                    color = Color(0xFF5DD9D0),
                )
            ) {
                Box(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = "$completedHabits / $totalHabits",
                            style = MaterialTheme.typography.displaySmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color(0xFF4ECDC4)
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Done today",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF5DD9D0)
                        )
                    }
                    // Accent bar izquierda
                    Box(
                        modifier = Modifier
                            .width(3.dp)
                            .fillMaxSize()
                            .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
                            .background(Color(0xFF4ECDC4))
                            .align(Alignment.CenterStart)
                    )
                }
            }
        }



        // Streak card
//        Surface(
//            modifier = Modifier
//                .weight(1f)
//                .height(120.dp),
//            shape = RoundedCornerShape(16.dp),
//            color = Color(0xFF22222F)
//        ) {
//
//        }
    }
}