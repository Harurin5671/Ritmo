package com.app.crowns.ritmo.ui.theme

import androidx.compose.ui.graphics.Color

// ── Surfaces ──────────────────────────────────────
val Background      = Color(0xFF0F0F14)
val Surface         = Color(0xFF1A1A24)

val SurfaceContainerHigh = Color(0xFF2A2A3C)
val SurfaceContainerHighest = Color(0xFF33334A)
val SurfaceElevated = Color(0xFF22222F)

// ── Brand ─────────────────────────────────────────
val Primary          = Color(0xFF7C6FFF)
val PrimaryContainer = Color(0xFF2D2850)
val PrimaryContainerHigh = Color(0xFF3D3580)
val Secondary        = Color(0xFF4ECDC4)
val SecondaryContainer = Color(0xFF1A3A38)
val Accent           = Color(0xFFA78BFA)

// ── Semantic ──────────────────────────────────────
val Success = Color(0xFF4ADE80)
val Warning = Color(0xFFFBBF24)
val Error   = Color(0xFFF87171)

// ── Text ──────────────────────────────────────────
val TextPrimary   = Color(0xFFF2F2F7)
val TextSecondary = Color(0xFF9898A8)
val TextTertiary  = Color(0xFF5A5A6E)

// ── OnSurface ─────────────────────────────────────────
val OnSurfaceDim = TextPrimary.copy(alpha = 0.40f)
val OnSurfaceMedium = TextPrimary.copy(alpha = 0.60f)

// ── Variants ─────────────────────────────────────────
val OutlineVariant = Color(0xFFFFFFFF).copy(alpha = 0.06f)

// ── Category colors ───────────────────────────────
val CategoryHygiene       = Color(0xFF7C6FFF)
val CategoryHydration     = Color(0xFF4ECDC4)
val CategoryHealth        = Color(0xFF4ADE80)
val CategoryExercise      = Color(0xFFF97316)
val CategoryNutrition     = Color(0xFFFBBF24)
val CategoryMentalWellness = Color(0xFFEC4899)
val CategorySleep         = Color(0xFF6366F1)
val CategoryCustom        = Color(0xFF9898A8)