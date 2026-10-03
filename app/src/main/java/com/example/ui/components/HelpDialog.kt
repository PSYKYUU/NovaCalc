package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalCalcColors

data class MathExample(
    val category: String,
    val expression: String,
    val description: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpDialog(
    onLoadExample: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val colors = LocalCalcColors.current

    val examples = listOf(
        MathExample("Complex Numbers", "exp(i * π)", "Euler's identity: e^(i*pi) = -1"),
        MathExample("Complex Numbers", "sqrt(-4)", "Imaginary root: returns 2i"),
        MathExample("Complex Numbers", "(3 + 4i) * (1 - 2i)", "Complex number multiplication"),
        MathExample("Complex Numbers", "abs(3 + 4i)", "Complex modulus (magnitude = 5)"),
        MathExample("Complex Numbers", "arg(1 + i)", "Complex phase/angle in RAD or DEG"),
        MathExample("Complex Numbers", "conj(5 - 2i)", "Complex conjugate: 5 + 2i"),
        MathExample("Variables", "r = 5", "Assign 5 to radius variable 'r'"),
        MathExample("Variables", "area = π * r^2", "Use variables in formulas"),
        MathExample("Variables", "x = 3 + 2i", "Assign complex number to variable"),
        MathExample("Scientific", "sin(π / 6)", "Trigonometric evaluation (0.5 in RAD)"),
        MathExample("Scientific", "5! + 3^4", "Factorials & exponential powers"),
        MathExample("Scientific", "ln(e^3)", "Natural logarithm"),
        MathExample("Scientific", "log10(1000)", "Base-10 logarithm")
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = colors.surface,
        dragHandle = { BottomSheetDefaults.DragHandle(color = colors.textMuted) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .padding(horizontal = 20.dp)
                .padding(bottom = 16.dp)
                .testTag("help_sheet")
        ) {
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
                        imageVector = Icons.Default.HelpOutline,
                        contentDescription = null,
                        tint = colors.accentPrimary
                    )
                    Text(
                        text = "NovaCalc Guide & Examples",
                        color = colors.textPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = colors.textMuted)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Tap any example equation below to try it out immediately in the calculator.",
                color = colors.textMuted,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(examples) { ex ->
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = colors.surfaceElevated,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onLoadExample(ex.expression) }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (ex.category == "Complex Numbers") colors.accentSecondary.copy(alpha = 0.2f) else colors.keyOpBg
                                ) {
                                    Text(
                                        text = ex.category,
                                        color = if (ex.category == "Complex Numbers") colors.accentSecondary else colors.accentPrimary,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = ex.expression,
                                    color = colors.textPrimary,
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = ex.description,
                                    color = colors.textMuted,
                                    fontSize = 12.sp
                                )
                            }

                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Run example",
                                tint = colors.accentPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
