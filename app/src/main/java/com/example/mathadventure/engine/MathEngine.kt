package com.example.mathadventure.engine

import com.example.mathadventure.data.model.LearningTier
import com.example.mathadventure.data.model.MathOperation
import kotlin.random.Random

enum class VisualRepresentationType {
    EQUAL_GROUPS,
    DOT_ARRAY,
    NUMBER_LINE_FORWARD,
    NUMBER_LINE_BACKWARD,
    FACT_FAMILY,
    STEP_BY_STEP_DIVISION,
    FRUIT_STAND,
    STANDARD
}

data class MathProblem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val operation: MathOperation,
    val numA: Int,
    val numB: Int,
    val correctAnswer: Int,
    val remainder: Int = 0,
    val missingPosition: MissingPosition = MissingPosition.RESULT,
    val visualType: VisualRepresentationType = VisualRepresentationType.STANDARD,
    val options: List<Int>,
    val explanationEn: String,
    val explanationAr: String,
    val hintEn: String,
    val hintAr: String
)

enum class MissingPosition {
    FIRST_FACTOR,   // ? × b = c
    SECOND_FACTOR,  // a × ? = c
    RESULT          // a × b = ?
}

data class FactFamily(
    val factorA: Int,
    val factorB: Int,
    val product: Int
) {
    val mult1 = "$factorA × $factorB = $product"
    val mult2 = "$factorB × $factorA = $product"
    val div1 = "$product ÷ $factorA = $factorB"
    val div2 = "$product ÷ $factorB = $factorA"
}

data class LongDivisionStep(
    val stepIndex: Int,
    val currentDividendPart: Int,
    val divisor: Int,
    val expectedQuotientDigit: Int,
    val expectedMultiplyResult: Int,
    val expectedRemainder: Int,
    val broughtDownDigit: Int?
)

object DeterministicMathEngine {

    fun generateProblem(
        operation: MathOperation,
        tier: LearningTier,
        topicKey: String? = null,
        forcedA: Int? = null,
        forcedB: Int? = null
    ): MathProblem {
        val rand = Random(System.nanoTime())
        val actualOp = if (operation == MathOperation.MIXED) {
            if (rand.nextBoolean()) MathOperation.MULTIPLICATION else MathOperation.DIVISION
        } else {
            operation
        }

        return when (actualOp) {
            MathOperation.MULTIPLICATION -> generateMultiplication(tier, topicKey, forcedA, forcedB, rand)
            MathOperation.DIVISION -> generateDivision(tier, topicKey, forcedA, forcedB, rand)
            else -> generateMultiplication(tier, topicKey, forcedA, forcedB, rand)
        }
    }

    private fun generateMultiplication(
        tier: LearningTier,
        topicKey: String?,
        forcedA: Int?,
        forcedB: Int?,
        rand: Random
    ): MathProblem {
        val a: Int
        val b: Int

        if (forcedA != null && forcedB != null) {
            a = forcedA
            b = forcedB
        } else if (topicKey != null && topicKey.startsWith("table_")) {
            val tableNum = topicKey.removePrefix("table_").toIntOrNull() ?: 3
            a = tableNum
            b = when (tier) {
                LearningTier.BEGINNER -> rand.nextInt(1, 6)
                LearningTier.INTERMEDIATE -> rand.nextInt(0, 13)
                LearningTier.EXPERT -> rand.nextInt(2, 16)
            }
        } else {
            when (tier) {
                LearningTier.BEGINNER -> {
                    a = rand.nextInt(1, 6)
                    b = rand.nextInt(1, 6)
                }
                LearningTier.INTERMEDIATE -> {
                    a = rand.nextInt(2, 13)
                    b = rand.nextInt(2, 13)
                }
                LearningTier.EXPERT -> {
                    if (rand.nextBoolean()) {
                        // Multi-digit by 1 digit or powers of 10
                        a = listOf(10, 100, 11, 12, 14, 25, 50).random(rand)
                        b = rand.nextInt(2, 10)
                    } else {
                        a = rand.nextInt(6, 15)
                        b = rand.nextInt(6, 15)
                    }
                }
            }
        }

        val answer = a * b
        val options = generateOptions(answer, rand)

        val visualType = when {
            a <= 5 && b <= 5 -> VisualRepresentationType.EQUAL_GROUPS
            a <= 7 && b <= 7 -> VisualRepresentationType.DOT_ARRAY
            else -> VisualRepresentationType.NUMBER_LINE_FORWARD
        }

        return MathProblem(
            operation = MathOperation.MULTIPLICATION,
            numA = a,
            numB = b,
            correctAnswer = answer,
            options = options,
            visualType = visualType,
            explanationEn = "$a × $b means $a groups of $b, which adds up to $answer! ($b " + (1 until a).joinToString(" + ") { "$b" } + " = $answer)",
            explanationAr = "$a × $b تعني $a مجموعات في كل منها $b، ومجموعها $answer! ($b " + (1 until a).joinToString(" + ") { "$b" } + " = $answer)",
            hintEn = "Try counting by $b's or using repeated addition: $b added $a times.",
            hintAr = "جرب العد بالقفزات بمقدار $b أو اجمع $b بعدد $a مرات."
        )
    }

    private fun generateDivision(
        tier: LearningTier,
        topicKey: String?,
        forcedA: Int?,
        forcedB: Int?,
        rand: Random
    ): MathProblem {
        val divisor: Int
        val quotient: Int
        var remainder = 0

        if (forcedA != null && forcedB != null) {
            val total = forcedA
            divisor = forcedB.coerceAtLeast(1)
            quotient = total / divisor
            remainder = total % divisor
            val options = generateOptions(quotient, rand)
            return MathProblem(
                operation = MathOperation.DIVISION,
                numA = total,
                numB = divisor,
                correctAnswer = quotient,
                remainder = remainder,
                options = options,
                visualType = VisualRepresentationType.NUMBER_LINE_BACKWARD,
                explanationEn = "$total ÷ $divisor gives quotient $quotient" + if (remainder > 0) " with remainder $remainder." else ".",
                explanationAr = "$total ÷ $divisor الناتج هو $quotient" + if (remainder > 0) " والباقي $remainder." else ".",
                hintEn = "Think: What number times $divisor is closest to $total without going over?",
                hintAr = "فكر: ما هو العدد الذي إذا ضربته في $divisor كان أقرب ما يكون إلى $total؟"
            )
        }

        val withRemainders = (tier == LearningTier.INTERMEDIATE && rand.nextFloat() < 0.3f) ||
                (tier == LearningTier.EXPERT && rand.nextFloat() < 0.5f)

        when (tier) {
            LearningTier.BEGINNER -> {
                divisor = rand.nextInt(2, 6)
                quotient = rand.nextInt(1, 6)
            }
            LearningTier.INTERMEDIATE -> {
                divisor = rand.nextInt(2, 10)
                quotient = rand.nextInt(2, 12)
                if (withRemainders) remainder = rand.nextInt(1, divisor)
            }
            LearningTier.EXPERT -> {
                divisor = rand.nextInt(3, 13)
                quotient = rand.nextInt(5, 25)
                if (withRemainders) remainder = rand.nextInt(1, divisor)
            }
        }

        val total = (divisor * quotient) + remainder
        val options = generateOptions(quotient, rand)

        val visualType = if (remainder == 0 && total <= 20) {
            VisualRepresentationType.EQUAL_GROUPS
        } else {
            VisualRepresentationType.NUMBER_LINE_BACKWARD
        }

        val remainderEn = if (remainder > 0) " with remainder $remainder" else ""
        val remainderAr = if (remainder > 0) " والباقي $remainder" else ""

        return MathProblem(
            operation = MathOperation.DIVISION,
            numA = total,
            numB = divisor,
            correctAnswer = quotient,
            remainder = remainder,
            options = options,
            visualType = visualType,
            explanationEn = "$total ÷ $divisor = $quotient$remainderEn because ($divisor × $quotient) + $remainder = $total.",
            explanationAr = "$total ÷ $divisor = $quotient$remainderAr لأن ($divisor × $quotient) + $remainder = $total.",
            hintEn = "Remember multiplication: $divisor × ? = ${total - remainder}",
            hintAr = "تذكر الضرب: $divisor × ؟ = ${total - remainder}"
        )
    }

    private fun generateOptions(correctAnswer: Int, rand: Random): List<Int> {
        val options = mutableSetOf(correctAnswer)
        val deltas = listOf(-2, -1, 1, 2, -3, 3, -10, 10, -5, 5).shuffled(rand)

        for (d in deltas) {
            val candidate = correctAnswer + d
            if (candidate >= 0 && candidate != correctAnswer) {
                options.add(candidate)
            }
            if (options.size == 4) break
        }

        while (options.size < 4) {
            val candidate = (correctAnswer + rand.nextInt(-15, 16)).coerceAtLeast(0)
            if (candidate != correctAnswer) options.add(candidate)
        }

        return options.toList().shuffled(rand)
    }

    fun buildFactFamily(a: Int, b: Int): FactFamily {
        return FactFamily(a, b, a * b)
    }

    fun getStepByStepLongDivision(dividend: Int, divisor: Int): List<LongDivisionStep> {
        val steps = mutableListOf<LongDivisionStep>()
        val dividendStr = dividend.toString()
        var currentPart = 0

        for (i in dividendStr.indices) {
            val digit = dividendStr[i] - '0'
            currentPart = currentPart * 10 + digit
            val quotientDigit = currentPart / divisor
            val multiplyRes = quotientDigit * divisor
            val rem = currentPart - multiplyRes

            steps.add(
                LongDivisionStep(
                    stepIndex = i,
                    currentDividendPart = currentPart,
                    divisor = divisor,
                    expectedQuotientDigit = quotientDigit,
                    expectedMultiplyResult = multiplyRes,
                    expectedRemainder = rem,
                    broughtDownDigit = digit
                )
            )
            currentPart = rem
        }
        return steps
    }
}
