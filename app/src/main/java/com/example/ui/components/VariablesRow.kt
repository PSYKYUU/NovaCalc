package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DataArray
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.VariableEntity
import com.example.ui.theme.LocalCalcColors
import com.example.ui.theme.LocalCalcShapes

@Composable
fun VariablesRow(
    variables: List<VariableEntity>,
    onInsertVariable: (String) -> Unit,
    onOpenVariablesModal: () -> Unit,
    onStoreCurrent: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalCalcColors.current
    val shapes = LocalCalcShapes.current
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // "+ Store" button
        Surface(
            shape = shapes.pillShape,
            color = colors.keyOpBg,
            modifier = Modifier
                .height(36.dp)
                .clickable { onStoreCurrent() }
                .testTag("btn_store_var")
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Store variable",
                    tint = colors.accentPrimary,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "Store",
                    color = colors.accentPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // "Manage All" button
        Surface(
            shape = shapes.pillShape,
            color = colors.surfaceElevated,
            modifier = Modifier
                .height(36.dp)
                .clickable { onOpenVariablesModal() }
                .testTag("btn_manage_vars")
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DataArray,
                    contentDescription = "All variables",
                    tint = colors.textMuted,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "Vars (${variables.size})",
                    color = colors.textMuted,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Quick Variable Pills
        val quickVars = if (variables.isNotEmpty()) {
            variables.take(10)
        } else {
            listOf(
                VariableEntity("x", "0", 0.0, 0.0, false),
                VariableEntity("y", "0", 0.0, 0.0, false),
                VariableEntity("ans", "0", 0.0, 0.0, false),
                VariableEntity("π", "3.14159", Math.PI, 0.0, false, true),
                VariableEntity("i", "i", 0.0, 1.0, true, true),
                VariableEntity("e", "2.71828", Math.E, 0.0, false, true)
            )
        }

        for (v in quickVars) {
            val isComplex = v.isComplex || v.name == "i"
            val pillColor = if (isComplex) colors.keyFnBg else colors.keyVarBg
            val textColor = if (isComplex) colors.accentSecondary else colors.keyVarText

            Surface(
                shape = shapes.pillShape,
                color = pillColor,
                modifier = Modifier
                    .height(36.dp)
                    .clickable { onInsertVariable(v.name) }
                    .testTag("quick_var_${v.name}")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = v.name,
                        color = textColor,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "= ${v.valueFormatted}",
                        color = textColor.copy(alpha = 0.6f),
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        maxLines = 1
                    )
                }
            }
        }
    }
}
