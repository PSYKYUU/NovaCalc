package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.math.Complex
import com.example.math.ExpressionParser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("NovaCalc", appName)
    }

    @Test
    fun `expression parser evaluates complex math and variables`() {
        val vars = mapOf("x" to Complex.real(5.0), "y" to Complex.real(10.0))
        val parser = ExpressionParser(isDegrees = false, variables = vars)

        // Basic arithmetic with variables
        val res1 = parser.evaluate("2*x + y")
        assertEquals(20.0, res1.value.re, 1e-9)

        // Complex numbers
        val res2 = parser.evaluate("(1 + 2i) * (3 - 4i)")
        // (1*3 - 2*(-4)) + (1*(-4) + 2*3)i = (3 + 8) + (-4 + 6)i = 11 + 2i
        assertEquals(11.0, res2.value.re, 1e-9)
        assertEquals(2.0, res2.value.im, 1e-9)

        // Sqrt of negative
        val res3 = parser.evaluate("sqrt(-9)")
        assertEquals(0.0, res3.value.re, 1e-9)
        assertEquals(3.0, res3.value.im, 1e-9)
    }
}
