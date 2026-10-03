package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ActiveModal
import com.example.ui.CalculatorUiState
import com.example.ui.NumberDisplayFormat
import com.example.ui.theme.AppTheme
import com.example.ui.theme.CalcColors
import com.example.ui.theme.LocalCalcColors
import com.example.ui.theme.LocalCalcShapes
import com.example.ui.theme.getThemeColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsSheet(
    uiState: CalculatorUiState,
    historyCount: Int,
    variablesCount: Int,
    onSelectTheme: (AppTheme) -> Unit,
    onToggleSharpCorners: (Boolean) -> Unit,
    onToggleQuickVariables: (Boolean) -> Unit,
    onToggleHaptics: (Boolean) -> Unit,
    onSelectPrecision: (Int) -> Unit,
    onToggleAngle: () -> Unit,
    onCycleFormat: () -> Unit,
    onOpenModal: (ActiveModal) -> Unit,
    onClearAllData: () -> Unit,
    onExportHistory: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val colors = LocalCalcColors.current
    val shapes = LocalCalcShapes.current
    val scrollState = rememberScrollState()
    val themeScrollState = rememberScrollState()
    var showClearDataConfirm by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = colors.surface,
        shape = shapes.sheetShape,
        dragHandle = { BottomSheetDefaults.DragHandle(color = colors.textMuted) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .padding(horizontal = 20.dp)
                .padding(bottom = 20.dp)
                .testTag("settings_sheet")
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    Surface(
                        shape = shapes.pillShape,
                        color = colors.keyOpBg,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = null,
                                tint = colors.accentPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Settings",
                            color = colors.textPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Preferences, Appearance and System",
                            color = colors.textMuted,
                            fontSize = 11.sp,
                            maxLines = 1
                        )
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = colors.textMuted)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ==========================================
                // 1. Theme Visual Showcase & Corner Styling
                // ==========================================
                SectionCard(
                    title = "Themes & Visual Style",
                    icon = Icons.Default.Palette
                ) {
                    Text(
                        text = "Visual Mockups (Tap to apply theme live):",
                        color = colors.textMuted,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    // Horizontal Scrollable Visual Screenshot Mockup Cards
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(themeScrollState),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AppTheme.values().forEach { theme ->
                            ThemeScreenshotCard(
                                theme = theme,
                                isSelected = uiState.appTheme == theme,
                                onClick = { onSelectTheme(theme) }
                            )
                        }
                    }

                    Divider(color = colors.border.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 14.dp))

                    // Corner Styling Toggle (Rounded vs Sharp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        ) {
                            Text(
                                text = "Corner Geometry",
                                color = colors.textPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = if (uiState.sharpCorners) "Brutalist crisp edges (0-2dp)" else "Modern curved borders (16-24dp)",
                                color = colors.textMuted,
                                fontSize = 12.sp
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ChoicePill(
                                label = "Curved",
                                isSelected = !uiState.sharpCorners,
                                onClick = { onToggleSharpCorners(false) }
                            )
                            ChoicePill(
                                label = "Sharp",
                                isSelected = uiState.sharpCorners,
                                onClick = { onToggleSharpCorners(true) }
                            )
                        }
                    }
                }

                // ==========================================
                // 2. Calculator Engine & Layout Options
                // ==========================================
                SectionCard(
                    title = "Engine & Display Preferences",
                    icon = Icons.Default.Calculate
                ) {
                    // Quick Variables Bar Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                            Text(
                                text = "Quick Variables Bar",
                                color = colors.textPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Display 1-tap variable chips bar directly above keypad",
                                color = colors.textMuted,
                                fontSize = 12.sp
                            )
                        }
                        Switch(
                            checked = uiState.showQuickVariables,
                            onCheckedChange = onToggleQuickVariables,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = colors.accentTertiary,
                                checkedTrackColor = colors.keyVarBg,
                                uncheckedThumbColor = colors.textMuted,
                                uncheckedTrackColor = colors.surfaceElevated
                            )
                        )
                    }

                    Divider(color = colors.border.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 12.dp))

                    // Angle Unit (DEG vs RAD)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        ) {
                            Text(
                                text = "Trigonometric Unit",
                                color = colors.textPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = if (uiState.isDegreeMode) "Degree Mode (DEG): sin(90°) = 1" else "Radian Mode (RAD): sin(π/2) = 1",
                                color = colors.textMuted,
                                fontSize = 12.sp
                            )
                        }
                        Button(
                            onClick = onToggleAngle,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (uiState.isDegreeMode) colors.keyOpBg else colors.keyFnBg
                            ),
                            shape = shapes.pillShape,
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = if (uiState.isDegreeMode) "DEG" else "RAD",
                                color = if (uiState.isDegreeMode) colors.accentPrimary else colors.accentSecondary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Divider(color = colors.border.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 12.dp))

                    // Decimal Precision
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Decimal Precision",
                                color = colors.textPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.weight(1f).padding(end = 4.dp)
                            )
                            Text(
                                text = "${uiState.precisionDecimals} decimal places",
                                color = colors.accentPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf(4, 6, 8, 10, 12).forEach { prec ->
                                val isSelected = uiState.precisionDecimals == prec
                                Surface(
                                    shape = shapes.pillShape,
                                    color = if (isSelected) colors.keyOpBg else colors.surface,
                                    border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, colors.accentPrimary) else null,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(34.dp)
                                        .clickable { onSelectPrecision(prec) }
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "$prec",
                                            color = if (isSelected) colors.accentPrimary else colors.textMuted,
                                            fontSize = 12.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Divider(color = colors.border.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 12.dp))

                    // Number Format Cycle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        ) {
                            Text(
                                text = "Result Notation",
                                color = colors.textPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            val formatDesc = when (uiState.numberFormat) {
                                NumberDisplayFormat.STANDARD -> "Standard decimal (e.g. 1250.75)"
                                NumberDisplayFormat.SCIENTIFIC -> "Scientific (e.g. 1.25075e+03)"
                                NumberDisplayFormat.POLAR -> "Complex polar (e.g. 5 ∠ 53.13°)"
                            }
                            Text(
                                text = formatDesc,
                                color = colors.textMuted,
                                fontSize = 12.sp
                            )
                        }
                        Button(
                            onClick = onCycleFormat,
                            colors = ButtonDefaults.buttonColors(containerColor = colors.surface),
                            shape = shapes.pillShape,
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = uiState.numberFormat.name,
                                color = colors.accentPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // ==========================================
                // 3. Tactile & System Feedback
                // ==========================================
                SectionCard(
                    title = "Tactile & Haptics",
                    icon = Icons.Default.Vibration
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                            Text(
                                text = "Keypad Vibration Feedback",
                                color = colors.textPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Provides responsive tactile vibration on button clicks",
                                color = colors.textMuted,
                                fontSize = 12.sp
                            )
                        }
                        Switch(
                            checked = uiState.hapticsEnabled,
                            onCheckedChange = onToggleHaptics,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = colors.accentPrimary,
                                checkedTrackColor = colors.keyOpBg,
                                uncheckedThumbColor = colors.textMuted,
                                uncheckedTrackColor = colors.surfaceElevated
                            )
                        )
                    }
                }

                // ==========================================
                // 4. Data Storage & Export
                // ==========================================
                SectionCard(
                    title = "Storage & Database",
                    icon = Icons.Default.Storage
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        ) {
                            Text(
                                text = "Calculation History Logs",
                                color = colors.textPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "$historyCount entries in local SQLite database",
                                color = colors.textMuted,
                                fontSize = 12.sp
                            )
                        }
                        OutlinedButton(
                            onClick = onExportHistory,
                            shape = shapes.pillShape,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.accentPrimary),
                            border = androidx.compose.foundation.BorderStroke(1.dp, colors.accentPrimary.copy(alpha = 0.5f)),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Export", fontSize = 12.sp)
                        }
                    }

                    Divider(color = colors.border.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        ) {
                            Text(
                                text = "Reset Local Storage",
                                color = colors.textPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "$variablesCount user variables & history logs",
                                color = colors.textMuted,
                                fontSize = 12.sp
                            )
                        }
                        OutlinedButton(
                            onClick = { showClearDataConfirm = true },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.error),
                            border = androidx.compose.foundation.BorderStroke(1.dp, colors.error.copy(alpha = 0.5f)),
                            shape = shapes.pillShape,
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text("Clear Data", fontSize = 12.sp)
                        }
                    }
                }

                // ==========================================
                // 5. Legal & About (Clean full titles, no short forms)
                // ==========================================
                SectionCard(
                    title = "Documentation & Legal",
                    icon = Icons.Default.MenuBook
                ) {
                    SettingsNavigationItem(
                        icon = Icons.Default.Gavel,
                        title = "Terms and Conditions",
                        subtitle = "Usage rules, computation parameters and disclaimer",
                        onClick = { onOpenModal(ActiveModal.TERMS_AND_CONDITIONS) }
                    )
                    Divider(color = colors.border.copy(alpha = 0.4f), modifier = Modifier.padding(vertical = 4.dp))
                    SettingsNavigationItem(
                        icon = Icons.Default.Shield,
                        title = "Privacy Policy",
                        subtitle = "100% offline, zero telemetry, zero analytics tracking",
                        onClick = { onOpenModal(ActiveModal.PRIVACY_POLICY) }
                    )
                    Divider(color = colors.border.copy(alpha = 0.4f), modifier = Modifier.padding(vertical = 4.dp))
                    SettingsNavigationItem(
                        icon = Icons.Default.Description,
                        title = "Open Source Licenses",
                        subtitle = "Complete legal texts for AlexCalc, Android Jetpack, Room, Kotlin",
                        onClick = { onOpenModal(ActiveModal.LICENSES) }
                    )
                    Divider(color = colors.border.copy(alpha = 0.4f), modifier = Modifier.padding(vertical = 4.dp))
                    SettingsNavigationItem(
                        icon = Icons.Default.Info,
                        title = "About NovaCalc",
                        subtitle = "Version 1.2.0 • Architecture & Credits To The Owner",
                        onClick = { onOpenModal(ActiveModal.ABOUT) }
                    )
                }
            }
        }
    }

    if (showClearDataConfirm) {
        AlertDialog(
            onDismissRequest = { showClearDataConfirm = false },
            containerColor = colors.surfaceElevated,
            shape = shapes.cardShape,
            title = {
                Text("Clear All Data?", color = colors.textPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    "This will permanently delete all calculation history logs and custom variables from your device's local database. System constants (π, e, i, φ) will remain.",
                    color = colors.textMuted
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onClearAllData()
                        showClearDataConfirm = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = colors.error),
                    shape = shapes.pillShape
                ) {
                    Text("Delete All", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDataConfirm = false }) {
                    Text("Cancel", color = colors.textMuted)
                }
            }
        )
    }
}

/**
 * Modern visual representation screenshot / mockup of the calculator theme.
 */
@Composable
private fun ThemeScreenshotCard(
    theme: AppTheme,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = LocalCalcColors.current
    val shapes = LocalCalcShapes.current
    val preview = getThemeColors(theme)

    Surface(
        shape = shapes.cardShape,
        color = colors.surface,
        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, preview.accentPrimary)
        else androidx.compose.foundation.BorderStroke(1.dp, colors.border.copy(alpha = 0.6f)),
        modifier = Modifier
            .width(148.dp)
            .clickable { onClick() }
            .testTag("theme_card_${theme.name}")
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Miniature UI Screenshot Representation
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = preview.bg,
                border = androidx.compose.foundation.BorderStroke(1.dp, preview.border),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(98.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(6.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Mini Top Status Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .size(width = 18.dp, height = 5.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(preview.keyOpBg)
                        )
                        Box(
                            modifier = Modifier
                                .size(width = 10.dp, height = 5.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(preview.accentPrimary)
                        )
                    }

                    // Mini Display Box with realistic math
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = preview.displayBg,
                        border = androidx.compose.foundation.BorderStroke(0.5.dp, preview.border),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp, vertical = 2.dp),
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = "sin(π/4) + 2i",
                                color = preview.textPrimary,
                                fontSize = 7.sp,
                                fontFamily = FontFamily.Monospace,
                                maxLines = 1
                            )
                            Text(
                                text = "= 0.707 + 2i",
                                color = preview.accentPrimary,
                                fontSize = 7.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                        }
                    }

                    // Mini Keypad Grid (3 rows x 4 cols)
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        // Row 1
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            MiniKey(color = preview.keyFnBg, weight = 1f)
                            MiniKey(color = preview.keyFnBg, weight = 1f)
                            MiniKey(color = preview.keyOpBg, weight = 1f)
                            MiniKey(color = preview.keyClearBg, weight = 1f)
                        }
                        // Row 2
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            MiniKey(color = preview.keyNumBg, weight = 1f)
                            MiniKey(color = preview.keyNumBg, weight = 1f)
                            MiniKey(color = preview.keyNumBg, weight = 1f)
                            MiniKey(color = preview.keyOpBg, weight = 1f)
                        }
                        // Row 3
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            MiniKey(color = preview.keyNumBg, weight = 1f)
                            MiniKey(color = preview.keyNumBg, weight = 1f)
                            MiniKey(color = preview.keyNumBg, weight = 1f)
                            MiniKey(color = preview.keyEqualBg, weight = 1f)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Theme Name & Selection Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = theme.displayName,
                    color = if (isSelected) preview.accentPrimary else colors.textPrimary,
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    maxLines = 1
                )
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Selected",
                        tint = preview.accentPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun MiniKey(color: Color, weight: Float) {
    Box(
        modifier = Modifier
            .height(8.dp)
            .clip(RoundedCornerShape(1.5.dp))
            .background(color)
    )
}

@Composable
private fun ChoicePill(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = LocalCalcColors.current
    val shapes = LocalCalcShapes.current

    Surface(
        shape = shapes.pillShape,
        color = if (isSelected) colors.keyOpBg else colors.surface,
        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, colors.accentPrimary)
        else androidx.compose.foundation.BorderStroke(1.dp, colors.border),
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = label,
            color = if (isSelected) colors.accentPrimary else colors.textMuted,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 11.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun SectionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    val colors = LocalCalcColors.current
    val shapes = LocalCalcShapes.current

    Surface(
        shape = shapes.cardShape,
        color = colors.surfaceElevated,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = colors.accentPrimary,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = title,
                    color = colors.accentPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
private fun SettingsNavigationItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    val colors = LocalCalcColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f).padding(end = 8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = colors.accentPrimary,
                modifier = Modifier.size(20.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = colors.textPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = subtitle,
                    color = colors.textMuted,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = colors.textMuted,
            modifier = Modifier.size(18.dp)
        )
    }
}
