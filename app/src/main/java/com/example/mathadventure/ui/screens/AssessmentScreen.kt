package com.example.mathadventure.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mathadventure.data.model.LearningTier
import com.example.mathadventure.data.model.MathOperation
import com.example.mathadventure.engine.DeterministicMathEngine
import com.example.mathadventure.engine.MathProblem
import com.example.mathadventure.engine.SoundEffect
import com.example.mathadventure.localization.AppStrings
import com.example.mathadventure.localization.MathFormatter
import com.example.mathadventure.ui.viewmodel.MathAdventureViewModel
import com.example.mathadventure.ui.viewmodel.ScreenDestination
import com.example.mathadventure.ui.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssessmentScreen(
    uiState: UiState,
    viewModel: MathAdventureViewModel,
    modifier: Modifier = Modifier
) {
    val strings = AppStrings(uiState.language)
    val scrollState = rememberScrollState()

    var questionIndex by remember { mutableIntStateOf(0) }
    var correctCount by remember { mutableIntStateOf(0) }
    val totalQuestions = 5

    val currentQuizProblem = remember(questionIndex) {
        val tier = when (questionIndex) {
            0, 1 -> LearningTier.BEGINNER
            2, 3 -> LearningTier.INTERMEDIATE
            else -> LearningTier.EXPERT
        }
        val op = if (questionIndex % 2 == 0) MathOperation.MULTIPLICATION else MathOperation.DIVISION
        DeterministicMathEngine.generateProblem(op, tier)
    }

    var selectedAnswer by remember { mutableStateOf<Int?>(null) }
    var isSubmitted by remember { mutableStateOf(false) }
    var isQuizCompleted by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.testsAndQuizzes, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier.testTag("assessment_back_btn")
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!isQuizCompleted) {
                // Header progress
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (strings.isAr)
                            "السؤال ${MathFormatter.format(questionIndex + 1, uiState.numeralFormat)} من ${MathFormatter.format(totalQuestions, uiState.numeralFormat)}"
                        else
                            "Question ${questionIndex + 1} of $totalQuestions",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "⭐ ${MathFormatter.format(correctCount * 10, uiState.numeralFormat)}",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF854D0E)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { (questionIndex + 1) / totalQuestions.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                )

                Spacer(modifier = Modifier.height(20.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val opSymbol = if (currentQuizProblem.operation == MathOperation.MULTIPLICATION) "×" else "÷"
                        Text(
                            text = "${MathFormatter.format(currentQuizProblem.numA, uiState.numeralFormat)} $opSymbol ${MathFormatter.format(currentQuizProblem.numB, uiState.numeralFormat)} = ?",
                            fontSize = 34.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF0F172A)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            currentQuizProblem.options.forEach { opt ->
                                val isSelected = selectedAnswer == opt
                                val isCorrect = opt == currentQuizProblem.correctAnswer

                                val bg = when {
                                    !isSubmitted -> if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                                    isCorrect -> Color(0xFF10B981)
                                    isSelected -> Color(0xFFEF4444)
                                    else -> MaterialTheme.colorScheme.surfaceVariant
                                }

                                val textColor = when {
                                    !isSubmitted -> if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                    isCorrect || isSelected -> Color.White
                                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                                }

                                Button(
                                    onClick = {
                                        if (!isSubmitted) {
                                            selectedAnswer = opt
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(52.dp)
                                        .testTag("quiz_opt_$opt"),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = bg)
                                ) {
                                    Text(
                                        text = MathFormatter.format(opt, uiState.numeralFormat),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = textColor
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        if (!isSubmitted) {
                            Button(
                                onClick = {
                                    if (selectedAnswer != null) {
                                        isSubmitted = true
                                        val correct = selectedAnswer == currentQuizProblem.correctAnswer
                                        if (correct) {
                                            correctCount++
                                            viewModel.soundManager.playSound(SoundEffect.CORRECT_CHIME)
                                        } else {
                                            viewModel.soundManager.playSound(SoundEffect.TRY_AGAIN)
                                        }
                                    }
                                },
                                enabled = selectedAnswer != null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("quiz_submit_btn")
                            ) {
                                Text(strings.checkAnswer, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Text(
                                text = if (selectedAnswer == currentQuizProblem.correctAnswer) strings.correct else strings.niceTry,
                                fontWeight = FontWeight.Bold,
                                color = if (selectedAnswer == currentQuizProblem.correctAnswer) Color(0xFF15803D) else MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    if (questionIndex + 1 < totalQuestions) {
                                        questionIndex++
                                        selectedAnswer = null
                                        isSubmitted = false
                                    } else {
                                        isQuizCompleted = true
                                        viewModel.soundManager.playSound(SoundEffect.LEVEL_UP)
                                        // Update child tier based on diagnostic result
                                        val newTier = when {
                                            correctCount >= 4 -> LearningTier.EXPERT
                                            correctCount >= 2 -> LearningTier.INTERMEDIATE
                                            else -> LearningTier.BEGINNER
                                        }
                                        viewModel.updateProfileTier(newTier)
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("quiz_next_btn")
                            ) {
                                Text(if (questionIndex + 1 < totalQuestions) strings.next else strings.scoreReport)
                            }
                        }
                    }
                }
            } else {
                // Completed Score Report Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .background(Color(0xFFFEF08A), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🎓", fontSize = 36.sp)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = strings.scoreReport,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        val percentage = (correctCount * 100) / totalQuestions
                        Text(
                            text = "${MathFormatter.format(correctCount, uiState.numeralFormat)} / ${MathFormatter.format(totalQuestions, uiState.numeralFormat)} ($percentage%)",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (percentage >= 80) Color(0xFF16A34A) else Color(0xFFD97706)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = if (percentage >= 80) strings.masteryReached else strings.keepPracticing,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                viewModel.navigateTo(ScreenDestination.HOME)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("quiz_return_home_btn")
                        ) {
                            Text(strings.home, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
