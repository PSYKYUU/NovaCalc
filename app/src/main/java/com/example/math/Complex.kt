package com.example.math

import kotlin.math.*

/**
 * Robust Complex number implementation with complete arithmetic, powers,
 * exponential, logarithms, trigonometry, and formatting.
 */
data class Complex(val re: Double, val im: Double = 0.0) {

    companion object {
        val ZERO = Complex(0.0, 0.0)
        val ONE = Complex(1.0, 0.0)
        val I = Complex(0.0, 1.0)
        val PI = Complex(Math.PI, 0.0)
        val E = Complex(Math.E, 0.0)
        val TAU = Complex(2.0 * Math.PI, 0.0)
        val PHI = Complex((1.0 + sqrt(5.0)) / 2.0, 0.0)

        fun fromPolar(r: Double, thetaRad: Double): Complex {
            return Complex(r * cos(thetaRad), r * sin(thetaRad))
        }

        fun real(value: Double): Complex = Complex(value, 0.0)
    }

    operator fun plus(other: Complex): Complex = Complex(re + other.re, im + other.im)
    operator fun plus(other: Double): Complex = Complex(re + other, im)

    operator fun minus(other: Complex): Complex = Complex(re - other.re, im - other.im)
    operator fun minus(other: Double): Complex = Complex(re - other, im)

    operator fun unaryMinus(): Complex = Complex(-re, -im)
    operator fun unaryPlus(): Complex = this

    operator fun times(other: Complex): Complex =
        Complex(re * other.re - im * other.im, re * other.im + im * other.re)
    operator fun times(other: Double): Complex = Complex(re * other, im * other)

    operator fun div(other: Complex): Complex {
        val denom = other.re * other.re + other.im * other.im
        if (denom == 0.0) throw ArithmeticException("Division by zero")
        return Complex(
            (re * other.re + im * other.im) / denom,
            (im * other.re - re * other.im) / denom
        )
    }
    operator fun div(other: Double): Complex {
        if (other == 0.0) throw ArithmeticException("Division by zero")
        return Complex(re / other, im / other)
    }

    operator fun rem(other: Complex): Complex {
        if (other.isZero()) throw ArithmeticException("Division by zero in modulo")
        // Real modulo if both real
        if (isReal() && other.isReal()) {
            return Complex(re % other.re, 0.0)
        }
        val q = this / other
        val qFloor = Complex(floor(q.re), floor(q.im))
        return this - (other * qFloor)
    }

    fun abs(): Double = hypot(re, im)

    fun absComplex(): Complex = Complex(abs(), 0.0)

    fun arg(inDegrees: Boolean = false): Double {
        val angle = atan2(im, re)
        return if (inDegrees) Math.toDegrees(angle) else angle
    }

    fun argComplex(inDegrees: Boolean = false): Complex = Complex(arg(inDegrees), 0.0)

    fun conj(): Complex = Complex(re, -im)

    fun isReal(epsilon: Double = 1e-12): Boolean = kotlin.math.abs(im) <= epsilon

    fun isZero(epsilon: Double = 1e-12): Boolean =
        kotlin.math.abs(re) <= epsilon && kotlin.math.abs(im) <= epsilon

    fun exp(): Complex {
        val expRe = kotlin.math.exp(re)
        return Complex(expRe * cos(im), expRe * sin(im))
    }

    fun ln(): Complex {
        val r = abs()
        if (r == 0.0) throw ArithmeticException("Logarithm of zero is undefined")
        return Complex(kotlin.math.ln(r), atan2(im, re))
    }

    fun log10(): Complex = ln() / Complex(kotlin.math.ln(10.0), 0.0)

    fun log2(): Complex = ln() / Complex(kotlin.math.ln(2.0), 0.0)

    fun pow(other: Complex): Complex {
        if (isZero()) {
            return if (other.isZero()) ONE else ZERO
        }
        // z^w = exp(w * ln(z))
        return (other * ln()).exp()
    }

    fun pow(exponent: Double): Complex = pow(Complex(exponent, 0.0))

    fun sqrt(): Complex {
        if (isReal() && re >= 0.0) {
            return Complex(kotlin.math.sqrt(re), 0.0)
        }
        if (isReal() && re < 0.0) {
            return Complex(0.0, kotlin.math.sqrt(-re))
        }
        val r = abs()
        val gamma = kotlin.math.sqrt((r + re) / 2.0)
        val delta = (if (im >= 0.0) 1.0 else -1.0) * kotlin.math.sqrt((r - re) / 2.0)
        return Complex(gamma, delta)
    }

    fun cbrt(): Complex {
        return pow(Complex(1.0 / 3.0, 0.0))
    }

    // Trigonometric functions
    fun sin(isDegrees: Boolean = false): Complex {
        val z = if (isDegrees) toRadians() else this
        // sin(a + bi) = sin(a)cosh(b) + i cos(a)sinh(b)
        return Complex(
            kotlin.math.sin(z.re) * cosh(z.im),
            kotlin.math.cos(z.re) * sinh(z.im)
        )
    }

    fun cos(isDegrees: Boolean = false): Complex {
        val z = if (isDegrees) toRadians() else this
        // cos(a + bi) = cos(a)cosh(b) - i sin(a)sinh(b)
        return Complex(
            kotlin.math.cos(z.re) * cosh(z.im),
            -kotlin.math.sin(z.re) * sinh(z.im)
        )
    }

    fun tan(isDegrees: Boolean = false): Complex {
        val c = cos(isDegrees)
        if (c.isZero()) throw ArithmeticException("Tangent undefined (division by zero)")
        return sin(isDegrees) / c
    }

    fun asin(isDegrees: Boolean = false): Complex {
        // asin(z) = -i * ln(i*z + sqrt(1 - z^2))
        val inside = (I * this) + (ONE - (this * this)).sqrt()
        val res = -I * inside.ln()
        return if (isDegrees) res.toDegrees() else res
    }

    fun acos(isDegrees: Boolean = false): Complex {
        // acos(z) = pi/2 - asin(z)
        val halfPi = Complex(Math.PI / 2.0, 0.0)
        val res = halfPi - asin(false)
        return if (isDegrees) res.toDegrees() else res
    }

    fun atan(isDegrees: Boolean = false): Complex {
        // atan(z) = (i/2) * ln((1 - i*z)/(1 + i*z))
        val num = ONE - (I * this)
        val den = ONE + (I * this)
        val res = (I * 0.5) * (num / den).ln()
        return if (isDegrees) res.toDegrees() else res
    }

    fun sinh(): Complex = (exp() - (-this).exp()) * 0.5
    fun cosh(): Complex = (exp() + (-this).exp()) * 0.5
    fun tanh(): Complex {
        val ch = cosh()
        if (ch.isZero()) throw ArithmeticException("Tanh undefined (division by zero)")
        return sinh() / ch
    }

    fun factorial(): Complex {
        if (!isReal()) throw ArithmeticException("Factorial is only supported for real numbers")
        val n = re
        if (n < 0.0) throw ArithmeticException("Factorial undefined for negative numbers")
        if (n == floor(n) && n <= 170.0) {
            var res = 1.0
            val intN = n.toLong()
            for (k in 2..intN) {
                res *= k
            }
            return Complex(res, 0.0)
        }
        // Lanczos approximation for Gamma(n + 1)
        return Complex(gammaLanczos(n + 1.0), 0.0)
    }

    private fun gammaLanczos(z: Double): Double {
        val p = doubleArrayOf(
            676.5203681218851, -1259.1392167224028,
            771.32342877765313, -176.61502916214059,
            12.507343278686905, -0.13857109526572012,
            9.9843695780195716e-6, 1.5056327351493116e-7
        )
        var x = z - 1.0
        var a = 0.99999999999980993
        for (i in p.indices) {
            a += p[i] / (x + i + 1)
        }
        val t = x + p.size - 0.5
        return kotlin.math.sqrt(2.0 * Math.PI) * t.pow(x + 0.5) * kotlin.math.exp(-t) * a
    }

    private fun toRadians(): Complex = Complex(Math.toRadians(re), Math.toRadians(im))
    private fun toDegrees(): Complex = Complex(Math.toDegrees(re), Math.toDegrees(im))

    /**
     * Formats complex number neatly without awkward trailing zeroes or 1.0i.
     */
    fun format(decimals: Int = 8, polar: Boolean = false, deg: Boolean = false): String {
        if (polar) {
            val r = abs()
            val theta = arg(deg)
            val rStr = formatDouble(r, decimals)
            val thStr = formatDouble(theta, decimals)
            return if (deg) "$rStr ∠ $thStr°" else "$rStr ∠ ${thStr}rad"
        }

        val cleanRe = roundToEpsilon(re)
        val cleanIm = roundToEpsilon(im)

        if (cleanIm == 0.0) {
            return formatDouble(cleanRe, decimals)
        }
        if (cleanRe == 0.0) {
            return formatImagOnly(cleanIm, decimals)
        }

        val reStr = formatDouble(cleanRe, decimals)
        val sign = if (cleanIm > 0.0) " + " else " - "
        val absIm = kotlin.math.abs(cleanIm)
        val imStr = if (absIm == 1.0) "i" else "${formatDouble(absIm, decimals)}i"
        return "$reStr$sign$imStr"
    }

    private fun formatImagOnly(imVal: Double, decimals: Int): String {
        return when (imVal) {
            1.0 -> "i"
            -1.0 -> "-i"
            else -> "${formatDouble(imVal, decimals)}i"
        }
    }

    private fun roundToEpsilon(v: Double, eps: Double = 1e-12): Double {
        if (kotlin.math.abs(v) < eps) return 0.0
        val rounded = kotlin.math.round(v)
        if (kotlin.math.abs(v - rounded) < eps) return rounded
        return v
    }

    private fun formatDouble(d: Double, maxDecimals: Int): String {
        if (d.isNaN()) return "NaN"
        if (d.isInfinite()) return if (d > 0) "∞" else "-∞"
        if (d == 0.0) return "0"

        // Scientific notation if very large or tiny
        val absVal = kotlin.math.abs(d)
        if ((absVal >= 1e11 || absVal < 1e-5) && absVal != 0.0) {
            return String.format(java.util.Locale.US, "%.5e", d).replace("e+0", "e").replace("e+", "e").replace("e-0", "e-")
        }

        // Format standard number
        val rounded = kotlin.math.round(d)
        if (kotlin.math.abs(d - rounded) < 1e-11) {
            return rounded.toLong().toString()
        }

        val formatted = String.format(java.util.Locale.US, "%.${maxDecimals}f", d)
            .trimEnd('0')
            .trimEnd('.')
        return formatted
    }
}
