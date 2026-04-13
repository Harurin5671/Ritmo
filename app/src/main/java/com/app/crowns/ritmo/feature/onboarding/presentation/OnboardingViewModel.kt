package com.app.crowns.ritmo.feature.onboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.crowns.ritmo.core.logging.AppLogger
import com.app.crowns.ritmo.core.permission.PermissionManager
import com.app.crowns.ritmo.feature.onboarding.domain.model.StarterTemplate
import com.app.crowns.ritmo.feature.onboarding.domain.usecase.CompleteOnboardingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val completeOnboardingUseCase: CompleteOnboardingUseCase,
    private val permissionManager: PermissionManager,
    private val logger: AppLogger
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            permissionManager.notificationsGranted.collect { granted ->
                _uiState.update { it.copy(notificationsGranted = granted) }
            }
        }

        viewModelScope.launch {
            permissionManager.exactAlarmsGranted.collect { granted ->
                _uiState.update { it.copy(exactAlarmsGranted = granted) }
            }
        }
    }

    fun onPermissionsPageReached() {
        permissionManager.checkPermissions()
    }

    fun onPermissionResult(granted: Boolean) {
        if (!granted) _uiState.update { it.copy(notificationPermissionDeniedOnce = true) }
        permissionManager.checkPermissions()
    }

    fun refreshPermissions() {
        permissionManager.checkPermissions()
    }

    fun onNameChanged(value: String) {
        val filtered = value.filter { it.isLetter() || it.isWhitespace() }
        logger.i("Name: $filtered")
        _uiState.update {
            it.copy(userName = filtered, userNameError = null)
        }
    }

    fun onTemplateSelected(template: StarterTemplate) {
        logger.i("Template: $template")
        _uiState.update { it.copy(selectedTemplate = template) }
    }

    fun onPageChanged(page: Int) {
        _uiState.update { it.copy(currentPage = page) }
    }

    fun onNextPage() {
        val current = _uiState.value
        if (current.currentPage == 1) {
            val error = validateName(current.userName)
            if (error != null) {
                _uiState.update { it.copy(userNameError = error) }
                return
            }
        }
        logger.i("Current State: $current")
        _uiState.update { it.copy(currentPage = it.currentPage + 1) }
    }

    fun onPreviousPage() {
        if (_uiState.value.currentPage > 0) {
            _uiState.update { it.copy(currentPage = it.currentPage - 1) }
        }
    }

    fun onFinish() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            try {
                val current = _uiState.value
                completeOnboardingUseCase(
                    name = current.userName,
                    template = current.selectedTemplate,

                )
                logger.i("Onboarding completed successfully: ${current.userName}")
                _uiState.update { it.copy(isSaving = false, isCompleted = true) }
            } catch (e: Exception) {
                logger.e(throwable = e, message = "Error completing onboarding")
                _uiState.update { it.copy(isSaving = false) }
            }
        }
    }

    private fun validateName(name: String): String? {
        return when {
            name.isBlank() -> "Please enter your name"
            name.trim().length < 4 -> "Name must be at least 4 characters"
            name.any { it.isDigit() } -> "Name cannot contain numbers"
            else -> null
        }
    }
}