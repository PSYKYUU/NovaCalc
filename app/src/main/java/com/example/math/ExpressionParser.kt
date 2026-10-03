package com.example.math

import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.round

class ExpressionParser(
    private val isDegrees: Boolean = false,
    private val variables: Map<String, Complex> = emptyMap()
) {

    data class EvalResult(
        val value: Complex,
        val assignmentVar: String? = null,
        val isAssignment: Boolean = false,
        val formatted: String,
        val isComplex: Boolean
    )

    private val standardConstants = mapOf(
        "pi" to Complex.PI,
        "π" to Complex.PI,
        "e" to Complex.E,
        "tau" to Complex.TAU,
        "τ" to Complex.TAU,
        "phi" to Complex.PHI,
        "φ" to Complex.PHI,
        "i" to Complex.I,
        "j" to Complex.I
    )

    private val supportedFunctions = setOf(
        "sin", "cos", "tan",
        "asin", "acos", "atan",
        "sinh", "cosh", "tanh",
        "ln", "log", "log10", "log2",
        "exp", "sqrt", "cbrt",
        "abs", "arg", "conj",
        "re", "im", "deg", "rad",
        "floor", "ceil", "round"
    )

    sealed class Token {
        data class NumberToken(val value: Double) : Token()
        data class IdentifierToken(val name: String) : Token()
        data class OperatorToken(val char: Char) : Token()
        data class FunctionToken(val name: String) : Token()
        object LeftParen : Token()
        object RightParen : Token()
        object Comma : Token()
        object Assignment : Token()
        object Factorial : Token()
    }

    fun evaluate(expression: String): EvalResult {
        val trimmed = expression.trim()
        if (trimmed.isEmpty()) {
            throw IllegalArgumentException("Empty expression")
        }

        // Check for variable assignment: e.g. "x = 5 + 3" or "radius = 12"
        val equalsIndex = findAssignmentEquals(trimmed)
        if (equalsIndex != -1) {
            val varName = trimmed.substring(0, equalsIndex).trim()
            val exprPart = trimmed.substring(equalsIndex + 1).trim()

            if (!isValidIdentifier(varName)) {
                throw IllegalArgumentException("Invalid variable name '$varName'")
            }
            if (isReservedName(varName)) {
                throw IllegalArgumentException("'$varName' is a reserved constant or function")
            }

            val eval = evaluateExpression(exprPart)
            return EvalResult(
                value = eval,
                assignmentVar = varName,
                isAssignment = true,
                formatted = eval.format(deg = isDegrees),
                isComplex = !eval.isReal()
            )
        }

        val result = evaluateExpression(trimmed)
        return EvalResult(
            value = result,
            assignmentVar = null,
            isAssignment = false,
            formatted = result.format(deg = isDegrees),
            isComplex = !result.isReal()
        )
    }

    private fun findAssignmentEquals(expr: String): Int {
        var depth = 0
        for (i in expr.indices) {
            val c = expr[i]
            if (c == '(') depth++
            else if (c == ')') depth--
            else if (c == '=' && depth == 0) {
                // Ensure it's not ==
                val isDouble = (i > 0 && expr[i - 1] == '=') || (i + 1 < expr.length && expr[i + 1] == '=')
                if (!isDouble) return i
            }
        }
        return -1
    }

    private fun isValidIdentifier(name: String): Boolean {
        if (name.isEmpty()) return false
        val first = name[0]
        if (!first.isLetter() && first != '_' && first != 'π' && first != 'τ' && first != 'φ') return false
        for (c in name) {
            if (!c.isLetterOrDigit() && c != '_' && c != 'π' && c != 'τ' && c != 'φ') return false
        }
        return true
    }

    private fun isReservedName(name: String): Boolean {
        val lower = name.lowercase()
        return standardConstants.containsKey(lower) || supportedFunctions.contains(lower)
    }

    private fun evaluateExpression(exprStr: String): Complex {
        val rawTokens = tokenize(exprStr)
        val tokens = insertImplicitMultiplication(rawTokens)
        val parser = RecursiveParser(tokens, isDegrees, getVariableValue = { name ->
            val lower = name.lowercase()
            when {
                standardConstants.containsKey(lower) -> standardConstants[lower]!!
                variables.containsKey(name) -> variables[name]!!
                variables.containsKey(lower) -> variables[lower]!!
                else -> throw IllegalArgumentException("Undefined variable '$name'")
            }
        })
        val result = parser.parse()
        if (parser.hasMore()) {
            throw IllegalArgumentException("Unexpected token after expression: ${parser.peek()}")
        }
        return result
    }

    private fun tokenize(input: String): List<Token> {
        val tokens = mutableListOf<Token>()
        var i = 0
        val len = input.length

        while (i < len) {
            val c = input[i]
            when {
                c.isWhitespace() -> i++
                c == '(' -> {
                    tokens.add(Token.LeftParen)
                    i++
                }
                c == ')' -> {
                    tokens.add(Token.RightParen)
                    i++
                }
                c == ',' -> {
                    tokens.add(Token.Comma)
                    i++
                }
                c == '!' -> {
                    tokens.add(Token.Factorial)
                    i++
                }
                c == '=' -> {
                    tokens.add(Token.Assignment)
                    i++
                }
                c in "+-*/×÷^%" -> {
                    val normalized = when (c) {
                        '×' -> '*'
                        '÷' -> '/'
                        else -> c
                    }
                    tokens.add(Token.OperatorToken(normalized))
                    i++
                }
                c.isDigit() || c == '.' -> {
                    val start = i
                    var hasDot = c == '.'
                    i++
                    while (i < len && (input[i].isDigit() || (input[i] == '.' && !hasDot))) {
                        if (input[i] == '.') hasDot = true
                        i++
                    }
                    // Scientific notation: e.g. 1.2e5 or 1e-4
                    if (i < len && (input[i] == 'e' || input[i] == 'E')) {
                        if (i + 1 < len && (input[i + 1].isDigit() || input[i + 1] == '+' || input[i + 1] == '-')) {
                            i++ // skip e
                            if (input[i] == '+' || input[i] == '-') i++
                            while (i < len && input[i].isDigit()) {
                                i++
                            }
                        }
                    }
                    val numStr = input.substring(start, i)
                    val num = numStr.toDoubleOrNull()
                        ?: throw IllegalArgumentException("Invalid number format: '$numStr'")
                    tokens.add(Token.NumberToken(num))
                }
                c.isLetter() || c == '_' || c == 'π' || c == 'τ' || c == 'φ' || c == '√' -> {
                    if (c == '√') {
                        tokens.add(Token.FunctionToken("sqrt"))
                        i++
                        continue
                    }
                    val start = i
                    while (i < len && (input[i].isLetterOrDigit() || input[i] == '_' || input[i] == 'π' || input[i] == 'τ' || input[i] == 'φ')) {
                        i++
                    }
                    val ident = input.substring(start, i)
                    val lower = ident.lowercase()
                    if (supportedFunctions.contains(lower)) {
                        tokens.add(Token.FunctionToken(lower))
                    } else {
                        tokens.add(Token.IdentifierToken(ident))
                    }
                }
                else -> {
                    throw IllegalArgumentException("Unrecognized character: '$c'")
                }
            }
        }
        return tokens
    }

    /**
     * Inserts implicit multiplication tokens between:
     * - Number and Identifier / Function / LeftParen (e.g. 2x, 2sin(x), 2(3+4))
     * - RightParen and Number / Identifier / Function / LeftParen (e.g. (2+3)(4+5), (2)x)
     * - Identifier and LeftParen (if identifier is a variable, not a function)
     * - Variable and Variable (e.g. x y)
     */
    private fun insertImplicitMultiplication(tokens: List<Token>): List<Token> {
        if (tokens.size <= 1) return tokens
        val result = mutableListOf<Token>()

        for (idx in 0 until tokens.size - 1) {
            val curr = tokens[idx]
            val next = tokens[idx + 1]
            result.add(curr)

            val needMultiply = when (curr) {
                is Token.NumberToken -> {
                    next is Token.IdentifierToken ||
                    next is Token.FunctionToken ||
                    next is Token.LeftParen
                }
                is Token.IdentifierToken -> {
                    next is Token.NumberToken ||
                    next is Token.IdentifierToken ||
                    next is Token.FunctionToken ||
                    next is Token.LeftParen
                }
                is Token.RightParen -> {
                    next is Token.NumberToken ||
                    next is Token.IdentifierToken ||
                    next is Token.FunctionToken ||
                    next is Token.LeftParen
                }
                is Token.Factorial -> {
                    next is Token.NumberToken ||
                    next is Token.IdentifierToken ||
                    next is Token.FunctionToken ||
                    next is Token.LeftParen
                }
                else -> false
            }

            if (needMultiply) {
                result.add(Token.OperatorToken('*'))
            }
        }
        result.add(tokens.last())
        return result
    }

    /**
     * Recursive descent parser for operator precedence:
     * Add/Sub -> Mul/Div/Mod -> Unary (+/-) -> Power (^) -> Postfix (!) -> Primary (Number, Identifier, Function, Paren)
     */
    private class RecursiveParser(
        private val tokens: List<Token>,
        private val isDegrees: Boolean,
        private val getVariableValue: (String) -> Complex
    ) {
        private var pos = 0

        fun hasMore(): Boolean = pos < tokens.size
        fun peek(): Token? = if (hasMore()) tokens[pos] else null

        private fun advance(): Token = tokens[pos++]

        fun parse(): Complex {
            val res = parseExpression()
            return res
        }

        private fun parseExpression(): Complex {
            var left = parseTerm()

            while (hasMore()) {
                val tok = peek()
                if (tok is Token.OperatorToken && (tok.char == '+' || tok.char == '-')) {
                    advance()
                    val right = parseTerm()
                    left = if (tok.char == '+') left + right else left - right
                } else {
                    break
                }
            }
            return left
        }

        private fun parseTerm(): Complex {
            var left = parseUnary()

            while (hasMore()) {
                val tok = peek()
                if (tok is Token.OperatorToken && (tok.char == '*' || tok.char == '/' || tok.char == '%')) {
                    advance()
                    val right = parseUnary()
                    left = when (tok.char) {
                        '*' -> left * right
                        '/' -> left / right
                        '%' -> left % right
                        else -> left
                    }
                } else {
                    break
                }
            }
            return left
        }

        private fun parseUnary(): Complex {
            val tok = peek()
            if (tok is Token.OperatorToken) {
                if (tok.char == '-') {
                    advance()
                    return -parseUnary()
                } else if (tok.char == '+') {
                    advance()
                    return parseUnary()
                }
            }
            return parsePower()
        }

        private fun parsePower(): Complex {
            var left = parsePostfix()
            val tok = peek()
            if (tok is Token.OperatorToken && tok.char == '^') {
                advance()
                // Right associative: 2^3^2 = 2^(3^2)
                val right = parseUnary()
                left = left.pow(right)
            }
            return left
        }

        private fun parsePostfix(): Complex {
            var expr = parsePrimary()
            while (hasMore()) {
                val tok = peek()
                if (tok is Token.Factorial) {
                    advance()
                    expr = expr.factorial()
                } else {
                    break
                }
            }
            return expr
        }

        private fun parsePrimary(): Complex {
            if (!hasMore()) {
                throw IllegalArgumentException("Unexpected end of expression")
            }

            when (val tok = advance()) {
                is Token.NumberToken -> return Complex.real(tok.value)
                is Token.IdentifierToken -> return getVariableValue(tok.name)
                is Token.FunctionToken -> return evaluateFunction(tok.name)
                is Token.LeftParen -> {
                    val inner = parseExpression()
                    if (!hasMore() || advance() !is Token.RightParen) {
                        throw IllegalArgumentException("Missing closing parenthesis ')'")
                    }
                    return inner
                }
                else -> throw IllegalArgumentException("Unexpected token: $tok")
            }
        }

        private fun evaluateFunction(funcName: String): Complex {
            if (!hasMore() || peek() !is Token.LeftParen) {
                // Sqrt or function might be followed directly by number if user wrote sqrt 4
                // But standard is func(arg)
                throw IllegalArgumentException("Expected '(' after function '$funcName'")
            }
            advance() // skip '('
            val arg = parseExpression()
            if (!hasMore() || advance() !is Token.RightParen) {
                throw IllegalArgumentException("Missing closing ')' for function '$funcName'")
            }

            return when (funcName.lowercase()) {
                "sin" -> arg.sin(isDegrees)
                "cos" -> arg.cos(isDegrees)
                "tan" -> arg.tan(isDegrees)
                "asin" -> arg.asin(isDegrees)
                "acos" -> arg.acos(isDegrees)
                "atan" -> arg.atan(isDegrees)
                "sinh" -> arg.sinh()
                "cosh" -> arg.cosh()
                "tanh" -> arg.tanh()
                "ln" -> arg.ln()
                "log", "log10" -> arg.log10()
                "log2" -> arg.log2()
                "exp" -> arg.exp()
                "sqrt" -> arg.sqrt()
                "cbrt" -> arg.cbrt()
                "abs" -> arg.absComplex()
                "arg" -> arg.argComplex(isDegrees)
                "conj" -> arg.conj()
                "re" -> Complex.real(arg.re)
                "im" -> Complex.real(arg.im)
                "deg" -> Complex.real(Math.toDegrees(arg.re))
                "rad" -> Complex.real(Math.toRadians(arg.re))
                "floor" -> Complex(floor(arg.re), floor(arg.im))
                "ceil" -> Complex(ceil(arg.re), ceil(arg.im))
                "round" -> Complex(round(arg.re), round(arg.im))
                else -> throw IllegalArgumentException("Unknown function '$funcName'")
            }
        }
    }
}
