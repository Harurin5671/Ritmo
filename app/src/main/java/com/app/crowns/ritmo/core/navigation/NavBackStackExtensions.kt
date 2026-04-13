package com.app.crowns.ritmo.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.navigation3.runtime.NavBackStack
import kotlinx.serialization.serializer

@Composable
fun rememberAppNavBackStack(vararg elements: AppDestination): NavBackStack<AppDestination> {
    return rememberSerializable(serializer = serializer()) {
        NavBackStack(*elements)
    }
}

fun NavBackStack<AppDestination>.navigateTo(destination: AppDestination) {
    add(destination)
}

fun NavBackStack<AppDestination>.back() {
    if (isEmpty()) return
    removeLastOrNull()
}

fun NavBackStack<AppDestination>.backTo(target: AppDestination) {
    if (isEmpty()) return
    if (target !in this) return
    while (isNotEmpty() && last() != target) {
        removeLastOrNull()
    }
}

fun NavBackStack<AppDestination>.clearAndNavigateTo(destination: AppDestination) {
    clear()
    add(destination)
}