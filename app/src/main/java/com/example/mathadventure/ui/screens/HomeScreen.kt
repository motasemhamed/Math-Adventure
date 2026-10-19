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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mathadventure.data.model.MathOperation
import com.example.mathadventure.localization.AppLanguage
import com.example.mathadventure.localization.AppStrings
import com.example.mathadventure.localization.MathFormatter
import com.example.mathadventure.localization.NumeralFormat
import com.example.mathadventure.ui.components.ParentGateDialog
import com.example.mathadventure.ui.components.RobotMascotView
import com.example.mathadventure.ui.components.VisualArrayView
import com.example.mathadventure.ui.viewmodel.MathAdventureViewModel
import com.example.mathadventure.ui.viewmodel.ScreenDestination
import com.example.mathadventure.ui.viewmodel.UiState

@Composable
fun HomeScreen(
    uiState: UiState,
    viewModel: MathAdventureViewModel,
    modifier: Modifier = Modifier
) {
    val strings = AppStrings(uiState.language)
    val profile = uiState.activeProfile
    val scrollState = rememberScrollState()
    var showParentGate by remember { mutableStateOf(false) }

    if (showParentGate) {
        ParentGateDialog(
            strings = strings,
            numeralFormat = uiState.numeralFormat,
            onSuccess = {
                showParentGate = false
                viewModel.navigateTo(ScreenDestination.PARENT_ZONE)
            },
            onDismiss = { showParentGate = false }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Top Header Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile & Level Tag
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF0284C7)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🤖", fontSize = 24.sp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = profile?.name ?: "Cadet Sami",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (strings.isAr) profile?.tier?.displayNameAr ?: "" else profile?.tier?.displayNameEn ?: "",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF0284C7),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Rewards Bar & Action Icons
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Star Counter
                Surface(
                    color = Color(0xFFFEF9C3),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.clickable { viewModel.navigateTo(ScreenDestination.REWARDS) }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("⭐", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = MathFormatter.format(profile?.stars ?: 0, uiState.numeralFormat),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF854D0E)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Coin Counter
                Surface(
                    color = Color(0xFFFFEDD5),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.clickable { viewModel.navigateTo(ScreenDestination.REWARDS) }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🪙", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = MathFormatter.format(profile?.coins ?: 0, uiState.numeralFormat),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF9A3412)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Parent Zone Lock
                IconButton(
                    onClick = { showParentGate = true },
                    modifier = Modifier.testTag("home_parent_zone_btn")
                ) {
                    Icon(Icons.Default.Lock, contentDescription = strings.parentZone, tint = Color(0xFF475569))
                }

                // Settings
                IconButton(
                    onClick = { viewModel.navigateTo(ScreenDestination.SETTINGS) },
                    modifier = Modifier.testTag("home_settings_btn")
                ) {
                    Icon(Icons.Default.Settings, contentDescription = strings.settings, tint = Color(0xFF475569))
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Robot Mascot Banner
        RobotMascotView(message = strings.guideWelcome)

        Spacer(modifier = Modifier.height(16.dp))

        // Diagnostic Assessment Promotion Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { viewModel.navigateTo(ScreenDestination.ASSESSMENT) }
                .testTag("home_diagnostic_banner"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEEF2FF)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFF4F46E5), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🎯", fontSize = 24.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = strings.diagnosticTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF312E81)
                    )
                    Text(
                        text = strings.diagnosticDesc,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF4338CA)
                    )
                }
                Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color(0xFF4F46E5))
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 4 Main Educational Academies & Hubs
        Text(
            text = if (strings.isAr) "أكاديميات ومختبرات التعلم" else "Learning Academies & Labs",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Multiplication Hub
            AcademyPortalCard(
                title = strings.multiplyAcademy,
                subtitle = if (strings.isAr) "٩ طرق مرئية + الجداول" else "9 Visual Methods + Tables",
                iconEmoji = "✖️",
                gradient = listOf(Color(0xFF2563EB), Color(0xFF1D4ED8)),
                modifier = Modifier
                    .weight(1f)
                    .testTag("nav_multiplication_btn"),
                onClick = { viewModel.navigateTo(ScreenDestination.MULTIPLICATION_ACADEMY) }
            )

            // Division Hub
            AcademyPortalCard(
                title = strings.divideAcademy,
                subtitle = if (strings.isAr) "١٠ طرق + القسمة المطولة" else "10 Methods + Long Division",
                iconEmoji = "➗",
                gradient = listOf(Color(0xFF7C3AED), Color(0xFF6D28D9)),
                modifier = Modifier
                    .weight(1f)
                    .testTag("nav_division_btn"),
                onClick = { viewModel.navigateTo(ScreenDestination.DIVISION_ACADEMY) }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Mental Math & Fact Families
            AcademyPortalCard(
                title = strings.mentalLab,
                subtitle = if (strings.isAr) "حيل ذهنية وعائلات الأعداد" else "Shortcuts & Fact Families",
                iconEmoji = "🧪",
                gradient = listOf(Color(0xFF059669), Color(0xFF047857)),
                modifier = Modifier
                    .weight(1f)
                    .testTag("nav_mental_lab_btn"),
                onClick = { viewModel.navigateTo(ScreenDestination.MENTAL_MATH_LAB) }
            )

            // Game Arcade
            AcademyPortalCard(
                title = strings.gameArcade,
                subtitle = if (strings.isAr) "١٠ ألعاب تفاعلية شيقة" else "10 Exciting Mini-Games",
                iconEmoji = "🎮",
                gradient = listOf(Color(0xFFEA580C), Color(0xFFC2410C)),
                modifier = Modifier
                    .weight(1f)
                    .testTag("nav_games_btn"),
                onClick = { viewModel.navigateTo(ScreenDestination.GAME_ARCADE) }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Quick Interactive Warm-Up Challenge
        val problem = uiState.currentProblem
        if (problem != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
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
                            text = if (strings.isAr) "تحدي كوانتم السريع" else "Quantum Quick Spark",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        IconButton(
                            onClick = { viewModel.loadNewProblem(MathOperation.MIXED) }
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = Color(0xFF64748B))
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Equation display
                    val opSymbol = if (problem.operation == MathOperation.MULTIPLICATION) "×" else "÷"
                    val eqText = "${MathFormatter.format(problem.numA, uiState.numeralFormat)} $opSymbol ${MathFormatter.format(problem.numB, uiState.numeralFormat)} = ?"
                    Text(
                        text = eqText,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF0F172A)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Optional Mini Visual Array
                    if (problem.numA <= 4 && problem.numB <= 4) {
                        VisualArrayView(rows = problem.numA, columns = problem.numB)
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // 4 Options Grid
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

                            val btnText = when {
                                !isChecked -> MaterialTheme.colorScheme.onSurfaceVariant
                                isCorrect || isSelected -> Color.White
                                else -> MaterialTheme.colorScheme.onSurfaceVariant
                            }

                            Button(
                                onClick = {
                                    if (!uiState.isAnswerChecked) {
                                        viewModel.checkAnswer(opt)
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp)
                                    .testTag("home_quick_opt_$opt"),
                                colors = ButtonDefaults.buttonColors(containerColor = btnBg),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = MathFormatter.format(opt, uiState.numeralFormat),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = btnText
                                )
                            }
                        }
                    }

                    // Feedback and explanation
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
                            onClick = { viewModel.loadNewProblem(MathOperation.MIXED) },
                            modifier = Modifier.testTag("home_quick_next_btn")
                        ) {
                            Text(strings.next)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AcademyPortalCard(
    title: String,
    subtitle: String,
    iconEmoji: String,
    gradient: List<Color>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(130.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(gradient))
                .padding(14.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(iconEmoji, fontSize = 28.sp)
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.8f)
                    )
                }

                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }
        }
    }
}
