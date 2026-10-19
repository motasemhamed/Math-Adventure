package com.example

import com.example.mathadventure.data.model.LearningTier
import com.example.mathadventure.data.model.MathOperation
import com.example.mathadventure.engine.DeterministicMathEngine
import com.example.mathadventure.localization.MathFormatter
import com.example.mathadventure.localization.NumeralFormat
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testMultiplicationCorrectness() {
        repeat(50) {
            val problem = DeterministicMathEngine.generateProblem(
                MathOperation.MULTIPLICATION,
                LearningTier.INTERMEDIATE
            )
            assertEquals(
                "Multiplication answer must match a * b",
                problem.numA * problem.numB,
                problem.correctAnswer
            )
            assertTrue("Options must contain correct answer", problem.options.contains(problem.correctAnswer))
            assertEquals("Must have exactly 4 choices", 4, problem.options.size)
        }
    }

    @Test
    fun testDivisionCorrectnessAndZeroProtection() {
        repeat(50) {
            val problem = DeterministicMathEngine.generateProblem(
                MathOperation.DIVISION,
                LearningTier.INTERMEDIATE
            )
            assertTrue("Divisor must never be zero", problem.numB > 0)
            val reconstructed = (problem.numB * problem.correctAnswer) + problem.remainder
            assertEquals(
                "Dividend must equal (divisor * quotient) + remainder",
                problem.numA,
                reconstructed
            )
            assertTrue("Remainder must be strictly less than divisor", problem.remainder < problem.numB)
        }
    }

    @Test
    fun testFactFamilyIntegrity() {
        val family = DeterministicMathEngine.buildFactFamily(6, 7)
        assertEquals(42, family.product)
        assertEquals(6, family.product / 7)
        assertEquals(7, family.product / 6)
    }

    @Test
    fun testStepByStepLongDivision() {
        val steps = DeterministicMathEngine.getStepByStepLongDivision(96, 4)
        // 96 / 4 = 24
        // Step 1: 9 / 4 = 2, mult = 8, rem = 1
        // Step 2: 16 / 4 = 4, mult = 16, rem = 0
        assertEquals(2, steps.size)
        assertEquals(2, steps[0].expectedQuotientDigit)
        assertEquals(4, steps[1].expectedQuotientDigit)
        assertEquals(0, steps[1].expectedRemainder)
    }

    @Test
    fun testNumeralFormatting() {
        val western = "12345"
        val eastern = MathFormatter.format(western, NumeralFormat.ARABIC_INDIC)
        assertEquals("١٢٣٤٥", eastern)

        val reverted = MathFormatter.format(12, NumeralFormat.WESTERN)
        assertEquals("12", reverted)
    }
}
