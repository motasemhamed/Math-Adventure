package com.example.mathadventure.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mathadventure.engine.DeterministicMathEngine
import com.example.mathadventure.localization.AppStrings
import com.example.mathadventure.localization.MathFormatter
import com.example.mathadventure.localization.NumeralFormat

@Composable
fun LongDivisionWorkbench(
    dividend: Int,
    divisor: Int,
    strings: AppStrings,
    numeralFormat: NumeralFormat,
    onCompleted: () -> Unit,
    modifier: Modifier = Modifier
) {
    val steps = remember(dividend, divisor) {
        DeterministicMathEngine.getStepByStepLongDivision(dividend, divisor)
    }

    var currentStepIndex by remember { mutableStateOf(0) }
    var userQuotientInput by remember { mutableStateOf("") }
    var feedbackMessage by remember { mutableStateOf("") }
    var isCurrentStepCorrect by remember { mutableStateOf(false) }

    val currentStep = steps.getOrNull(currentStepIndex)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = strings.divMethod7Title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Standard long division bracket display
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Quotient on top
                    Row(
                        modifier = Modifier.padding(bottom = 4.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (strings.isAr) "الناتج: " else "Quotient: ",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF64748B)
                        )
                        val revealedQuotient = steps.take(currentStepIndex + if (isCurrentStepCorrect) 1 else 0)
                            .joinToString("") { it.expectedQuotientDigit.toString() }
                        Text(
                            text = MathFormatter.format(revealedQuotient.ifEmpty { "?" }, numeralFormat),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF16A34A)
                        )
                    }

                    // Divisor and Dividend bracket
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = MathFormatter.format(divisor, numeralFormat),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Box(
                            modifier = Modifier
                                .border(
                                    width = 3.dp,
                                    color = Color(0xFF334155),
                                    shape = RoundedCornerShape(topStart = 8.dp)
                                )
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = MathFormatter.format(dividend, numeralFormat),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (currentStep != null) {
                Text(
                    text = if (strings.isAr)
                        "الخطوة ${MathFormatter.format(currentStepIndex + 1, numeralFormat)} من ${MathFormatter.format(steps.size, numeralFormat)}: كم مرة يتكرر ${MathFormatter.format(divisor, numeralFormat)} في ${MathFormatter.format(currentStep.currentDividendPart, numeralFormat)}؟"
                    else
                        "Step ${currentStepIndex + 1} of ${steps.size}: How many times does $divisor go into ${currentStep.currentDividendPart}?",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = userQuotientInput,
                        onValueChange = { userQuotientInput = it.filter { ch -> ch.isDigit() } },
                        modifier = Modifier
                            .width(120.dp)
                            .testTag("long_div_input"),
                        label = { Text(if (strings.isAr) "الرقم" else "Digit") },
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = {
                            val entered = userQuotientInput.toIntOrNull()
                            if (entered == currentStep.expectedQuotientDigit) {
                                isCurrentStepCorrect = true
                                feedbackMessage = if (strings.isAr)
                                    "أحسنت! ${MathFormatter.format(divisor, numeralFormat)} × ${MathFormatter.format(entered, numeralFormat)} = ${MathFormatter.format(currentStep.expectedMultiplyResult, numeralFormat)}، والباقي ${MathFormatter.format(currentStep.expectedRemainder, numeralFormat)}"
                                else
                                    "Great! $divisor × $entered = ${currentStep.expectedMultiplyResult}, remainder = ${currentStep.expectedRemainder}"
                            } else {
                                feedbackMessage = if (strings.isAr)
                                    "فكر مجددًا: جرب عددًا أصغر أو تحقق من جدول $divisor"
                                else
                                    "Try again: Check multiplication table for $divisor"
                            }
                        },
                        modifier = Modifier.testTag("long_div_check_btn")
                    ) {
                        Text(strings.checkAnswer)
                    }
                }

                if (feedbackMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = feedbackMessage,
                        color = if (isCurrentStepCorrect) Color(0xFF15803D) else MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (isCurrentStepCorrect) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            if (currentStepIndex + 1 < steps.size) {
                                currentStepIndex++
                                userQuotientInput = ""
                                feedbackMessage = ""
                                isCurrentStepCorrect = false
                            } else {
                                onCompleted()
                            }
                        },
                        modifier = Modifier.testTag("long_div_next_step_btn")
                    ) {
                        Text(if (currentStepIndex + 1 < steps.size) strings.next else strings.correct)
                    }
                }
            } else {
                Text(
                    text = if (strings.isAr) "اكتملت القسمة المطولة بنجاح تام!" else "Long Division Completed Successfully!",
                    color = Color(0xFF15803D),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    }
}
