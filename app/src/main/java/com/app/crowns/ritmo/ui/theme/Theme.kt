package com.app.crowns.ritmo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val RitmoDarkColorScheme = darkColorScheme(
    // ── Brand ────────────────────────────────────
    primary            = Primary,
    onPrimary          = TextPrimary,
    primaryContainer   = PrimaryContainer,
    onPrimaryContainer = Accent,

    secondary            = Secondary,
    onSecondary          = Background,
    secondaryContainer   = SecondaryContainer,
    onSecondaryContainer = Secondary,

    // ── Surfaces ─────────────────────────────────
    background          = Background,
    onBackground        = TextPrimary,
    surface             = Surface,
    onSurface           = TextPrimary,
    surfaceVariant      = SurfaceElevated,
    onSurfaceVariant    = TextSecondary,

    // ── Semantic ─────────────────────────────────
    error   = Error,
    onError = TextPrimary,

    // ── Misc ─────────────────────────────────────
    outline        = TextTertiary,
    outlineVariant = TextTertiary.copy(alpha = 0.15f)
)

//private val DarkColorScheme = darkColorScheme(
//    primary = Purple80,
//    secondary = PurpleGrey80,
//    tertiary = Pink80
//)
//
//private val LightColorScheme = lightColorScheme(
//    primary = Purple40,
//    secondary = PurpleGrey40,
//    tertiary = Pink40
//
//    /* Other default colors to override
//    background = Color(0xFFFFFBFE),
//    surface = Color(0xFFFFFBFE),
//    onPrimary = Color.White,
//    onSecondary = Color.White,
//    onTertiary = Color.White,
//    onBackground = Color(0xFF1C1B1F),
//    onSurface = Color(0xFF1C1B1F),
//    */
//)

@Composable
fun RitmoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
//    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
//
//        darkTheme -> DarkColorScheme
//        else -> LightColorScheme
//    }

    MaterialTheme(
        colorScheme = RitmoDarkColorScheme,
        typography = RitmoTypography,
        content = content
    )
}