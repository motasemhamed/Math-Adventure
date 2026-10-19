package com.example.mathadventure.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mathadventure.localization.AppStrings
import com.example.mathadventure.localization.MathFormatter
import com.example.mathadventure.ui.components.RobotMascotView
import com.example.mathadventure.ui.viewmodel.MathAdventureViewModel
import com.example.mathadventure.ui.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MentalMathLabScreen(
    uiState: UiState,
    viewModel: MathAdventureViewModel,
    modifier: Modifier = Modifier
) {
    val strings = AppStrings(uiState.language)
    val scrollState = rememberScrollState()

    var sliderA by remember { mutableFloatStateOf(6f) }
    var sliderB by remember { mutableFloatStateOf(7f) }

    val intA = sliderA.toInt()
    val intB = sliderB.toInt()
    val product = intA * intB

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.mentalLab, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier.testTag("mental_lab_back_btn")
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
            RobotMascotView(
                message = if (strings.isAr)
                    "أهلاً بك في المعمل الذري للحساب الذهني! هنا نكتشف الحيل السحرية لحل أي مسألة في ثوانٍ!"
                else
                    "Welcome to the Quantum Mental Math Lab! Discover lightning shortcuts and fact family connections!"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Fact Family Interactive Triangle Simulator
            Text(
                text = if (strings.isAr) "مختبر عائلات الأعداد السحرية" else "Interactive Fact Family Triangle",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Triangle Top
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .background(Color(0xFF10B981), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = MathFormatter.format(product, uiState.numeralFormat),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Triangle Base
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .background(Color(0xFF0284C7), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = MathFormatter.format(intA, uiState.numeralFormat),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Text("🔗", fontSize = 28.sp)

                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .background(Color(0xFF7C3AED), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = MathFormatter.format(intB, uiState.numeralFormat),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // The 4 connected equations
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${MathFormatter.format(intA, uiState.numeralFormat)} × ${MathFormatter.format(intB, uiState.numeralFormat)} = ${MathFormatter.format(product, uiState.numeralFormat)}",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF047857),
                            fontSize = 18.sp
                        )
                        Text(
                            text = "${MathFormatter.format(intB, uiState.numeralFormat)} × ${MathFormatter.format(intA, uiState.numeralFormat)} = ${MathFormatter.format(product, uiState.numeralFormat)}",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF047857),
                            fontSize = 18.sp
                        )
                        Text(
                            text = "${MathFormatter.format(product, uiState.numeralFormat)} ÷ ${MathFormatter.format(intA, uiState.numeralFormat)} = ${MathFormatter.format(intB, uiState.numeralFormat)}",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0284C7),
                            fontSize = 18.sp
                        )
                        Text(
                            text = "${MathFormatter.format(product, uiState.numeralFormat)} ÷ ${MathFormatter.format(intB, uiState.numeralFormat)} = ${MathFormatter.format(intA, uiState.numeralFormat)}",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0284C7),
                            fontSize = 18.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Sliders to change numbers dynamically
                    Text(text = "Factor A: $intA", style = MaterialTheme.typography.labelMedium)
                    Slider(
                        value = sliderA,
                        onValueChange = { sliderA = it },
                        valueRange = 2f..12f,
                        steps = 9
                    )

                    Text(text = "Factor B: $intB", style = MaterialTheme.typography.labelMedium)
                    Slider(
                        value = sliderB,
                        onValueChange = { sliderB = it },
                        valueRange = 2f..12f,
                        steps = 9
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Mental Math Shortcuts Test-Tubes
            Text(
                text = if (strings.isAr) "أنابيب الحيل الذهنية السريعة" else "Mental Math Lightning Tricks",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            MentalTrickCard(
                emoji = "⚡",
                title = if (strings.isAr) "حيلة جدول ٩ السحرية" else "The 9s Finger / Power Trick",
                body = if (strings.isAr)
                    "للضرب في ٩: اضرب في ١٠ ثم اطرح العدد نفسه!\nمثال: ٩ × ٦ = (١٠ × ٦) − ٦ = ٦٠ − ٦ = ٥٤"
                else
                    "Multiply by 9 by multiplying by 10 and subtracting the factor!\nExample: 9 × 6 = (10 × 6) − 6 = 60 − 6 = 54",
                bgColor = Color(0xFFFEF3C7)
            )

            Spacer(modifier = Modifier.height(10.dp))

            MentalTrickCard(
                emoji = "🖐️",
                title = if (strings.isAr) "حيلة الضرب في ٥" else "Multiplying by 5 Shortcut",
                body = if (strings.isAr)
                    "الضرب في ٥ هو نفسه الضرب في ١٠ ثم قسمة الناتج على ٢!\nمثال: ١٤ × ٥ = (١٤ × ١٠) ÷ ٢ = ١٤٠ ÷ ٢ = ٧٠"
                else
                    "Multiply by 10, then divide by 2 in half!\nExample: 14 × 5 = (14 × 10) ÷ 2 = 140 ÷ 2 = 70",
                bgColor = Color(0xFFDBEAFE)
            )

            Spacer(modifier = Modifier.height(10.dp))

            MentalTrickCard(
                emoji = "🚀",
                title = if (strings.isAr) "حيلة الضرب في ١١" else "The 11s Expansion Trick",
                body = if (strings.isAr)
                    "لضرب عدد من رقمين في ١١، اجمع الرقمين وضعهما في المنتصف!\nمثال: ٣٥ × ١١: ٣ + ٥ = ٨، إذن الناتج ٣٨٥!"
                else
                    "For a 2-digit number times 11, add the two digits and place the sum in the middle!\nExample: 35 × 11: 3 + 5 = 8, so the answer is 385!",
                bgColor = Color(0xFFFCE7F3)
            )
        }
    }
}

@Composable
fun MentalTrickCard(
    emoji: String,
    title: String,
    body: String,
    bgColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(emoji, fontSize = 28.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = body, style = MaterialTheme.typography.bodySmall, lineHeight = 18.sp)
            }
        }
    }
}
