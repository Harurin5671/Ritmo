package com.app.crowns.ritmo.feature.habit.domain.model

enum class HabitType {
    FIXED,      // Evento único ligado a un momento del día
    INTERVAL,   // Recurrente cada X tiempo (agua)
    FREE        // Hora fija simple
}