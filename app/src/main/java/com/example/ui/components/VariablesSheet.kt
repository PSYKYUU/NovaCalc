package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.VariableEntity
import com.example.ui.theme.LocalCalcColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VariablesSheet(
    variables: List<VariableEntity>,
    onInsertVariable: (String) -> Unit,
    onAddNewVariable: () -> Unit,
    onDeleteVariable: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val colors = LocalCalcColors.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = colors.surface,
        dragHandle = {
            BottomSheetDefaults.DragHandle(color = colors.textMuted)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.82f)
                .padding(horizontal = 20.dp)
                .padding(bottom = 16.dp)
                .testTag("variables_sheet")
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
                        imageVector = Icons.Default.DataArray,
                        contentDescription = null,
                        tint = colors.accentTertiary
                    )
                    Text(
                        text = "Variables & Constants",
                        color = colors.textPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = onAddNewVariable,
                    colors = ButtonDefaults.buttonColors(containerColor = colors.keyOpBg),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("btn_add_var_sheet")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = colors.accentPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add", color = colors.accentPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(variables, key = { it.name }) { v ->
                    VariableCard(
                        variable = v,
                        onInsert = { onInsertVariable(v.name) },
                        onDelete = { onDeleteVariable(v.name) },
                        onCopyValue = {
                            val clipboard =
                                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("Variable", v.valueFormatted))
                            Toast.makeText(context, "Copied ${v.valueFormatted}", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun VariableCard(
    variable: VariableEntity,
    onInsert: () -> Unit,
    onDelete: () -> Unit,
    onCopyValue: () -> Unit
) {
    val colors = LocalCalcColors.current
    val isSystem = variable.isConstant
    val isComplex = variable.isComplex || variable.name == "i"
    val accent = if (isComplex) colors.accentSecondary else if (isSystem) colors.accentPrimary else colors.accentTertiary

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = colors.surfaceElevated,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onInsert() }
            .testTag("var_card_${variable.name}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = accent.copy(alpha = 0.15f),
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = variable.name,
                            color = accent,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "= ${variable.valueFormatted}",
                            color = colors.textPrimary,
                            fontSize = 16.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.SemiBold
                        )
                        if (isSystem) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = colors.bg
                            ) {
                                Text(
                                    text = "CONST",
                                    color = colors.textMuted,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                    if (variable.description.isNotBlank()) {
                        Text(
                            text = variable.description,
                            color = colors.textMuted,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onCopyValue, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy value",
                        tint = colors.textMuted,
                        modifier = Modifier.size(16.dp)
                    )
                }

                if (!isSystem && variable.name != "ans") {
                    IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Delete variable",
                            tint = colors.error,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SaveVariableDialog(
    varName: String,
    varExpr: String,
    onNameChange: (String) -> Unit,
    onExprChange: (String) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit
) {
    val colors = LocalCalcColors.current

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = colors.surfaceElevated,
        title = {
            Text("Store Variable", color = colors.textPrimary, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Assign a value or expression to a custom variable (e.g. x = 2*pi, z = 3+4i)",
                    color = colors.textMuted,
                    fontSize = 13.sp
                )

                OutlinedTextField(
                    value = varName,
                    onValueChange = onNameChange,
                    label = { Text("Variable Name (e.g. x, radius, z1)", color = colors.textMuted) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colors.accentTertiary,
                        unfocusedBorderColor = colors.border,
                        focusedTextColor = colors.textPrimary,
                        unfocusedTextColor = colors.textPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_var_name")
                )

                OutlinedTextField(
                    value = varExpr,
                    onValueChange = onExprChange,
                    label = { Text("Value or Expression", color = colors.textMuted) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colors.accentPrimary,
                        unfocusedBorderColor = colors.border,
                        focusedTextColor = colors.textPrimary,
                        unfocusedTextColor = colors.textPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_var_expr")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onSave,
                enabled = varName.isNotBlank() && varExpr.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = colors.accentTertiary),
                modifier = Modifier.testTag("btn_confirm_save_var")
            ) {
                Text("Save", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = colors.textMuted)
            }
        }
    )
}
