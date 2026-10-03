package com.example.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(
    viewModel: AlexCalcViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val historyList by viewModel.historyList.collectAsStateWithLifecycle()
    val variablesList by viewModel.variablesList.collectAsStateWithLifecycle()

    BackHandler(enabled = uiState.activeModal != ActiveModal.NONE) {
        viewModel.setActiveModal(ActiveModal.NONE)
    }

    MyApplicationTheme(
        appTheme = uiState.appTheme,
        sharpCorners = uiState.sharpCorners
    ) {
        val colors = LocalCalcColors.current
        val shapes = LocalCalcShapes.current

        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .background(colors.bg),
            containerColor = colors.bg,
            contentWindowInsets = WindowInsets.safeDrawing,
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "NovaCalc",
                                color = colors.textPrimary,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Surface(
                                shape = shapes.pillShape,
                                color = colors.keyOpBg
                            ) {
                                Text(
                                    text = "ℂ / ℝ",
                                    color = colors.accentPrimary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    },
                    actions = {
                        // Guide / Examples button
                        IconButton(
                            onClick = { viewModel.setActiveModal(ActiveModal.HELP) },
                            modifier = Modifier.testTag("topbar_btn_help")
                        ) {
                            Icon(
                                imageVector = Icons.Default.HelpOutline,
                                contentDescription = "Guide and Examples",
                                tint = colors.textMuted
                            )
                        }

                        // Variables button
                        IconButton(
                            onClick = { viewModel.setActiveModal(ActiveModal.VARIABLES) },
                            modifier = Modifier.testTag("topbar_btn_vars")
                        ) {
                            BadgedBox(
                                badge = {
                                    if (variablesList.isNotEmpty()) {
                                        Badge(
                                            containerColor = colors.accentTertiary,
                                            contentColor = colors.bg
                                        ) {
                                            Text("${variablesList.size}")
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DataArray,
                                    contentDescription = "Variables",
                                    tint = colors.accentTertiary
                                )
                            }
                        }

                        // History button
                        IconButton(
                            onClick = { viewModel.setActiveModal(ActiveModal.HISTORY) },
                            modifier = Modifier.testTag("topbar_btn_history")
                        ) {
                            BadgedBox(
                                badge = {
                                    if (historyList.isNotEmpty()) {
                                        Badge(
                                            containerColor = colors.accentPrimary,
                                            contentColor = colors.bg
                                        ) {
                                            Text("${historyList.size}")
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.History,
                                    contentDescription = "History",
                                    tint = colors.accentPrimary
                                )
                            }
                        }

                        // Settings button
                        IconButton(
                            onClick = { viewModel.setActiveModal(ActiveModal.SETTINGS) },
                            modifier = Modifier.testTag("topbar_btn_settings")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = colors.accentPrimary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colors.bg,
                        titleContentColor = colors.textPrimary
                    )
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Interactive Display Section
                DisplaySection(
                    uiState = uiState,
                    onExpressionChanged = { newExpr ->
                        // Managed in text field
                    },
                    onCursorChanged = { newPos ->
                        viewModel.setCursorPosition(newPos)
                    },
                    onEvaluate = { viewModel.evaluate() },
                    onToggleAngle = { viewModel.toggleAngleMode() },
                    onCycleFormat = { viewModel.cycleNumberFormat() },
                    modifier = Modifier.weight(1f)
                )

                // Optional Quick Variables Row (Toggled via Settings)
                AnimatedVisibility(
                    visible = uiState.showQuickVariables,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    VariablesRow(
                        variables = variablesList,
                        onInsertVariable = { varName -> viewModel.insertText(varName) },
                        onOpenVariablesModal = { viewModel.setActiveModal(ActiveModal.VARIABLES) },
                        onStoreCurrent = { viewModel.showSaveVariableDialog() }
                    )
                }

                // Multi-mode Animated Keypad
                KeypadSection(
                    mode = uiState.keypadMode,
                    onModeChange = { newMode -> viewModel.setKeypadMode(newMode) },
                    onInsertText = { text -> viewModel.insertText(text) },
                    onSmartParenthesis = { viewModel.insertSmartParenthesis() },
                    onDeleteBackward = { viewModel.deleteBackward() },
                    onClearAll = { viewModel.clearAll() },
                    onEvaluate = { viewModel.evaluate() },
                    hapticsEnabled = uiState.hapticsEnabled
                )
            }
        }

        // Modals & Bottom Sheets
        when (uiState.activeModal) {
            ActiveModal.HISTORY -> {
                HistorySheet(
                    historyList = historyList,
                    searchQuery = uiState.historySearchQuery,
                    onSearchQueryChange = { query -> viewModel.setHistorySearchQuery(query) },
                    onUseExpression = { expr -> viewModel.useHistoryExpression(expr) },
                    onUseResult = { res -> viewModel.useHistoryResult(res) },
                    onToggleFavorite = { id -> viewModel.toggleFavoriteHistory(id) },
                    onDeleteHistory = { id -> viewModel.deleteHistory(id) },
                    onClearAll = { viewModel.clearAllHistory() },
                    onDismiss = { viewModel.setActiveModal(ActiveModal.NONE) }
                )
            }
            ActiveModal.VARIABLES -> {
                VariablesSheet(
                    variables = variablesList,
                    onInsertVariable = { name ->
                        viewModel.insertText(name)
                        viewModel.setActiveModal(ActiveModal.NONE)
                    },
                    onAddNewVariable = {
                        viewModel.showSaveVariableDialog("")
                    },
                    onDeleteVariable = { name -> viewModel.deleteVariable(name) },
                    onDismiss = { viewModel.setActiveModal(ActiveModal.NONE) }
                )
            }
            ActiveModal.SAVE_VARIABLE -> {
                SaveVariableDialog(
                    varName = uiState.pendingVarName,
                    varExpr = uiState.pendingVarExpr,
                    onNameChange = { viewModel.setPendingVarName(it) },
                    onExprChange = { viewModel.setPendingVarExpr(it) },
                    onSave = { viewModel.savePendingVariable() },
                    onDismiss = { viewModel.setActiveModal(ActiveModal.NONE) }
                )
            }
            ActiveModal.HELP -> {
                HelpDialog(
                    onLoadExample = { expr ->
                        viewModel.useHistoryExpression(expr)
                        viewModel.evaluate()
                    },
                    onDismiss = { viewModel.setActiveModal(ActiveModal.NONE) }
                )
            }
            ActiveModal.SETTINGS -> {
                SettingsSheet(
                    uiState = uiState,
                    historyCount = historyList.size,
                    variablesCount = variablesList.size,
                    onSelectTheme = { theme -> viewModel.setAppTheme(theme) },
                    onToggleSharpCorners = { sharp -> viewModel.setSharpCorners(sharp) },
                    onToggleQuickVariables = { show -> viewModel.setShowQuickVariables(show) },
                    onToggleHaptics = { enabled -> viewModel.setHapticsEnabled(enabled) },
                    onSelectPrecision = { prec -> viewModel.setPrecisionDecimals(prec) },
                    onToggleAngle = { viewModel.toggleAngleMode() },
                    onCycleFormat = { viewModel.cycleNumberFormat() },
                    onOpenModal = { modal -> viewModel.setActiveModal(modal) },
                    onClearAllData = { viewModel.clearAllData() },
                    onExportHistory = {
                        if (historyList.isEmpty()) {
                            Toast.makeText(context, "No calculation history to export", Toast.LENGTH_SHORT).show()
                        } else {
                            val text = historyList.joinToString("\n") { "${it.expression} = ${it.resultFormatted}" }
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("NovaCalc History", text))
                            Toast.makeText(context, "Exported ${historyList.size} calculations to clipboard", Toast.LENGTH_SHORT).show()
                        }
                    },
                    onDismiss = { viewModel.setActiveModal(ActiveModal.NONE) }
                )
            }
            ActiveModal.TERMS_AND_CONDITIONS -> {
                TermsDialog(
                    onDismiss = { viewModel.setActiveModal(ActiveModal.SETTINGS) }
                )
            }
            ActiveModal.PRIVACY_POLICY -> {
                PrivacyPolicyDialog(
                    onDismiss = { viewModel.setActiveModal(ActiveModal.SETTINGS) }
                )
            }
            ActiveModal.LICENSES -> {
                LicensesDialog(
                    onDismiss = { viewModel.setActiveModal(ActiveModal.SETTINGS) }
                )
            }
            ActiveModal.ABOUT -> {
                AboutDialog(
                    onDismiss = { viewModel.setActiveModal(ActiveModal.SETTINGS) }
                )
            }
            ActiveModal.NONE -> { /* No modal */ }
        }
    }
}
