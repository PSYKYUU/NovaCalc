package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ActiveModal
import com.example.ui.CalculatorUiState
import com.example.ui.theme.AppTheme
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
    onOpenModal: (ActiveModal) -> Unit,
    onClearAllData: () -> Unit,
    onDismiss: () -> Unit
) {
    val colors = LocalCalcColors.current
    val shapes = LocalCalcShapes.current
    val scrollState = rememberScrollState()
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
                .fillMaxHeight(0.88f)
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
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        tint = colors.accentPrimary
                    )
                    Text(
                        text = "Settings",
                        color = colors.textPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
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
                // 1. Theme & Appearance (including Corner Style toggle)
                SectionCard(title = "Appearance & Theme") {
                    Text(
                        text = "Color Palette",
                        color = colors.textPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        AppTheme.values().forEach { theme ->
                            ThemeItemRow(
                                theme = theme,
                                isSelected = uiState.appTheme == theme,
                                onClick = { onSelectTheme(theme) }
                            )
                        }
                    }

                    Divider(color = colors.border.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 12.dp))

                    // Corner Styling Toggle (Rounded vs Sharp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Corner Style",
                                color = colors.textPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = if (uiState.sharpCorners) "Sharp square edges" else "Smooth rounded corners",
                                color = colors.textMuted,
                                fontSize = 12.sp
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            ChoicePill(
                                label = "Rounded",
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

                // 2. Layout & Display Options
                SectionCard(title = "Layout & Variables") {
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
                                text = "Show horizontal variable chips bar directly above the keypad",
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

                    Divider(color = colors.border.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 10.dp))

                    // Angle Unit
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Trigonometric Angle Unit",
                                color = colors.textPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = if (uiState.isDegreeMode) "Degree Mode (DEG)" else "Radian Mode (RAD)",
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

                    Divider(color = colors.border.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 10.dp))

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
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "${uiState.precisionDecimals} decimals",
                                color = colors.accentPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
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
                                            fontSize = 13.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // 3. Tactile Feedback
                SectionCard(title = "Haptic Feedback") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Vibration on Key Press",
                                color = colors.textPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Provides responsive tactile feedback",
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

                // 4. Data Management
                SectionCard(title = "Storage & Database") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Local Database",
                                color = colors.textPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "$historyCount calculation logs • $variablesCount variables",
                                color = colors.textMuted,
                                fontSize = 12.sp
                            )
                        }
                        OutlinedButton(
                            onClick = { showClearDataConfirm = true },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.error),
                            border = androidx.compose.foundation.BorderStroke(1.dp, colors.error.copy(alpha = 0.5f)),
                            shape = shapes.pillShape
                        ) {
                            Text("Clear Data", fontSize = 12.sp)
                        }
                    }
                }

                // 5. Legal & About (Clean full titles, no short forms)
                SectionCard(title = "Legal and About") {
                    SettingsNavigationItem(
                        icon = Icons.Default.Gavel,
                        title = "Terms and Conditions",
                        subtitle = "Usage rules and calculation accuracy disclaimer",
                        onClick = { onOpenModal(ActiveModal.TERMS_AND_CONDITIONS) }
                    )
                    Divider(color = colors.border.copy(alpha = 0.4f), modifier = Modifier.padding(vertical = 4.dp))
                    SettingsNavigationItem(
                        icon = Icons.Default.Shield,
                        title = "Privacy Policy",
                        subtitle = "100% offline, zero data collection or tracking",
                        onClick = { onOpenModal(ActiveModal.PRIVACY_POLICY) }
                    )
                    Divider(color = colors.border.copy(alpha = 0.4f), modifier = Modifier.padding(vertical = 4.dp))
                    SettingsNavigationItem(
                        icon = Icons.Default.Description,
                        title = "Open Source Licenses",
                        subtitle = "Full legal texts for Android Jetpack, Compose, Room, AlexCalc",
                        onClick = { onOpenModal(ActiveModal.LICENSES) }
                    )
                    Divider(color = colors.border.copy(alpha = 0.4f), modifier = Modifier.padding(vertical = 4.dp))
                    SettingsNavigationItem(
                        icon = Icons.Default.Info,
                        title = "About NovaCalc",
                        subtitle = "Version 1.2.0 • Architecture & Credits To The Owner (CTTO)",
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
        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, colors.accentPrimary) else androidx.compose.foundation.BorderStroke(1.dp, colors.border),
        modifier = Modifier
            .clickable { onClick() }
    ) {
        Text(
            text = label,
            color = if (isSelected) colors.accentPrimary else colors.textMuted,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
        )
    }
}

@Composable
private fun SectionCard(
    title: String,
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
                .padding(16.dp)
        ) {
            Text(
                text = title,
                color = colors.accentPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))
            content()
        }
    }
}

@Composable
private fun ThemeItemRow(
    theme: AppTheme,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = LocalCalcColors.current
    val shapes = LocalCalcShapes.current
    val preview = getThemeColors(theme)

    Surface(
        shape = shapes.pillShape,
        color = if (isSelected) preview.keyOpBg else preview.surface,
        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, preview.accentPrimary) else androidx.compose.foundation.BorderStroke(1.dp, colors.border.copy(alpha = 0.5f)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("theme_item_${theme.name}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(preview.bg)
                            .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(preview.accentPrimary)
                    )
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(preview.accentSecondary)
                    )
                }

                Column {
                    Text(
                        text = theme.displayName,
                        color = if (isSelected) preview.accentPrimary else colors.textPrimary,
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                    Text(
                        text = theme.description,
                        color = colors.textMuted,
                        fontSize = 11.sp
                    )
                }
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Selected",
                    tint = preview.accentPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
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
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = colors.accentPrimary,
                modifier = Modifier.size(20.dp)
            )
            Column {
                Text(
                    text = title,
                    color = colors.textPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = subtitle,
                    color = colors.textMuted,
                    fontSize = 11.sp
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
