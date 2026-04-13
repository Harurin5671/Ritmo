package com.app.crowns.ritmo.feature.onboarding.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.crowns.ritmo.feature.onboarding.presentation.pages.OnboardingNamePage
import com.app.crowns.ritmo.feature.onboarding.presentation.pages.OnboardingPermissionsPage
import com.app.crowns.ritmo.feature.onboarding.presentation.pages.OnboardingTemplatePage
import com.app.crowns.ritmo.feature.onboarding.presentation.pages.OnboardingWelcomePage

@Composable
fun OnboardingScreen(
    onFinished: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(pageCount = { 4 })

    LaunchedEffect(state.currentPage) {
        pagerState.animateScrollToPage(state.currentPage)
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            if (page != state.currentPage) {
                // S
                if (page < state.currentPage || state.userName.isNotBlank()) {
                    viewModel.onPageChanged(page)
                } else {
                    pagerState.animateScrollToPage(state.currentPage)
                }
            }
        }
    }

    LaunchedEffect(state.isCompleted) {
        if (state.isCompleted) onFinished()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            PageIndicator(
                pageCount = 4,
                currentPage = state.currentPage,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 24.dp)
            )
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f),
//                    .padding(24.dp),
                userScrollEnabled = false
            ) { page ->
                when (page) {
                    0 -> OnboardingWelcomePage(
                        onNext = { viewModel.onNextPage() }
                    )

                    1 -> OnboardingNamePage(
                        name = state.userName,
                        error = state.userNameError,
                        onNameChanged = viewModel::onNameChanged,
                        onNext = { viewModel.onNextPage() },
                        onBack = { viewModel.onPreviousPage() }
                    )

                    2 -> OnboardingTemplatePage(
                        selected = state.selectedTemplate,
                        onTemplateSelected = viewModel::onTemplateSelected,
                        onNext = { viewModel.onNextPage() },
                        onBack = { viewModel.onPreviousPage() }
                    )

                    3 -> OnboardingPermissionsPage(
                       notificationsGranted = state.notificationsGranted,
                        exactAlarmsGranted = state.exactAlarmsGranted,
                        notificationDeniedOnce = state.notificationPermissionDeniedOnce,
                        onPermissionResult = { granted -> viewModel.onPermissionResult(granted) },
                        onRefreshPermissions = viewModel::refreshPermissions,
                        onFinish = { viewModel.onFinish() },
                        onBack = { viewModel.onPreviousPage() },
                        isSaving = state.isSaving,
                        isButtonEnabled = state.canFinish
                    )
                }
            }
        }
    }
}

@Composable
fun PageIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            val isActive = index == currentPage
            val width by animateDpAsState(
                targetValue = if (isActive) 24.dp else 8.dp,
                animationSpec = tween(300),
                label = "indicator_width"
            )
            val color by animateColorAsState(
                targetValue = if (isActive) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                },
                animationSpec = tween(300),
                label = "indicator_color"
            )
            Box(
                modifier = Modifier
                    .height(8.dp)
                    .width(width)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}