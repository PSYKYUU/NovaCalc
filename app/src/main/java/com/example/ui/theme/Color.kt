package com.example.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

enum class AppTheme(val displayName: String, val description: String) {
    OBSIDIAN("Dark Obsidian", "Deep midnight slate with electric cyan"),
    OLED_BLACK("OLED True Black", "Zero-power pure pitch black with emerald"),
    MIDNIGHT_NAVY("Midnight Navy", "Cosmic deep navy with sky blue"),
    CYBERPUNK("Cyber Neon", "Dark synthwave violet with neon pink"),
    TITANIUM("Titanium Slate", "Industrial dark metallic with clean indigo")
}

@Immutable
data class CalcColors(
    val bg: Color,
    val surface: Color,
    val surfaceElevated: Color,
    val border: Color,
    val displayBg: Color,
    val keyNumBg: Color,
    val keyNumText: Color,
    val keyOpBg: Color,
    val keyOpText: Color,
    val keyFnBg: Color,
    val keyFnText: Color,
    val keyVarBg: Color,
    val keyVarText: Color,
    val keyClearBg: Color,
    val keyClearText: Color,
    val keyEqualBg: Color,
    val keyEqualText: Color,
    val accentPrimary: Color,
    val accentSecondary: Color,
    val accentTertiary: Color,
    val textPrimary: Color,
    val textMuted: Color,
    val error: Color
)

val ObsidianThemeColors = CalcColors(
    bg = Color(0xFF090D16),
    surface = Color(0xFF111827),
    surfaceElevated = Color(0xFF172033),
    border = Color(0xFF1E293B),
    displayBg = Color(0xFF0C1322),
    keyNumBg = Color(0xFF182235),
    keyNumText = Color(0xFFF8FAFC),
    keyOpBg = Color(0xFF1E2C44),
    keyOpText = Color(0xFF38BDF8),
    keyFnBg = Color(0xFF1A263D),
    keyFnText = Color(0xFF818CF8),
    keyVarBg = Color(0xFF0E302A),
    keyVarText = Color(0xFF34D399),
    keyClearBg = Color(0xFF3B151E),
    keyClearText = Color(0xFFF87171),
    keyEqualBg = Color(0xFF0284C7),
    keyEqualText = Color(0xFFFFFFFF),
    accentPrimary = Color(0xFF38BDF8),
    accentSecondary = Color(0xFF818CF8),
    accentTertiary = Color(0xFF34D399),
    textPrimary = Color(0xFFF1F5F9),
    textMuted = Color(0xFF94A3B8),
    error = Color(0xFFF43F5E)
)

val OledBlackThemeColors = CalcColors(
    bg = Color(0xFF000000),
    surface = Color(0xFF0A0A0A),
    surfaceElevated = Color(0xFF141414),
    border = Color(0xFF262626),
    displayBg = Color(0xFF050505),
    keyNumBg = Color(0xFF121212),
    keyNumText = Color(0xFFFFFFFF),
    keyOpBg = Color(0xFF1C1C1C),
    keyOpText = Color(0xFF10B981),
    keyFnBg = Color(0xFF171717),
    keyFnText = Color(0xFF34D399),
    keyVarBg = Color(0xFF064E3B),
    keyVarText = Color(0xFF6EE7B7),
    keyClearBg = Color(0xFF300B0F),
    keyClearText = Color(0xFFFB7185),
    keyEqualBg = Color(0xFF059669),
    keyEqualText = Color(0xFFFFFFFF),
    accentPrimary = Color(0xFF10B981),
    accentSecondary = Color(0xFF34D399),
    accentTertiary = Color(0xFF6EE7B7),
    textPrimary = Color(0xFFFFFFFF),
    textMuted = Color(0xFFA3A3A3),
    error = Color(0xFFF43F5E)
)

val MidnightNavyThemeColors = CalcColors(
    bg = Color(0xFF070E1C),
    surface = Color(0xFF0E1A33),
    surfaceElevated = Color(0xFF15264A),
    border = Color(0xFF1D3567),
    displayBg = Color(0xFF0A1428),
    keyNumBg = Color(0xFF142447),
    keyNumText = Color(0xFFF0F6FC),
    keyOpBg = Color(0xFF1A3366),
    keyOpText = Color(0xFF60A5FA),
    keyFnBg = Color(0xFF162B54),
    keyFnText = Color(0xFF93C5FD),
    keyVarBg = Color(0xFF063F3C),
    keyVarText = Color(0xFF2DD4BF),
    keyClearBg = Color(0xFF3D1623),
    keyClearText = Color(0xFFF87171),
    keyEqualBg = Color(0xFF2563EB),
    keyEqualText = Color(0xFFFFFFFF),
    accentPrimary = Color(0xFF60A5FA),
    accentSecondary = Color(0xFF93C5FD),
    accentTertiary = Color(0xFF2DD4BF),
    textPrimary = Color(0xFFF0F6FC),
    textMuted = Color(0xFF8B9CB8),
    error = Color(0xFFF87171)
)

val CyberpunkThemeColors = CalcColors(
    bg = Color(0xFF0A071A),
    surface = Color(0xFF140E30),
    surfaceElevated = Color(0xFF1E1547),
    border = Color(0xFF2D1F6B),
    displayBg = Color(0xFF0F0B26),
    keyNumBg = Color(0xFF1A133D),
    keyNumText = Color(0xFFFAF5FF),
    keyOpBg = Color(0xFF281854),
    keyOpText = Color(0xFFF43F5E),
    keyFnBg = Color(0xFF22164A),
    keyFnText = Color(0xFFC084FC),
    keyVarBg = Color(0xFF0D3338),
    keyVarText = Color(0xFF22D3EE),
    keyClearBg = Color(0xFF3B0D24),
    keyClearText = Color(0xFFFB7185),
    keyEqualBg = Color(0xFFE11D48),
    keyEqualText = Color(0xFFFFFFFF),
    accentPrimary = Color(0xFFF43F5E),
    accentSecondary = Color(0xFFC084FC),
    accentTertiary = Color(0xFF22D3EE),
    textPrimary = Color(0xFFFAF5FF),
    textMuted = Color(0xFFA78BFA),
    error = Color(0xFFFB7185)
)

val TitaniumThemeColors = CalcColors(
    bg = Color(0xFF0F172A),
    surface = Color(0xFF1E293B),
    surfaceElevated = Color(0xFF334155),
    border = Color(0xFF475569),
    displayBg = Color(0xFF131D33),
    keyNumBg = Color(0xFF25334A),
    keyNumText = Color(0xFFF8FAFC),
    keyOpBg = Color(0xFF314361),
    keyOpText = Color(0xFF38BDF8),
    keyFnBg = Color(0xFF2C3E5B),
    keyFnText = Color(0xFF818CF8),
    keyVarBg = Color(0xFF163C38),
    keyVarText = Color(0xFF34D399),
    keyClearBg = Color(0xFF3B1C27),
    keyClearText = Color(0xFFF87171),
    keyEqualBg = Color(0xFF0EA5E9),
    keyEqualText = Color(0xFFFFFFFF),
    accentPrimary = Color(0xFF38BDF8),
    accentSecondary = Color(0xFF818CF8),
    accentTertiary = Color(0xFF34D399),
    textPrimary = Color(0xFFF8FAFC),
    textMuted = Color(0xFF94A3B8),
    error = Color(0xFFF43F5E)
)

fun getThemeColors(theme: AppTheme): CalcColors {
    return when (theme) {
        AppTheme.OBSIDIAN -> ObsidianThemeColors
        AppTheme.OLED_BLACK -> OledBlackThemeColors
        AppTheme.MIDNIGHT_NAVY -> MidnightNavyThemeColors
        AppTheme.CYBERPUNK -> CyberpunkThemeColors
        AppTheme.TITANIUM -> TitaniumThemeColors
    }
}

val LocalCalcColors = staticCompositionLocalOf { ObsidianThemeColors }

// Global static fallbacks for backwards compatibility
val DarkBg get() = ObsidianThemeColors.bg
val DarkSurface get() = ObsidianThemeColors.surface
val DarkSurfaceElevated get() = ObsidianThemeColors.surfaceElevated
val DarkBorder get() = ObsidianThemeColors.border
val DisplayBg get() = ObsidianThemeColors.displayBg
val KeyNumBg get() = ObsidianThemeColors.keyNumBg
val KeyNumText get() = ObsidianThemeColors.keyNumText
val KeyOpBg get() = ObsidianThemeColors.keyOpBg
val KeyOpText get() = ObsidianThemeColors.keyOpText
val KeyFnBg get() = ObsidianThemeColors.keyFnBg
val KeyFnText get() = ObsidianThemeColors.keyFnText
val KeyVarBg get() = ObsidianThemeColors.keyVarBg
val KeyVarText get() = ObsidianThemeColors.keyVarText
val KeyClearBg get() = ObsidianThemeColors.keyClearBg
val KeyClearText get() = ObsidianThemeColors.keyClearText
val KeyEqualBg get() = ObsidianThemeColors.keyEqualBg
val KeyEqualText get() = ObsidianThemeColors.keyEqualText
val AccentCyan get() = ObsidianThemeColors.accentPrimary
val AccentIndigo get() = ObsidianThemeColors.accentSecondary
val AccentEmerald get() = ObsidianThemeColors.accentTertiary
val AccentViolet = Color(0xFFA855F7)
val TextMuted get() = ObsidianThemeColors.textMuted
val TextLight get() = ObsidianThemeColors.textPrimary
val ErrorRed get() = ObsidianThemeColors.error
