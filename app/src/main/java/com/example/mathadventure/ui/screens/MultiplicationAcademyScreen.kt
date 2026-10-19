package com.example.mathadventure.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mathadventure.data.model.MathOperation
import com.example.mathadventure.localization.AppStrings
import com.example.mathadventure.localization.MathFormatter
import com.example.mathadventure.ui.components.NumberLineView
import com.example.mathadventure.ui.components.RobotMascotView
import com.example.mathadventure.ui.components.VisualArrayView
import com.example.mathadventure.ui.viewmodel.MathAdventureViewModel
import com.example.mathadventure.ui.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiplicationAcademyScreen(
    uiState: UiState,
    viewModel: MathAdventureViewModel,
    modifier: Modifier = Modifier
) {
    val strings = AppStrings(uiState.language)
    val scrollState = rememberScrollState()

    var selectedMethodIndex by remember { mutableIntStateOf(0) }
    var selectedTable by remember { mutableIntStateOf(3) }

    val methods = listOf(
        Pair(strings.multMethod1Title, strings.multMethod1Desc),
        Pair(strings.multMethod2Title, strings.multMethod2Desc),
        Pair(strings.multMethod3Title, strings.multMethod3Desc),
        Pair(strings.multMethod4Title, strings.multMethod4Desc),
        Pair(strings.multMethod5Title, strings.multMethod5Desc),
        Pair(strings.multMethod6Title, strings.multMethod6Desc),
        Pair(strings.multMethod7Title, strings.multMethod7Desc),
        Pair(strings.multMethod8Title, strings.multMethod8Desc),
        Pair(strings.multMethod9Title, strings.multMethod9Desc)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.multiplyAcademy, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier.testTag("mult_academy_back_btn")
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
                            viewModel.loadNewProblem(MathOperation.MULTIPLICATION, topicKey = "method_$idx")
                        },
                        label = { Text("${idx + 1}. ${pair.first.take(18)}...") },
                        modifier = Modifier.testTag("mult_method_chip_$idx"),
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
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = activeMethod.first,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E40AF)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = activeMethod.second,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF1E3A8A)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Method-Specific Interactive Demonstration
            when (selectedMethodIndex) {
                0 -> { // Concrete Equal Groups & Arrays
                    Text(
                        text = if (strings.isAr) "مصفوفة توضيحية تفاعلية: ٣ صفوف × ٤ أعمدة" else "Interactive Visual Array: 3 Rows × 4 Columns = 12",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    VisualArrayView(rows = 3, columns = 4, itemEmoji = "⭐")
                }
                1 -> { // Repeated Addition
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9))
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "4 × 3",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "3 + 3 + 3 + 3 = 12",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF16A34A)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (strings.isAr) "تم جمع العدد ٣ أربع مرات!" else "The number 3 was added 4 times!",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF64748B)
                            )
                        }
                    }
                }
                2 -> { // Number Line Jumps
                    Text(
                        text = if (strings.isAr) "قفزات متساوية للأمام: ٤ قفزات بحجم ٣" else "Equal Forward Hops: 4 leaps of size 3",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    NumberLineView(jumpCount = 4, stepSize = 3, format = uiState.numeralFormat)
                }
                3 -> { // Tables 0 to 12
                    Text(
                        text = if (strings.isAr) "اختر جدول الضرب للتمرين:" else "Select Multiplication Table:",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        (0..12).forEach { tableNum ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (selectedTable == tableNum) MaterialTheme.colorScheme.primary else Color(0xFFE2E8F0),
                                modifier = Modifier
                                    .clickable {
                                        selectedTable = tableNum
                                        viewModel.loadNewProblem(MathOperation.MULTIPLICATION, topicKey = "table_$tableNum")
                                    }
                                    .testTag("table_select_$tableNum")
                            ) {
                                Text(
                                    text = "×${MathFormatter.format(tableNum, uiState.numeralFormat)}",
                                    color = if (selectedTable == tableNum) Color.White else Color(0xFF334155),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    // Table rows display
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            (1..5).forEach { multiplier ->
                                val res = selectedTable * multiplier
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 2.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "${MathFormatter.format(selectedTable, uiState.numeralFormat)} × ${MathFormatter.format(multiplier, uiState.numeralFormat)}",
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "= ${MathFormatter.format(res, uiState.numeralFormat)}",
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
                4 -> { // Commutative Property
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7))
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "3 × 4 = 4 × 3 = 12",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF92400E)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (strings.isAr)
                                    "تبديل ترتيب الأعداد المضروبة لا يغير الناتج أبدًا!"
                                else
                                    "Changing the order of the factors never changes the product!",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF78350F)
                            )
                        }
                    }
                }
                5 -> { // Distributive Property
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E8FF))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "7 × 8 = (7 × 5) + (7 × 3)",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF6B21A8)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "= 35 + 21 = 56",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF16A34A)
                            )
                        }
                    }
                }
                6 -> { // Doubling & Halving
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFDCFCE7))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "4 × 8 = 2 × 16 = 32",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF166534)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (strings.isAr)
                                    "قسّم ٤ على ٢ = ٢، وضاعف ٨ إلى ١٦، ليصبح الحساب سهلاً للغاية!"
                                else
                                    "Halve 4 to 2, and double 8 to 16 for super easy mental math!",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF14532D)
                            )
                        }
                    }
                }
                7 -> { // Friendly Numbers & Compensation
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEDD5))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "9 × 7 = (10 × 7) − 7",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF9A3412)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "= 70 − 7 = 63",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF16A34A)
                            )
                        }
                    }
                }
                8 -> { // Powers of 10
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E7FF))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "6 × 10 = 60\n6 × 100 = 600\n6 × 1,000 = 6,000",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF3730A3)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Guided Practice Section
            val problem = uiState.currentProblem
            if (problem != null) {
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
                                text = if (strings.isAr) "تدريب عملي موجه" else "Guided Practice",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            IconButton(
                                onClick = { viewModel.loadNewProblem(MathOperation.MULTIPLICATION) }
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = Color(0xFF64748B))
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "${MathFormatter.format(problem.numA, uiState.numeralFormat)} × ${MathFormatter.format(problem.numB, uiState.numeralFormat)} = ?",
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
                                        .testTag("mult_guided_opt_$opt"),
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
                                onClick = { viewModel.loadNewProblem(MathOperation.MULTIPLICATION) },
                                modifier = Modifier.testTag("mult_guided_next_btn")
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
