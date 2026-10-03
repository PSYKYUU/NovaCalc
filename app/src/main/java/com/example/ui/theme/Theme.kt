package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

@Immutable
data class CalcShapes(
    val cardRadius: Dp,
    val buttonRadius: Dp,
    val pillRadius: Dp,
    val sheetRadius: Dp
) {
    val cardShape: RoundedCornerShape get() = RoundedCornerShape(cardRadius)
    val buttonShape: RoundedCornerShape get() = RoundedCornerShape(buttonRadius)
    val pillShape: RoundedCornerShape get() = RoundedCornerShape(pillRadius)
    val sheetShape: RoundedCornerShape get() = RoundedCornerShape(topStart = sheetRadius, topEnd = sheetRadius)
}

val RoundedCalcShapes = CalcShapes(
    cardRadius = 24.dp,
    buttonRadius = 16.dp,
    pillRadius = 12.dp,
    sheetRadius = 24.dp
)

val SharpCalcShapes = CalcShapes(
    cardRadius = 2.dp,
    buttonRadius = 2.dp,
    pillRadius = 2.dp,
    sheetRadius = 2.dp
)

val LocalCalcShapes = staticCompositionLocalOf { RoundedCalcShapes }

@Composable
fun MyApplicationTheme(
    appTheme: AppTheme = AppTheme.OBSIDIAN,
    sharpCorners: Boolean = false,
    content: @Composable () -> Unit
) {
    val calcColors = getThemeColors(appTheme)
    val calcShapes = if (sharpCorners) SharpCalcShapes else RoundedCalcShapes

    val colorScheme = darkColorScheme(
        primary = calcColors.accentPrimary,
        onPrimary = calcColors.bg,
        primaryContainer = calcColors.keyOpBg,
        onPrimaryContainer = calcColors.accentPrimary,
        secondary = calcColors.accentSecondary,
        onSecondary = calcColors.bg,
        secondaryContainer = calcColors.keyFnBg,
        onSecondaryContainer = calcColors.keyFnText,
        tertiary = calcColors.accentTertiary,
        onTertiary = calcColors.bg,
        background = calcColors.bg,
        onBackground = calcColors.textPrimary,
        surface = calcColors.surface,
        onSurface = calcColors.textPrimary,
        surfaceVariant = calcColors.surfaceElevated,
        onSurfaceVariant = calcColors.textMuted,
        outline = calcColors.border,
        error = calcColors.error,
        onError = calcColors.textPrimary
    )

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = calcColors.bg.toArgb()
                window.navigationBarColor = calcColors.bg.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
            }
        }
    }

    CompositionLocalProvider(
        LocalCalcColors provides calcColors,
        LocalCalcShapes provides calcShapes
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
