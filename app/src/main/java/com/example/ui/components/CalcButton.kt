package com.example.ui.components

import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalCalcColors
import com.example.ui.theme.LocalCalcShapes

enum class ButtonType {
    NUMBER,
    OPERATOR,
    FUNCTION,
    VARIABLE,
    CLEAR,
    EQUAL,
    SECONDARY
}

@Composable
fun CalcButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    type: ButtonType = ButtonType.NUMBER,
    subText: String? = null,
    fontSize: TextUnit = 20.sp,
    height: Dp = 56.dp,
    testTag: String = "btn_$text",
    hapticsEnabled: Boolean = true
) {
    val view = LocalView.current
    val colors = LocalCalcColors.current
    val shapes = LocalCalcShapes.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Smooth responsive spring animation on press
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "button_scale"
    )

    val (bgBrush, textColor) = when (type) {
        ButtonType.NUMBER -> Brush.verticalGradient(
            listOf(colors.keyNumBg.copy(alpha = 0.95f), colors.keyNumBg)
        ) to colors.keyNumText

        ButtonType.OPERATOR -> Brush.verticalGradient(
            listOf(colors.keyOpBg.copy(alpha = 0.9f), colors.keyOpBg)
        ) to colors.keyOpText

        ButtonType.FUNCTION -> Brush.verticalGradient(
            listOf(colors.keyFnBg.copy(alpha = 0.9f), colors.keyFnBg)
        ) to colors.keyFnText

        ButtonType.VARIABLE -> Brush.verticalGradient(
            listOf(colors.keyVarBg.copy(alpha = 0.9f), colors.keyVarBg)
        ) to colors.keyVarText

        ButtonType.CLEAR -> Brush.verticalGradient(
            listOf(colors.keyClearBg.copy(alpha = 0.9f), colors.keyClearBg)
        ) to colors.keyClearText

        ButtonType.EQUAL -> Brush.linearGradient(
            listOf(colors.keyEqualBg, colors.accentPrimary)
        ) to colors.keyEqualText

        ButtonType.SECONDARY -> Brush.verticalGradient(
            listOf(colors.surfaceElevated, colors.surfaceElevated)
        ) to colors.textMuted
    }

    Box(
        modifier = modifier
            .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
            .height(height)
            .scale(scale)
            .clip(shapes.buttonShape)
            .background(bgBrush)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    if (hapticsEnabled) {
                        try {
                            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                        } catch (_: Exception) {}
                    }
                    onClick()
                }
            )
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                color = textColor,
                fontSize = fontSize,
                fontWeight = if (type == ButtonType.NUMBER || type == ButtonType.EQUAL) FontWeight.SemiBold else FontWeight.Medium,
                fontFamily = FontFamily.SansSerif
            )
            if (subText != null) {
                Text(
                    text = subText,
                    color = textColor.copy(alpha = 0.6f),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}
