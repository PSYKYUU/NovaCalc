package com.example.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AlexCalcDatabase
import com.example.data.AlexCalcRepository
import com.example.data.CalculationHistoryEntity
import com.example.data.VariableEntity
import com.example.math.Complex
import com.example.math.ExpressionParser
import com.example.ui.theme.AppTheme
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class KeypadMode {
    BASIC,
    SCIENTIFIC,
    COMPLEX
}

enum class NumberDisplayFormat {
    STANDARD,
    SCIENTIFIC,
    POLAR
}

enum class ActiveModal {
    NONE,
    HISTORY,
    VARIABLES,
    HELP,
    SAVE_VARIABLE,
    SETTINGS,
    TERMS_AND_CONDITIONS,
    PRIVACY_POLICY,
    LICENSES,
    ABOUT
}

data class CalculatorUiState(
    val expression: String = "",
    val cursorPosition: Int = 0,
    val liveResult: String? = null,
    val finalResult: String? = null,
    val lastResultComplex: Complex? = null,
    val errorMessage: String? = null,
    val isDegreeMode: Boolean = false,
    val numberFormat: NumberDisplayFormat = NumberDisplayFormat.STANDARD,
    val keypadMode: KeypadMode = KeypadMode.BASIC,
    val activeModal: ActiveModal = ActiveModal.NONE,
    val isEvaluating: Boolean = false,
    val historySearchQuery: String = "",
    val pendingVarName: String = "",
    val pendingVarExpr: String = "",
    val appTheme: AppTheme = AppTheme.OBSIDIAN,
    val hapticsEnabled: Boolean = true,
    val precisionDecimals: Int = 8,
    val showQuickVariables: Boolean = false,
    val sharpCorners: Boolean = false
)

class AlexCalcViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("novacalc_settings", Context.MODE_PRIVATE)
    private val repository: AlexCalcRepository

    private val _uiState = MutableStateFlow(
        CalculatorUiState(
            appTheme = loadSavedTheme(),
            hapticsEnabled = prefs.getBoolean("haptics_enabled", true),
            precisionDecimals = prefs.getInt("precision_decimals", 8),
            isDegreeMode = prefs.getBoolean("is_degree_mode", false),
            showQuickVariables = prefs.getBoolean("show_quick_variables", false),
            sharpCorners = prefs.getBoolean("sharp_corners", false)
        )
    )
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    init {
        val database = AlexCalcDatabase.getDatabase(application, viewModelScope)
        repository = AlexCalcRepository(database.historyDao(), database.variableDao())
    }

    private fun loadSavedTheme(): AppTheme {
        val themeName = prefs.getString("app_theme", AppTheme.OBSIDIAN.name) ?: AppTheme.OBSIDIAN.name
        return try {
            AppTheme.valueOf(themeName)
        } catch (_: Exception) {
            AppTheme.OBSIDIAN
        }
    }

    fun setShowQuickVariables(show: Boolean) {
        prefs.edit().putBoolean("show_quick_variables", show).apply()
        _uiState.update { it.copy(showQuickVariables = show) }
    }

    fun setSharpCorners(sharp: Boolean) {
        prefs.edit().putBoolean("sharp_corners", sharp).apply()
        _uiState.update { it.copy(sharpCorners = sharp) }
    }

    val historyList: StateFlow<List<CalculationHistoryEntity>> =
        _uiState.flatMapLatest { state ->
            if (state.historySearchQuery.isBlank()) {
                repository.allHistory
            } else {
                repository.searchHistory(state.historySearchQuery.trim())
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val variablesList: StateFlow<List<VariableEntity>> =
        repository.allVariables.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private var currentVarMap: Map<String, Complex> = emptyMap()

    init {
        viewModelScope.launch {
            variablesList.collect { vars ->
                val map = mutableMapOf<String, Complex>()
                for (v in vars) {
                    map[v.name] = Complex(v.realPart, v.imagPart)
                }
                currentVarMap = map
                updateLivePreview(_uiState.value.expression, _uiState.value.isDegreeMode)
            }
        }
    }

    fun setAppTheme(theme: AppTheme) {
        prefs.edit().putString("app_theme", theme.name).apply()
        _uiState.update { it.copy(appTheme = theme) }
    }

    fun setHapticsEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("haptics_enabled", enabled).apply()
        _uiState.update { it.copy(hapticsEnabled = enabled) }
    }

    fun setPrecisionDecimals(decimals: Int) {
        val clamped = decimals.coerceIn(2, 14)
        prefs.edit().putInt("precision_decimals", clamped).apply()
        _uiState.update { it.copy(precisionDecimals = clamped) }
        updateLivePreview(_uiState.value.expression, _uiState.value.isDegreeMode)
    }

    fun setKeypadMode(mode: KeypadMode) {
        _uiState.update { it.copy(keypadMode = mode) }
    }

    fun setActiveModal(modal: ActiveModal) {
        _uiState.update { it.copy(activeModal = modal) }
    }

    fun toggleAngleMode() {
        val newDeg = !_uiState.value.isDegreeMode
        prefs.edit().putBoolean("is_degree_mode", newDeg).apply()
        _uiState.update { it.copy(isDegreeMode = newDeg) }
        updateLivePreview(_uiState.value.expression, newDeg)
    }

    fun cycleNumberFormat() {
        val current = _uiState.value.numberFormat
        val next = when (current) {
            NumberDisplayFormat.STANDARD -> NumberDisplayFormat.SCIENTIFIC
            NumberDisplayFormat.SCIENTIFIC -> NumberDisplayFormat.POLAR
            NumberDisplayFormat.POLAR -> NumberDisplayFormat.STANDARD
        }
        _uiState.update { it.copy(numberFormat = next) }
        updateLivePreview(_uiState.value.expression, _uiState.value.isDegreeMode)
    }

    fun setHistorySearchQuery(query: String) {
        _uiState.update { it.copy(historySearchQuery = query) }
    }

    fun setCursorPosition(pos: Int) {
        val len = _uiState.value.expression.length
        val validPos = pos.coerceIn(0, len)
        _uiState.update { it.copy(cursorPosition = validPos) }
    }

    fun insertText(insert: String) {
        val current = _uiState.value.expression
        val pos = _uiState.value.cursorPosition.coerceIn(0, current.length)

        val before = current.substring(0, pos)
        val after = current.substring(pos)
        val newExpr = before + insert + after
        val newPos = pos + insert.length

        _uiState.update {
            it.copy(
                expression = newExpr,
                cursorPosition = newPos,
                errorMessage = null
            )
        }
        updateLivePreview(newExpr, _uiState.value.isDegreeMode)
    }

    fun insertSmartParenthesis() {
        val current = _uiState.value.expression
        val pos = _uiState.value.cursorPosition.coerceIn(0, current.length)

        val openCount = current.count { it == '(' }
        val closeCount = current.count { it == ')' }

        val lastChar = if (pos > 0) current[pos - 1] else null
        val shouldClose = openCount > closeCount && lastChar != null &&
                (lastChar.isLetterOrDigit() || lastChar == ')' || lastChar == 'π' || lastChar == 'i')

        if (shouldClose) {
            insertText(")")
        } else {
            insertText("(")
        }
    }

    fun deleteBackward() {
        val current = _uiState.value.expression
        val pos = _uiState.value.cursorPosition.coerceIn(0, current.length)
        if (pos == 0) return

        val before = current.substring(0, pos)
        val funcs = listOf("asin(", "acos(", "atan(", "sinh(", "cosh(", "tanh(", "sqrt(", "cbrt(", "sin(", "cos(", "tan(", "log10(", "log2(", "abs(", "arg(", "conj(", "exp(", "ln(")
        var deleteCount = 1
        for (f in funcs) {
            if (before.endsWith(f)) {
                deleteCount = f.length
                break
            }
        }

        val newExpr = current.substring(0, pos - deleteCount) + current.substring(pos)
        val newPos = pos - deleteCount

        _uiState.update {
            it.copy(
                expression = newExpr,
                cursorPosition = newPos,
                errorMessage = null
            )
        }
        updateLivePreview(newExpr, _uiState.value.isDegreeMode)
    }

    fun clearAll() {
        _uiState.update {
            it.copy(
                expression = "",
                cursorPosition = 0,
                liveResult = null,
                finalResult = null,
                errorMessage = null
            )
        }
    }

    fun evaluate() {
        val expr = _uiState.value.expression.trim()
        if (expr.isEmpty()) return

        try {
            val parser = ExpressionParser(
                isDegrees = _uiState.value.isDegreeMode,
                variables = currentVarMap
            )
            val result = parser.evaluate(expr)
            val formatted = formatResult(result.value, _uiState.value.numberFormat, _uiState.value.isDegreeMode)

            viewModelScope.launch {
                if (result.isAssignment && result.assignmentVar != null) {
                    repository.saveVariable(
                        name = result.assignmentVar,
                        valueFormatted = formatted,
                        realPart = result.value.re,
                        imagPart = result.value.im,
                        isComplex = result.isComplex,
                        isConstant = false,
                        description = "User assigned variable"
                    )
                }

                repository.saveVariable(
                    name = "ans",
                    valueFormatted = formatted,
                    realPart = result.value.re,
                    imagPart = result.value.im,
                    isComplex = result.isComplex,
                    isConstant = false,
                    description = "Last calculated answer"
                )

                repository.saveHistory(
                    expression = expr,
                    resultFormatted = formatted,
                    realPart = result.value.re,
                    imagPart = result.value.im,
                    isComplex = result.isComplex,
                    angleMode = if (_uiState.value.isDegreeMode) "DEG" else "RAD"
                )
            }

            _uiState.update {
                it.copy(
                    finalResult = formatted,
                    lastResultComplex = result.value,
                    liveResult = null,
                    errorMessage = null
                )
            }

        } catch (e: Exception) {
            val friendlyMsg = formatErrorMessage(e)
            _uiState.update {
                it.copy(
                    errorMessage = friendlyMsg
                )
            }
        }
    }

    private fun updateLivePreview(expr: String, isDeg: Boolean) {
        val trimmed = expr.trim()
        if (trimmed.isEmpty()) {
            _uiState.update { it.copy(liveResult = null) }
            return
        }

        try {
            val openCount = trimmed.count { it == '(' }
            val closeCount = trimmed.count { it == ')' }
            val completedExpr = if (openCount > closeCount) {
                trimmed + ")".repeat(openCount - closeCount)
            } else {
                trimmed
            }

            val parser = ExpressionParser(isDegrees = isDeg, variables = currentVarMap)
            val eval = parser.evaluate(completedExpr)
            val formatted = formatResult(eval.value, _uiState.value.numberFormat, isDeg)
            _uiState.update { it.copy(liveResult = formatted) }
        } catch (_: Exception) {
            _uiState.update { it.copy(liveResult = null) }
        }
    }

    private fun formatResult(c: Complex, format: NumberDisplayFormat, deg: Boolean): String {
        val decimals = _uiState.value.precisionDecimals
        return when (format) {
            NumberDisplayFormat.STANDARD -> c.format(decimals = decimals, polar = false, deg = deg)
            NumberDisplayFormat.SCIENTIFIC -> {
                if (c.isReal()) {
                    String.format(java.util.Locale.US, "%.${(decimals - 3).coerceAtLeast(2)}e", c.re)
                } else {
                    "${String.format(java.util.Locale.US, "%.${(decimals - 4).coerceAtLeast(2)}e", c.re)} + ${String.format(java.util.Locale.US, "%.${(decimals - 4).coerceAtLeast(2)}e", c.im)}i"
                }
            }
            NumberDisplayFormat.POLAR -> c.format(decimals = (decimals - 2).coerceAtLeast(2), polar = true, deg = deg)
        }
    }

    private fun formatErrorMessage(e: Throwable): String {
        val msg = e.message ?: "Invalid expression"
        return when {
            msg.contains("Division by zero", ignoreCase = true) -> "Division by zero"
            msg.contains("Undefined variable", ignoreCase = true) -> msg
            msg.contains("Missing closing", ignoreCase = true) -> "Missing closing parenthesis ')'"
            msg.contains("Expected ')'", ignoreCase = true) -> "Mismatched parentheses"
            else -> msg
        }
    }

    fun useHistoryExpression(historyExpr: String) {
        _uiState.update {
            it.copy(
                expression = historyExpr,
                cursorPosition = historyExpr.length,
                activeModal = ActiveModal.NONE,
                errorMessage = null
            )
        }
        updateLivePreview(historyExpr, _uiState.value.isDegreeMode)
    }

    fun useHistoryResult(resultStr: String) {
        insertText(resultStr)
        _uiState.update { it.copy(activeModal = ActiveModal.NONE) }
    }

    fun toggleFavoriteHistory(id: Long) {
        viewModelScope.launch {
            repository.toggleFavorite(id)
        }
    }

    fun deleteHistory(id: Long) {
        viewModelScope.launch {
            repository.deleteHistory(id)
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            repository.clearHistory()
            repository.clearUserVariables()
            clearAll()
        }
    }

    fun showSaveVariableDialog(defaultExpr: String? = null) {
        val valExpr = defaultExpr ?: _uiState.value.finalResult ?: _uiState.value.liveResult ?: ""
        _uiState.update {
            it.copy(
                activeModal = ActiveModal.SAVE_VARIABLE,
                pendingVarName = "",
                pendingVarExpr = valExpr
            )
        }
    }

    fun setPendingVarName(name: String) {
        _uiState.update { it.copy(pendingVarName = name) }
    }

    fun setPendingVarExpr(expr: String) {
        _uiState.update { it.copy(pendingVarExpr = expr) }
    }

    fun savePendingVariable() {
        val name = _uiState.value.pendingVarName.trim()
        val expr = _uiState.value.pendingVarExpr.trim()
        if (name.isEmpty() || expr.isEmpty()) return

        viewModelScope.launch {
            try {
                val parser = ExpressionParser(
                    isDegrees = _uiState.value.isDegreeMode,
                    variables = currentVarMap
                )
                val eval = parser.evaluate(expr)
                val formatted = formatResult(eval.value, _uiState.value.numberFormat, _uiState.value.isDegreeMode)

                repository.saveVariable(
                    name = name,
                    valueFormatted = formatted,
                    realPart = eval.value.re,
                    imagPart = eval.value.im,
                    isComplex = eval.isComplex,
                    isConstant = false,
                    description = "Custom variable"
                )
                _uiState.update { it.copy(activeModal = ActiveModal.NONE, pendingVarName = "", pendingVarExpr = "") }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Could not save variable: ${e.message}") }
            }
        }
    }

    fun deleteVariable(name: String) {
        viewModelScope.launch {
            repository.deleteVariable(name)
        }
    }
}
