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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.CalculatorUiState
import com.example.ui.NumberDisplayFormat
import com.example.ui.theme.LocalCalcColors
import com.example.ui.theme.LocalCalcShapes

@Composable
fun DisplaySection(
    uiState: CalculatorUiState,
    onExpressionChanged: (String) -> Unit,
    onCursorChanged: (Int) -> Unit,
    onEvaluate: () -> Unit,
    onToggleAngle: () -> Unit,
    onCycleFormat: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val colors = LocalCalcColors.current
    val shapes = LocalCalcShapes.current
    val scrollState = rememberScrollState()

    // Calculate unclosed parentheses
    val openParens = uiState.expression.count { it == '(' }
    val closeParens = uiState.expression.count { it == ')' }
    val unclosedCount = openParens - closeParens

    // Synchronize TextFieldValue with uiState
    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = uiState.expression,
                selection = TextRange(uiState.cursorPosition)
            )
        )
    }

    LaunchedEffect(uiState.expression, uiState.cursorPosition) {
        if (textFieldValue.text != uiState.expression ||
            textFieldValue.selection.start != uiState.cursorPosition
        ) {
            textFieldValue = TextFieldValue(
                text = uiState.expression,
                selection = TextRange(uiState.cursorPosition.coerceIn(0, uiState.expression.length))
            )
        }
    }

    LaunchedEffect(uiState.expression) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(shapes.cardShape)
            .border(1.dp, colors.border, shapes.cardShape)
            .testTag("display_section"),
        colors = CardDefaults.cardColors(containerColor = colors.displayBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Top Status Bar: Angle Mode, Format, and Parentheses indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // DEG / RAD Toggle Chip
                    Surface(
                        shape = shapes.pillShape,
                        color = if (uiState.isDegreeMode) colors.keyOpBg else colors.keyFnBg,
                        modifier = Modifier
                            .clickable { onToggleAngle() }
                            .testTag("toggle_angle_chip")
                    ) {
                        Text(
                            text = if (uiState.isDegreeMode) "DEG" else "RAD",
                            color = if (uiState.isDegreeMode) colors.accentPrimary else colors.accentSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    // Number Format Chip (STD / SCI / POLAR)
                    Surface(
                        shape = shapes.pillShape,
                        color = colors.surfaceElevated,
                        modifier = Modifier
                            .clickable { onCycleFormat() }
                            .testTag("cycle_format_chip")
                    ) {
                        val fmtLabel = when (uiState.numberFormat) {
                            NumberDisplayFormat.STANDARD -> "STD"
                            NumberDisplayFormat.SCIENTIFIC -> "SCI"
                            NumberDisplayFormat.POLAR -> "POLAR"
                        }
                        Text(
                            text = fmtLabel,
                            color = colors.textMuted,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    // Parentheses Balance Badge
                    if (unclosedCount > 0) {
                        Surface(
                            shape = shapes.pillShape,
                            color = colors.accentSecondary.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "unclosed: $unclosedCount )",
                                color = colors.accentSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                // Copy button for result or expression
                val textToCopy = uiState.finalResult ?: uiState.liveResult ?: uiState.expression
                if (textToCopy.isNotBlank()) {
                    IconButton(
                        onClick = {
                            val clipboard =
                                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("NovaCalc", textToCopy))
                            Toast.makeText(context, "Copied: $textToCopy", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("copy_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy result",
                            tint = colors.textMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Main Expression Input
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState)
            ) {
                if (textFieldValue.text.isEmpty()) {
                    Text(
                        text = "0",
                        color = colors.textMuted.copy(alpha = 0.4f),
                        fontSize = 32.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Light
                    )
                }
                BasicTextField(
                    value = textFieldValue,
                    onValueChange = { newVal ->
                        textFieldValue = newVal
                        onExpressionChanged(newVal.text)
                        onCursorChanged(newVal.selection.start)
                    },
                    textStyle = TextStyle(
                        color = colors.textPrimary,
                        fontSize = 32.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Normal,
                        letterSpacing = 0.5.sp
                    ),
                    cursorBrush = SolidColor(colors.accentPrimary),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { onEvaluate() }
                    ),
                    modifier = Modifier.testTag("expression_input")
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Live Preview or Final Evaluated Result
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                // If there's an error message
                AnimatedVisibility(
                    visible = uiState.errorMessage != null,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Text(
                        text = uiState.errorMessage ?: "",
                        color = colors.error,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.End,
                        maxLines = 1
                    )
                }

                // If calculated final result
                AnimatedVisibility(
                    visible = uiState.errorMessage == null && uiState.finalResult != null,
                    enter = fadeIn() + scaleIn(initialScale = 0.9f),
                    exit = fadeOut()
                ) {
                    Text(
                        text = "= ${uiState.finalResult}",
                        color = colors.accentPrimary,
                        fontSize = 26.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End,
                        maxLines = 1,
                        modifier = Modifier.testTag("final_result_text")
                    )
                }

                // Live preview while user is still typing expression
                AnimatedVisibility(
                    visible = uiState.errorMessage == null && uiState.finalResult == null && uiState.liveResult != null,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Text(
                        text = "= ${uiState.liveResult}",
                        color = colors.accentPrimary.copy(alpha = 0.7f),
                        fontSize = 20.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.End,
                        maxLines = 1,
                        modifier = Modifier.testTag("live_result_text")
                    )
                }
            }
        }
    }
}
