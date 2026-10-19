package com.example.mathadventure.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mathadventure.data.model.MathOperation
import com.example.mathadventure.localization.AppStrings
import com.example.mathadventure.localization.MathFormatter
import com.example.mathadventure.ui.components.LongDivisionWorkbench
import com.example.mathadventure.ui.components.NumberLineView
import com.example.mathadventure.ui.components.VisualArrayView
import com.example.mathadventure.ui.viewmodel.MathAdventureViewModel
import com.example.mathadventure.ui.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DivisionAcademyScreen(
    uiState: UiState,
    viewModel: MathAdventureViewModel,
    modifier: Modifier = Modifier
) {
    val strings = AppStrings(uiState.language)
    val scrollState = rememberScrollState()
    var selectedMethodIndex by remember { mutableIntStateOf(0) }

    val methods = listOf(
        Pair(strings.divMethod1Title, strings.divMethod1Desc),
        Pair(strings.divMethod2Title, strings.divMethod2Desc),
        Pair(strings.divMethod3Title, strings.divMethod3Desc),
        Pair(strings.divMethod4Title, strings.divMethod4Desc),
        Pair(strings.divMethod5Title, strings.divMethod5Desc),
        Pair(strings.divMethod6Title, strings.divMethod6Desc),
        Pair(strings.divMethod7Title, strings.divMethod7Desc),
        Pair(strings.divMethod8Title, strings.divMethod8Desc),
        Pair(strings.divMethod9Title, strings.divMethod9Desc),
        Pair(strings.divMethod10Title, strings.divMethod10Desc)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.divideAcademy, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier.testTag("div_academy_back_btn")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = strings.back)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Method Chips Carousel
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                methods.forEachIndexed { idx, pair ->
                    FilterChip(
                        selected = selectedMethodIndex == idx,
                        onClick = {
                            selectedMethodIndex = idx
                            viewModel.loadNewProblem(MathOperation.DIVISION, topicKey = "div_method_$idx")
                        },
                        label = { Text("${idx + 1}. ${pair.first.take(18)}...") },
                        modifier = Modifier.testTag("div_method_chip_$idx"),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Explanation Card
            val activeMethod = methods[selectedMethodIndex]
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFAF5FF))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = activeMethod.first,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6B21A8)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = activeMethod.second,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF581C87)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Method Interactive Demonstration
            when (selectedMethodIndex) {
                0 -> { // Equal Sharing
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "12 ÷ 3 = 4",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (strings.isAr)
                                    "١٢ قطعة حلوى موزعة بالتساوي على ٣ أصدقاء = ٤ لكل صديق بالتساوي!"
                                else
                                    "12 treats shared equally among 3 friends = 4 treats each!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF475569)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                (1..3).forEach {
                                    Card(
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9))
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(10.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(if (it == 1) "🐼" else if (it == 2) "🦊" else "🦉", fontSize = 28.sp)
                                            Text("🍬🍬\n🍬🍬", fontSize = 16.sp)
                                            Text("4", fontWeight = FontWeight.Bold, color = Color(0xFF16A34A))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                1 -> { // Equal Grouping
                    Text(
                        text = if (strings.isAr) "تجميع ١٢ نجمة في مجموعات من ٣: نكون ٤ مجموعات" else "12 stars grouped into sets of 3 = 4 groups",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    VisualArrayView(rows = 4, columns = 3, itemEmoji = "⭐")
                }
                2 -> { // Repeated Subtraction
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2))
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "12 − 3 = 9\n9 − 3 = 6\n6 − 3 = 3\n3 − 3 = 0",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF991B1B),
                                lineHeight = 28.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (strings.isAr) "طرحنا ٣ أربع مرات حتى وصلنا إلى الصفر، إذن ١٢ ÷ ٣ = ٤" else "Subtracted 3 four times to reach 0, so 12 ÷ 3 = 4",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF15803D)
                            )
                        }
                    }
                }
                3 -> { // Backward Number Line Jumps
                    Text(
                        text = if (strings.isAr) "قفزات عكسية للخلف من ١٢ بمقدار ٣" else "Backward Leaps from 12 in steps of 3",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    NumberLineView(jumpCount = 4, stepSize = 3, isBackward = true, format = uiState.numeralFormat)
                }
                4 -> { // Fact Families
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFECFDF5))
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "🔺 12 🔺\n3  ×  4",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF047857),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "3 × 4 = 12   ↔   4 × 3 = 12\n12 ÷ 3 = 4   ↔   12 ÷ 4 = 3",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF065F46)
                            )
                        }
                    }
                }
                6 -> { // Step-by-Step Long Division Workbench
                    LongDivisionWorkbench(
                        dividend = 96,
                        divisor = 4,
                        strings = strings,
                        numeralFormat = uiState.numeralFormat,
                        onCompleted = {
                            viewModel.showCelebration(stars = 10, coins = 20, message = strings.divMethod7Title)
                        }
                    )
                }
                7 -> { // Division with Remainders
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB))
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "14 ÷ 3 = 4 (R 2)",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFB45309)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (strings.isAr)
                                    "١٤ تفاحة موزعة على ٣ أطفال: كل طفل يأخذ ٤ تفاحات، ويبقى ٢ في السلة!"
                                else
                                    "14 apples shared among 3 children: 4 apples each, and 2 remain in the basket!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF92400E)
                            )
                        }
                    }
                }
                8 -> { // Divisibility Rules
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = if (strings.isAr) "قواعد سحرية سريعة:" else "Quick Divisibility Rules:",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF166534)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (strings.isAr)
                                    "• يقبل على ٢: إذا كان آحاده زوجيًا (٠، ٢، ٤، ٦، ٨)\n• يقبل على ٥: إذا كان آحاده ٠ أو ٥\n• يقبل على ١٠: إذا كان آحاده ٠\n• يقبل على ٣: إذا كان مجموع أرقامه يقبل على ٣"
                                else
                                    "• Divisible by 2: Last digit is even (0, 2, 4, 6, 8)\n• Divisible by 5: Last digit is 0 or 5\n• Divisible by 10: Last digit is 0\n• Divisible by 3: Sum of digits is divisible by 3",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF14532D)
                            )
                        }
                    }
                }
                else -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "15 ÷ 3 = ?",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = if (strings.isAr) "اسأل نفسك: ٣ × كم = ١٥؟ الجواب هو ٥!" else "Ask yourself: 3 × ? = 15. The answer is 5!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF334155)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Guided Division Practice
            val problem = uiState.currentProblem
            if (problem != null && problem.operation == MathOperation.DIVISION) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (strings.isAr) "تدريب عملي على القسمة" else "Division Practice",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            IconButton(
                                onClick = { viewModel.loadNewProblem(MathOperation.DIVISION) }
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = Color(0xFF64748B))
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "${MathFormatter.format(problem.numA, uiState.numeralFormat)} ÷ ${MathFormatter.format(problem.numB, uiState.numeralFormat)} = ?",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF0F172A)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            for (opt in problem.options) {
                                val isSelected = uiState.lastSelectedOption == opt
                                val isChecked = uiState.isAnswerChecked
                                val isCorrect = opt == problem.correctAnswer

                                val btnBg = when {
                                    !isChecked -> MaterialTheme.colorScheme.surfaceVariant
                                    isCorrect -> Color(0xFF10B981)
                                    isSelected -> Color(0xFFEF4444)
                                    else -> MaterialTheme.colorScheme.surfaceVariant
                                }

                                Button(
                                    onClick = {
                                        if (!uiState.isAnswerChecked) {
                                            viewModel.checkAnswer(opt)
                                        }
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(50.dp)
                                        .testTag("div_guided_opt_$opt"),
                                    colors = ButtonDefaults.buttonColors(containerColor = btnBg),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = MathFormatter.format(opt, uiState.numeralFormat),
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isChecked && (isCorrect || isSelected)) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        if (uiState.isAnswerChecked) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = if (uiState.isLastAnswerCorrect) strings.correct else strings.niceTry,
                                fontWeight = FontWeight.Bold,
                                color = if (uiState.isLastAnswerCorrect) Color(0xFF15803D) else MaterialTheme.colorScheme.error
                            )
                            Text(
                                text = if (strings.isAr) problem.explanationAr else problem.explanationEn,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF475569),
                                modifier = Modifier.padding(top = 4.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { viewModel.loadNewProblem(MathOperation.DIVISION) },
                                modifier = Modifier.testTag("div_guided_next_btn")
                            ) {
                                Text(strings.next)
                            }
                        }
                    }
                }
            }
        }
    }
}
