package com.example.mathadventure.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import com.example.mathadventure.data.model.MathOperation
import com.example.mathadventure.engine.DeterministicMathEngine
import com.example.mathadventure.engine.SoundEffect
import com.example.mathadventure.localization.AppStrings
import com.example.mathadventure.localization.MathFormatter
import com.example.mathadventure.ui.viewmodel.MathAdventureViewModel
import com.example.mathadventure.ui.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveGameScreen(
    uiState: UiState,
    viewModel: MathAdventureViewModel,
    modifier: Modifier = Modifier
) {
    val strings = AppStrings(uiState.language)
    val gameId = uiState.activeGameId ?: "game_rocket"
    val scrollState = rememberScrollState()

    val gameTitle = when (gameId) {
        "game_rocket" -> strings.game1Title
        "game_fruit_shop" -> strings.game2Title
        "game_treasure" -> strings.game3Title
        "game_monster" -> strings.game4Title
        "game_bingo" -> strings.game5Title
        "game_fact_family" -> strings.game6Title
        "game_detective" -> strings.game7Title
        "game_bridge" -> strings.game8Title
        "game_daily" -> strings.game9Title
        "game_boss" -> strings.game10Title
        else -> strings.gameArcade
    }

    val animatedProgress by animateFloatAsState(
        targetValue = uiState.activeGameProgress,
        label = "game_progress"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(gameTitle, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier.testTag("active_game_back_btn")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = strings.back)
                    }
                },
                actions = {
                    Surface(
                        color = Color(0xFFFEF9C3),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("⭐", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = MathFormatter.format(uiState.activeGameScore, uiState.numeralFormat),
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF854D0E)
                            )
                        }
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
            // Progress Bar
            Column(modifier = Modifier.fillMaxWidth()) {
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = Color(0xFFE2E8F0),
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${(animatedProgress * 100).toInt()}%",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF64748B),
                    modifier = Modifier.align(Alignment.End)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Game Interactive Board by Game ID
            when (gameId) {
                "game_rocket" -> RocketMathView(uiState, viewModel, strings)
                "game_fruit_shop" -> PandaFruitShopView(uiState, viewModel, strings)
                "game_treasure" -> TreasureHuntView(uiState, viewModel, strings)
                "game_monster" -> MonsterBattleView(uiState, viewModel, strings)
                "game_bingo" -> MultiplicationBingoView(uiState, viewModel, strings)
                "game_fact_family" -> FactFamilyMatchView(uiState, viewModel, strings)
                "game_detective" -> MissingNumberDetectiveView(uiState, viewModel, strings)
                "game_bridge" -> BuildTheBridgeView(uiState, viewModel, strings)
                "game_daily" -> DailyChallengeView(uiState, viewModel, strings)
                "game_boss" -> QuantumBossView(uiState, viewModel, strings)
                else -> RocketMathView(uiState, viewModel, strings)
            }
        }
    }
}

// GAME 1: Rocket Math
@Composable
fun RocketMathView(uiState: UiState, viewModel: MathAdventureViewModel, strings: AppStrings) {
    val problem = uiState.currentProblem ?: return
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("🚀", fontSize = 60.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (strings.isAr) "تزويد الصاروخ بالوقود للإنطلاق!" else "Fuel the Thrusters to Launch!",
                color = Color(0xFF38BDF8),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "${MathFormatter.format(problem.numA, uiState.numeralFormat)} × ${MathFormatter.format(problem.numB, uiState.numeralFormat)} = ?",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            GameOptionsGrid(problem.options, problem.correctAnswer, uiState) { opt ->
                if (opt == problem.correctAnswer) {
                    viewModel.soundManager.playSound(SoundEffect.ROCKET_LAUNCH)
                    viewModel.updateGameProgress(scoreDelta = 10, progressDelta = 0.25f)
                    viewModel.loadNewProblem(MathOperation.MULTIPLICATION)
                } else {
                    viewModel.soundManager.playSound(SoundEffect.TRY_AGAIN)
                }
            }
        }
    }
}

// GAME 2: Panda's Fruit Shop
@Composable
fun PandaFruitShopView(uiState: UiState, viewModel: MathAdventureViewModel, strings: AppStrings) {
    val itemsCount = remember { (2..5).random() }
    val priceEach = remember { (2..6).random() }
    val totalPrice = itemsCount * priceEach
    val rand = remember { kotlin.random.Random(System.nanoTime()) }
    val options = remember(totalPrice) {
        listOf(totalPrice, totalPrice + 2, (totalPrice - 2).coerceAtLeast(1), totalPrice + 4).shuffled(rand)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFECFDF5))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("🐼 🛒", fontSize = 48.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (strings.isAr)
                    "اشترى الزبون $itemsCount سلال تفاح، في كل سلة $priceEach تفاحات. ما هو المجموع؟"
                else
                    "Customer bought $itemsCount fruit baskets with $priceEach fruits each. How many fruits total?",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF065F46)
            )

            Spacer(modifier = Modifier.height(16.dp))

            GameOptionsGrid(options, totalPrice, uiState) { opt ->
                if (opt == totalPrice) {
                    viewModel.soundManager.playSound(SoundEffect.COIN_COLLECT)
                    viewModel.updateGameProgress(scoreDelta = 15, progressDelta = 0.25f)
                    viewModel.loadNewProblem(MathOperation.MULTIPLICATION)
                } else {
                    viewModel.soundManager.playSound(SoundEffect.TRY_AGAIN)
                }
            }
        }
    }
}

// GAME 3: Treasure Hunt
@Composable
fun TreasureHuntView(uiState: UiState, viewModel: MathAdventureViewModel, strings: AppStrings) {
    val problem = uiState.currentProblem ?: return
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("💎 🔒", fontSize = 48.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = if (strings.isAr) "اختر المفتاح الذهبي المناسب لفتح قفل الكنز!" else "Choose the golden key to unlock the treasure chest!",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF92400E),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "${MathFormatter.format(problem.numA, uiState.numeralFormat)} × ${MathFormatter.format(problem.numB, uiState.numeralFormat)} = ?",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF78350F)
            )
            Spacer(modifier = Modifier.height(16.dp))
            GameOptionsGrid(problem.options, problem.correctAnswer, uiState) { opt ->
                if (opt == problem.correctAnswer) {
                    viewModel.soundManager.playSound(SoundEffect.COIN_COLLECT)
                    viewModel.updateGameProgress(scoreDelta = 20, progressDelta = 0.25f)
                    viewModel.loadNewProblem(MathOperation.MULTIPLICATION)
                } else {
                    viewModel.soundManager.playSound(SoundEffect.TRY_AGAIN)
                }
            }
        }
    }
}

// GAME 4: Monster Battle
@Composable
fun MonsterBattleView(uiState: UiState, viewModel: MathAdventureViewModel, strings: AppStrings) {
    var monsterHp by remember { mutableIntStateOf(3) }
    val problem = uiState.currentProblem ?: return

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFAF5FF))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(if (monsterHp > 1) "👾" else "🥰", fontSize = 54.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                repeat(3) { idx ->
                    Text(if (idx < monsterHp) "💜" else "🤍", fontSize = 20.sp)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (strings.isAr) "أطلق شعاع الطاقة التعليمي لترويض الوحش اللطيف!" else "Beam math power to tame the cute monster!",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF6B21A8)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "${MathFormatter.format(problem.numA, uiState.numeralFormat)} ÷ ${MathFormatter.format(problem.numB, uiState.numeralFormat)} = ?",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF581C87)
            )
            Spacer(modifier = Modifier.height(16.dp))
            GameOptionsGrid(problem.options, problem.correctAnswer, uiState) { opt ->
                if (opt == problem.correctAnswer) {
                    viewModel.soundManager.playSound(SoundEffect.CORRECT_CHIME)
                    monsterHp = (monsterHp - 1).coerceAtLeast(0)
                    viewModel.updateGameProgress(scoreDelta = 25, progressDelta = 0.34f)
                    viewModel.loadNewProblem(MathOperation.DIVISION)
                } else {
                    viewModel.soundManager.playSound(SoundEffect.TRY_AGAIN)
                }
            }
        }
    }
}

// GAME 5: Multiplication Bingo
@Composable
fun MultiplicationBingoView(uiState: UiState, viewModel: MathAdventureViewModel, strings: AppStrings) {
    val boardNumbers = remember { listOf(6, 12, 15, 20, 24, 30, 36, 42, 48) }
    val markedIndices = remember { mutableStateListOf<Int>() }
    val problem = uiState.currentProblem ?: return

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (strings.isAr) "ابحث عن الناتج في شبكة البينجو:" else "Find product on Bingo grid:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${MathFormatter.format(problem.numA, uiState.numeralFormat)} × ${MathFormatter.format(problem.numB, uiState.numeralFormat)} = ?",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(14.dp))

            // 3x3 Grid
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                for (row in 0..2) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (col in 0..2) {
                            val idx = row * 3 + col
                            val num = boardNumbers[idx]
                            val isMarked = markedIndices.contains(idx)

                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(56.dp)
                                    .clickable {
                                        if (num == problem.correctAnswer || !isMarked) {
                                            markedIndices.add(idx)
                                            viewModel.soundManager.playSound(SoundEffect.CORRECT_CHIME)
                                            viewModel.updateGameProgress(scoreDelta = 10, progressDelta = 0.2f)
                                            viewModel.loadNewProblem(MathOperation.MULTIPLICATION)
                                        }
                                    },
                                shape = RoundedCornerShape(12.dp),
                                color = if (isMarked) Color(0xFF10B981) else Color(0xFFF1F5F9),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1))
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = MathFormatter.format(num, uiState.numeralFormat),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isMarked) Color.White else Color(0xFF0F172A)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// GAME 6: Fact Family Link
@Composable
fun FactFamilyMatchView(uiState: UiState, viewModel: MathAdventureViewModel, strings: AppStrings) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFECFEFF))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("🔗", fontSize = 48.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (strings.isAr) "ما هي معادلة القسمة التوأم لـ ٥ × ٦ = ٣٠؟" else "What is the twin division fact for 5 × 6 = 30?",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color(0xFF0E7490)
            )
            Spacer(modifier = Modifier.height(16.dp))

            val options = listOf("30 ÷ 5 = 6", "30 ÷ 3 = 10", "30 − 5 = 25", "5 + 6 = 11")
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                options.forEach { opt ->
                    Button(
                        onClick = {
                            if (opt == "30 ÷ 5 = 6") {
                                viewModel.soundManager.playSound(SoundEffect.CORRECT_CHIME)
                                viewModel.updateGameProgress(scoreDelta = 20, progressDelta = 0.25f)
                            } else {
                                viewModel.soundManager.playSound(SoundEffect.TRY_AGAIN)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(opt, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// GAME 7: Missing Number Detective
@Composable
fun MissingNumberDetectiveView(uiState: UiState, viewModel: MathAdventureViewModel, strings: AppStrings) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFDF2F8))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("🔍 🕵️", fontSize = 48.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (strings.isAr) "ابحث عن العامل الناقص: ؟ × ٨ = ٥٦" else "Find missing factor: ? × 8 = 56",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFFBE185D)
            )
            Spacer(modifier = Modifier.height(16.dp))
            GameOptionsGrid(listOf(6, 7, 8, 9), 7, uiState) { opt ->
                if (opt == 7) {
                    viewModel.soundManager.playSound(SoundEffect.CORRECT_CHIME)
                    viewModel.updateGameProgress(scoreDelta = 15, progressDelta = 0.25f)
                } else {
                    viewModel.soundManager.playSound(SoundEffect.TRY_AGAIN)
                }
            }
        }
    }
}

// GAME 8: Build the Bridge
@Composable
fun BuildTheBridgeView(uiState: UiState, viewModel: MathAdventureViewModel, strings: AppStrings) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("🌉 🦊", fontSize = 48.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (strings.isAr) "ضع اللوح التالي في جسر الأعداد: ٤، ٨، ١٢، ؟" else "Place the next plank in sequence: 4, 8, 12, ?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF92400E)
            )
            Spacer(modifier = Modifier.height(16.dp))
            GameOptionsGrid(listOf(14, 15, 16, 18), 16, uiState) { opt ->
                if (opt == 16) {
                    viewModel.soundManager.playSound(SoundEffect.LEVEL_UP)
                    viewModel.updateGameProgress(scoreDelta = 20, progressDelta = 0.25f)
                } else {
                    viewModel.soundManager.playSound(SoundEffect.TRY_AGAIN)
                }
            }
        }
    }
}

// GAME 9: Daily Challenge
@Composable
fun DailyChallengeView(uiState: UiState, viewModel: MathAdventureViewModel, strings: AppStrings) {
    val problem = uiState.currentProblem ?: return
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("⭐ 📅", fontSize = 48.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (strings.isAr) "التحدي اليومي السريع!" else "Daily Quick Challenge!",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF166534)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "${MathFormatter.format(problem.numA, uiState.numeralFormat)} × ${MathFormatter.format(problem.numB, uiState.numeralFormat)} = ?",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF15803D)
            )
            Spacer(modifier = Modifier.height(16.dp))
            GameOptionsGrid(problem.options, problem.correctAnswer, uiState) { opt ->
                if (opt == problem.correctAnswer) {
                    viewModel.soundManager.playSound(SoundEffect.CORRECT_CHIME)
                    viewModel.updateGameProgress(scoreDelta = 20, progressDelta = 0.2f)
                    viewModel.loadNewProblem(MathOperation.MULTIPLICATION)
                } else {
                    viewModel.soundManager.playSound(SoundEffect.TRY_AGAIN)
                }
            }
        }
    }
}

// GAME 10: Boss Challenge
@Composable
fun QuantumBossView(uiState: UiState, viewModel: MathAdventureViewModel, strings: AppStrings) {
    val problem = uiState.currentProblem ?: return
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1B4B))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("👑 🤖 ⚡", fontSize = 48.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (strings.isAr) "تحدي الزعيم كوانتم الخارق!" else "Quantum Robot Boss Challenge!",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFBBF24)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "${MathFormatter.format(problem.numA, uiState.numeralFormat)} × ${MathFormatter.format(problem.numB, uiState.numeralFormat)} = ?",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            GameOptionsGrid(problem.options, problem.correctAnswer, uiState) { opt ->
                if (opt == problem.correctAnswer) {
                    viewModel.soundManager.playSound(SoundEffect.LEVEL_UP)
                    viewModel.updateGameProgress(scoreDelta = 50, progressDelta = 0.34f)
                    viewModel.loadNewProblem(MathOperation.MIXED)
                } else {
                    viewModel.soundManager.playSound(SoundEffect.TRY_AGAIN)
                }
            }
        }
    }
}

@Composable
fun GameOptionsGrid(
    options: List<Int>,
    correctAnswer: Int,
    uiState: UiState,
    onOptionSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (opt in options) {
            Button(
                onClick = { onOptionSelected(opt) },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .testTag("game_opt_$opt"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = MathFormatter.format(opt, uiState.numeralFormat),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
