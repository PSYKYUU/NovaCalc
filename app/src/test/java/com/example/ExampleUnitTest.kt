package com.example

import com.example.math.Complex
import com.example.math.ExpressionParser
import org.junit.Assert.*
import org.junit.Test
import kotlin.math.PI

class ExampleUnitTest {

    @Test
    fun testComplexArithmetic() {
        val a = Complex(3.0, 4.0)
        val b = Complex(1.0, -2.0)

        val sum = a + b
        assertEquals(4.0, sum.re, 1e-9)
        assertEquals(2.0, sum.im, 1e-9)

        val mul = a * b
        // (3+4i)(1-2i) = 3 - 6i + 4i - 8i^2 = 11 - 2i
        assertEquals(11.0, mul.re, 1e-9)
        assertEquals(-2.0, mul.im, 1e-9)

        assertEquals(5.0, a.abs(), 1e-9)
    }

    @Test
    fun testEulerIdentity() {
        // e^(i * pi) = -1
        val parser = ExpressionParser()
        val res = parser.evaluate("exp(i * π)")
        assertEquals(-1.0, res.value.re, 1e-6)
        assertEquals(0.0, res.value.im, 1e-6)
    }

    @Test
    fun testVariableAssignmentAndImplicitMultiplication() {
        val parser = ExpressionParser(variables = mapOf("radius" to Complex.real(3.0)))
        val res = parser.evaluate("2 radius")
        assertEquals(6.0, res.value.re, 1e-9)

        val assign = parser.evaluate("x = 42")
        assertTrue(assign.isAssignment)
        assertEquals("x", assign.assignmentVar)
        assertEquals(42.0, assign.value.re, 1e-9)
    }
}
