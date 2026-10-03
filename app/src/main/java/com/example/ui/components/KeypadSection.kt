package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.KeypadMode
import com.example.ui.theme.LocalCalcColors
import com.example.ui.theme.LocalCalcShapes

@Composable
fun KeypadSection(
    mode: KeypadMode,
    onModeChange: (KeypadMode) -> Unit,
    onInsertText: (String) -> Unit,
    onSmartParenthesis: () -> Unit,
    onDeleteBackward: () -> Unit,
    onClearAll: () -> Unit,
    onEvaluate: () -> Unit,
    hapticsEnabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    val colors = LocalCalcColors.current
    val shapes = LocalCalcShapes.current

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Mode Selector Tab Bar
        Surface(
            shape = shapes.cardShape,
            color = colors.surfaceElevated,
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .padding(horizontal = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(3.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                ModeTab(
                    label = "Basic",
                    isSelected = mode == KeypadMode.BASIC,
                    onClick = { onModeChange(KeypadMode.BASIC) },
                    modifier = Modifier.weight(1f),
                    testTag = "tab_basic"
                )
                ModeTab(
                    label = "Scientific",
                    isSelected = mode == KeypadMode.SCIENTIFIC,
                    onClick = { onModeChange(KeypadMode.SCIENTIFIC) },
                    modifier = Modifier.weight(1f),
                    testTag = "tab_scientific"
                )
                ModeTab(
                    label = "Complex ℂ",
                    isSelected = mode == KeypadMode.COMPLEX,
                    onClick = { onModeChange(KeypadMode.COMPLEX) },
                    modifier = Modifier.weight(1f),
                    testTag = "tab_complex"
                )
            }
        }

        // Animated keypad content based on mode
        AnimatedContent(
            targetState = mode,
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
            },
            label = "keypad_transition"
        ) { targetMode ->
            when (targetMode) {
                KeypadMode.BASIC -> BasicKeypad(
                    onInsertText = onInsertText,
                    onSmartParenthesis = onSmartParenthesis,
                    onDeleteBackward = onDeleteBackward,
                    onClearAll = onClearAll,
                    onEvaluate = onEvaluate,
                    hapticsEnabled = hapticsEnabled
                )
                KeypadMode.SCIENTIFIC -> ScientificKeypad(
                    onInsertText = onInsertText,
                    onSmartParenthesis = onSmartParenthesis,
                    onDeleteBackward = onDeleteBackward,
                    onClearAll = onClearAll,
                    onEvaluate = onEvaluate,
                    hapticsEnabled = hapticsEnabled
                )
                KeypadMode.COMPLEX -> ComplexKeypad(
                    onInsertText = onInsertText,
                    onSmartParenthesis = onSmartParenthesis,
                    onDeleteBackward = onDeleteBackward,
                    onClearAll = onClearAll,
                    onEvaluate = onEvaluate,
                    hapticsEnabled = hapticsEnabled
                )
            }
        }
    }
}

@Composable
private fun ModeTab(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String
) {
    val colors = LocalCalcColors.current
    val shapes = LocalCalcShapes.current
    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(shapes.pillShape)
            .background(if (isSelected) colors.keyOpBg else Color.Transparent)
            .clickable { onClick() }
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (isSelected) colors.accentPrimary else colors.textMuted,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
private fun BasicKeypad(
    onInsertText: (String) -> Unit,
    onSmartParenthesis: () -> Unit,
    onDeleteBackward: () -> Unit,
    onClearAll: () -> Unit,
    onEvaluate: () -> Unit,
    hapticsEnabled: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CalcButton(text = "AC", onClick = onClearAll, type = ButtonType.CLEAR, modifier = Modifier.weight(1f), height = 58.dp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "( )", onClick = onSmartParenthesis, type = ButtonType.OPERATOR, modifier = Modifier.weight(1f), height = 58.dp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "%", onClick = { onInsertText("%") }, type = ButtonType.OPERATOR, modifier = Modifier.weight(1f), height = 58.dp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "÷", onClick = { onInsertText("/") }, type = ButtonType.OPERATOR, modifier = Modifier.weight(1f), height = 58.dp, hapticsEnabled = hapticsEnabled)
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CalcButton(text = "7", onClick = { onInsertText("7") }, type = ButtonType.NUMBER, modifier = Modifier.weight(1f), height = 58.dp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "8", onClick = { onInsertText("8") }, type = ButtonType.NUMBER, modifier = Modifier.weight(1f), height = 58.dp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "9", onClick = { onInsertText("9") }, type = ButtonType.NUMBER, modifier = Modifier.weight(1f), height = 58.dp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "×", onClick = { onInsertText("*") }, type = ButtonType.OPERATOR, modifier = Modifier.weight(1f), height = 58.dp, hapticsEnabled = hapticsEnabled)
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CalcButton(text = "4", onClick = { onInsertText("4") }, type = ButtonType.NUMBER, modifier = Modifier.weight(1f), height = 58.dp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "5", onClick = { onInsertText("5") }, type = ButtonType.NUMBER, modifier = Modifier.weight(1f), height = 58.dp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "6", onClick = { onInsertText("6") }, type = ButtonType.NUMBER, modifier = Modifier.weight(1f), height = 58.dp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "-", onClick = { onInsertText("-") }, type = ButtonType.OPERATOR, modifier = Modifier.weight(1f), height = 58.dp, hapticsEnabled = hapticsEnabled)
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CalcButton(text = "1", onClick = { onInsertText("1") }, type = ButtonType.NUMBER, modifier = Modifier.weight(1f), height = 58.dp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "2", onClick = { onInsertText("2") }, type = ButtonType.NUMBER, modifier = Modifier.weight(1f), height = 58.dp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "3", onClick = { onInsertText("3") }, type = ButtonType.NUMBER, modifier = Modifier.weight(1f), height = 58.dp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "+", onClick = { onInsertText("+") }, type = ButtonType.OPERATOR, modifier = Modifier.weight(1f), height = 58.dp, hapticsEnabled = hapticsEnabled)
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CalcButton(text = "0", onClick = { onInsertText("0") }, type = ButtonType.NUMBER, modifier = Modifier.weight(1f), height = 58.dp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = ".", onClick = { onInsertText(".") }, type = ButtonType.NUMBER, modifier = Modifier.weight(1f), height = 58.dp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "⌫", onClick = onDeleteBackward, type = ButtonType.SECONDARY, modifier = Modifier.weight(1f), height = 58.dp, testTag = "btn_backspace", hapticsEnabled = hapticsEnabled)
            CalcButton(text = "=", onClick = onEvaluate, type = ButtonType.EQUAL, modifier = Modifier.weight(1f), height = 58.dp, testTag = "btn_equals", hapticsEnabled = hapticsEnabled)
        }
    }
}

@Composable
private fun ScientificKeypad(
    onInsertText: (String) -> Unit,
    onSmartParenthesis: () -> Unit,
    onDeleteBackward: () -> Unit,
    onClearAll: () -> Unit,
    onEvaluate: () -> Unit,
    hapticsEnabled: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            CalcButton(text = "sin", onClick = { onInsertText("sin(") }, type = ButtonType.FUNCTION, modifier = Modifier.weight(1f), height = 48.dp, fontSize = 15.sp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "cos", onClick = { onInsertText("cos(") }, type = ButtonType.FUNCTION, modifier = Modifier.weight(1f), height = 48.dp, fontSize = 15.sp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "tan", onClick = { onInsertText("tan(") }, type = ButtonType.FUNCTION, modifier = Modifier.weight(1f), height = 48.dp, fontSize = 15.sp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "xʸ", onClick = { onInsertText("^") }, type = ButtonType.FUNCTION, modifier = Modifier.weight(1f), height = 48.dp, fontSize = 15.sp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "√", onClick = { onInsertText("sqrt(") }, type = ButtonType.FUNCTION, modifier = Modifier.weight(1f), height = 48.dp, fontSize = 16.sp, hapticsEnabled = hapticsEnabled)
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            CalcButton(text = "asin", onClick = { onInsertText("asin(") }, type = ButtonType.FUNCTION, modifier = Modifier.weight(1f), height = 48.dp, fontSize = 14.sp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "acos", onClick = { onInsertText("acos(") }, type = ButtonType.FUNCTION, modifier = Modifier.weight(1f), height = 48.dp, fontSize = 14.sp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "atan", onClick = { onInsertText("atan(") }, type = ButtonType.FUNCTION, modifier = Modifier.weight(1f), height = 48.dp, fontSize = 14.sp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "ln", onClick = { onInsertText("ln(") }, type = ButtonType.FUNCTION, modifier = Modifier.weight(1f), height = 48.dp, fontSize = 15.sp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "log", onClick = { onInsertText("log10(") }, type = ButtonType.FUNCTION, modifier = Modifier.weight(1f), height = 48.dp, fontSize = 15.sp, hapticsEnabled = hapticsEnabled)
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            CalcButton(text = "eˣ", onClick = { onInsertText("exp(") }, type = ButtonType.FUNCTION, modifier = Modifier.weight(1f), height = 48.dp, fontSize = 15.sp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "x!", onClick = { onInsertText("!") }, type = ButtonType.FUNCTION, modifier = Modifier.weight(1f), height = 48.dp, fontSize = 15.sp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "(", onClick = { onInsertText("(") }, type = ButtonType.OPERATOR, modifier = Modifier.weight(1f), height = 48.dp, fontSize = 16.sp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = ")", onClick = { onInsertText(")") }, type = ButtonType.OPERATOR, modifier = Modifier.weight(1f), height = 48.dp, fontSize = 16.sp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "AC", onClick = onClearAll, type = ButtonType.CLEAR, modifier = Modifier.weight(1f), height = 48.dp, fontSize = 15.sp, hapticsEnabled = hapticsEnabled)
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            CalcButton(text = "7", onClick = { onInsertText("7") }, type = ButtonType.NUMBER, modifier = Modifier.weight(1f), height = 48.dp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "8", onClick = { onInsertText("8") }, type = ButtonType.NUMBER, modifier = Modifier.weight(1f), height = 48.dp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "9", onClick = { onInsertText("9") }, type = ButtonType.NUMBER, modifier = Modifier.weight(1f), height = 48.dp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "÷", onClick = { onInsertText("/") }, type = ButtonType.OPERATOR, modifier = Modifier.weight(1f), height = 48.dp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "%", onClick = { onInsertText("%") }, type = ButtonType.OPERATOR, modifier = Modifier.weight(1f), height = 48.dp, hapticsEnabled = hapticsEnabled)
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            CalcButton(text = "4", onClick = { onInsertText("4") }, type = ButtonType.NUMBER, modifier = Modifier.weight(1f), height = 48.dp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "5", onClick = { onInsertText("5") }, type = ButtonType.NUMBER, modifier = Modifier.weight(1f), height = 48.dp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "6", onClick = { onInsertText("6") }, type = ButtonType.NUMBER, modifier = Modifier.weight(1f), height = 48.dp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "×", onClick = { onInsertText("*") }, type = ButtonType.OPERATOR, modifier = Modifier.weight(1f), height = 48.dp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "π", onClick = { onInsertText("π") }, type = ButtonType.VARIABLE, modifier = Modifier.weight(1f), height = 48.dp, hapticsEnabled = hapticsEnabled)
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            CalcButton(text = "1", onClick = { onInsertText("1") }, type = ButtonType.NUMBER, modifier = Modifier.weight(1f), height = 48.dp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "2", onClick = { onInsertText("2") }, type = ButtonType.NUMBER, modifier = Modifier.weight(1f), height = 48.dp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "3", onClick = { onInsertText("3") }, type = ButtonType.NUMBER, modifier = Modifier.weight(1f), height = 48.dp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "-", onClick = { onInsertText("-") }, type = ButtonType.OPERATOR, modifier = Modifier.weight(1f), height = 48.dp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "e", onClick = { onInsertText("e") }, type = ButtonType.VARIABLE, modifier = Modifier.weight(1f), height = 48.dp, hapticsEnabled = hapticsEnabled)
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            CalcButton(text = "0", onClick = { onInsertText("0") }, type = ButtonType.NUMBER, modifier = Modifier.weight(1f), height = 48.dp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = ".", onClick = { onInsertText(".") }, type = ButtonType.NUMBER, modifier = Modifier.weight(1f), height = 48.dp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "⌫", onClick = onDeleteBackward, type = ButtonType.SECONDARY, modifier = Modifier.weight(1f), height = 48.dp, testTag = "btn_backspace", hapticsEnabled = hapticsEnabled)
            CalcButton(text = "+", onClick = { onInsertText("+") }, type = ButtonType.OPERATOR, modifier = Modifier.weight(1f), height = 48.dp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "=", onClick = onEvaluate, type = ButtonType.EQUAL, modifier = Modifier.weight(1f), height = 48.dp, testTag = "btn_equals", hapticsEnabled = hapticsEnabled)
        }
    }
}

@Composable
private fun ComplexKeypad(
    onInsertText: (String) -> Unit,
    onSmartParenthesis: () -> Unit,
    onDeleteBackward: () -> Unit,
    onClearAll: () -> Unit,
    onEvaluate: () -> Unit,
    hapticsEnabled: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            CalcButton(text = "i", onClick = { onInsertText("i") }, type = ButtonType.VARIABLE, modifier = Modifier.weight(1f), height = 48.dp, fontSize = 18.sp, subText = "√-1", hapticsEnabled = hapticsEnabled)
            CalcButton(text = "|z|", onClick = { onInsertText("abs(") }, type = ButtonType.FUNCTION, modifier = Modifier.weight(1f), height = 48.dp, fontSize = 15.sp, subText = "modulus", hapticsEnabled = hapticsEnabled)
            CalcButton(text = "arg", onClick = { onInsertText("arg(") }, type = ButtonType.FUNCTION, modifier = Modifier.weight(1f), height = 48.dp, fontSize = 15.sp, subText = "angle", hapticsEnabled = hapticsEnabled)
            CalcButton(text = "conj", onClick = { onInsertText("conj(") }, type = ButtonType.FUNCTION, modifier = Modifier.weight(1f), height = 48.dp, fontSize = 14.sp, subText = "z*", hapticsEnabled = hapticsEnabled)
            CalcButton(text = "Euler", onClick = { onInsertText("exp(i * π)") }, type = ButtonType.FUNCTION, modifier = Modifier.weight(1f), height = 48.dp, fontSize = 13.sp, subText = "e^(iπ)", hapticsEnabled = hapticsEnabled)
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            CalcButton(text = "Re", onClick = { onInsertText("re(") }, type = ButtonType.FUNCTION, modifier = Modifier.weight(1f), height = 48.dp, fontSize = 14.sp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "Im", onClick = { onInsertText("im(") }, type = ButtonType.FUNCTION, modifier = Modifier.weight(1f), height = 48.dp, fontSize = 14.sp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "^", onClick = { onInsertText("^") }, type = ButtonType.OPERATOR, modifier = Modifier.weight(1f), height = 48.dp, fontSize = 16.sp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "√", onClick = { onInsertText("sqrt(") }, type = ButtonType.FUNCTION, modifier = Modifier.weight(1f), height = 48.dp, fontSize = 16.sp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "AC", onClick = onClearAll, type = ButtonType.CLEAR, modifier = Modifier.weight(1f), height = 48.dp, fontSize = 15.sp, hapticsEnabled = hapticsEnabled)
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            CalcButton(text = "7", onClick = { onInsertText("7") }, type = ButtonType.NUMBER, modifier = Modifier.weight(1f), height = 48.dp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "8", onClick = { onInsertText("8") }, type = ButtonType.NUMBER, modifier = Modifier.weight(1f), height = 48.dp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "9", onClick = { onInsertText("9") }, type = ButtonType.NUMBER, modifier = Modifier.weight(1f), height = 48.dp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "(", onClick = { onInsertText("(") }, type = ButtonType.OPERATOR, modifier = Modifier.weight(1f), height = 48.dp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = ")", onClick = { onInsertText(")") }, type = ButtonType.OPERATOR, modifier = Modifier.weight(1f), height = 48.dp, hapticsEnabled = hapticsEnabled)
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            CalcButton(text = "4", onClick = { onInsertText("4") }, type = ButtonType.NUMBER, modifier = Modifier.weight(1f), height = 48.dp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "5", onClick = { onInsertText("5") }, type = ButtonType.NUMBER, modifier = Modifier.weight(1f), height = 48.dp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "6", onClick = { onInsertText("6") }, type = ButtonType.NUMBER, modifier = Modifier.weight(1f), height = 48.dp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "×", onClick = { onInsertText("*") }, type = ButtonType.OPERATOR, modifier = Modifier.weight(1f), height = 48.dp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "÷", onClick = { onInsertText("/") }, type = ButtonType.OPERATOR, modifier = Modifier.weight(1f), height = 48.dp, hapticsEnabled = hapticsEnabled)
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            CalcButton(text = "1", onClick = { onInsertText("1") }, type = ButtonType.NUMBER, modifier = Modifier.weight(1f), height = 48.dp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "2", onClick = { onInsertText("2") }, type = ButtonType.NUMBER, modifier = Modifier.weight(1f), height = 48.dp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "3", onClick = { onInsertText("3") }, type = ButtonType.NUMBER, modifier = Modifier.weight(1f), height = 48.dp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "+", onClick = { onInsertText("+") }, type = ButtonType.OPERATOR, modifier = Modifier.weight(1f), height = 48.dp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "-", onClick = { onInsertText("-") }, type = ButtonType.OPERATOR, modifier = Modifier.weight(1f), height = 48.dp, hapticsEnabled = hapticsEnabled)
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            CalcButton(text = "0", onClick = { onInsertText("0") }, type = ButtonType.NUMBER, modifier = Modifier.weight(1f), height = 48.dp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = ".", onClick = { onInsertText(".") }, type = ButtonType.NUMBER, modifier = Modifier.weight(1f), height = 48.dp, hapticsEnabled = hapticsEnabled)
            CalcButton(text = "=", onClick = { onInsertText(" = ") }, type = ButtonType.OPERATOR, modifier = Modifier.weight(1f), height = 48.dp, subText = "assign", hapticsEnabled = hapticsEnabled)
            CalcButton(text = "⌫", onClick = onDeleteBackward, type = ButtonType.SECONDARY, modifier = Modifier.weight(1f), height = 48.dp, testTag = "btn_backspace", hapticsEnabled = hapticsEnabled)
            CalcButton(text = "=", onClick = onEvaluate, type = ButtonType.EQUAL, modifier = Modifier.weight(1f), height = 48.dp, testTag = "btn_equals", hapticsEnabled = hapticsEnabled)
        }
    }
}
