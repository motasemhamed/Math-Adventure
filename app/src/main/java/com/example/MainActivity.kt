package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mathadventure.localization.AppStrings
import com.example.mathadventure.ui.components.CelebrationDialog
import com.example.mathadventure.ui.screens.ActiveGameScreen
import com.example.mathadventure.ui.screens.AssessmentScreen
import com.example.mathadventure.ui.screens.DivisionAcademyScreen
import com.example.mathadventure.ui.screens.GameArcadeScreen
import com.example.mathadventure.ui.screens.HomeScreen
import com.example.mathadventure.ui.screens.LanguageSelectionScreen
import com.example.mathadventure.ui.screens.MentalMathLabScreen
import com.example.mathadventure.ui.screens.MultiplicationAcademyScreen
import com.example.mathadventure.ui.screens.ParentZoneScreen
import com.example.mathadventure.ui.screens.RewardsScreen
import com.example.mathadventure.ui.screens.SettingsScreen
import com.example.mathadventure.ui.viewmodel.MathAdventureViewModel
import com.example.mathadventure.ui.viewmodel.ScreenDestination
import com.example.ui.theme.MathAdventureTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MathAdventureViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val strings = AppStrings(uiState.language)

            // Dynamic RTL / LTR layout direction
            CompositionLocalProvider(LocalLayoutDirection provides uiState.language.layoutDirection) {
                MathAdventureTheme {
                    // System Back Handler
                    BackHandler(enabled = uiState.currentScreen != ScreenDestination.HOME && uiState.currentScreen != ScreenDestination.LANGUAGE_ONBOARDING) {
                        viewModel.navigateBack()
                    }

                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = {
                            val showBottomBar = uiState.currentScreen != ScreenDestination.LANGUAGE_ONBOARDING &&
                                    uiState.currentScreen != ScreenDestination.ACTIVE_GAME

                            if (showBottomBar) {
                                NavigationBar(
                                    modifier = Modifier
                                        .navigationBarsPadding()
                                        .testTag("main_bottom_nav")
                                ) {
                                    NavigationBarItem(
                                        selected = uiState.currentScreen == ScreenDestination.HOME,
                                        onClick = { viewModel.navigateTo(ScreenDestination.HOME) },
                                        icon = { Icon(Icons.Default.Home, contentDescription = strings.home) },
                                        label = { Text(strings.home) },
                                        modifier = Modifier.testTag("nav_item_home")
                                    )
                                    NavigationBarItem(
                                        selected = uiState.currentScreen == ScreenDestination.MULTIPLICATION_ACADEMY,
                                        onClick = { viewModel.navigateTo(ScreenDestination.MULTIPLICATION_ACADEMY) },
                                        icon = { Text("✖️") },
                                        label = { Text(strings.multiplyAcademy) },
                                        modifier = Modifier.testTag("nav_item_multiply")
                                    )
                                    NavigationBarItem(
                                        selected = uiState.currentScreen == ScreenDestination.DIVISION_ACADEMY,
                                        onClick = { viewModel.navigateTo(ScreenDestination.DIVISION_ACADEMY) },
                                        icon = { Text("➗") },
                                        label = { Text(strings.divideAcademy) },
                                        modifier = Modifier.testTag("nav_item_divide")
                                    )
                                    NavigationBarItem(
                                        selected = uiState.currentScreen == ScreenDestination.GAME_ARCADE,
                                        onClick = { viewModel.navigateTo(ScreenDestination.GAME_ARCADE) },
                                        icon = { Icon(Icons.Default.SportsEsports, contentDescription = strings.gameArcade) },
                                        label = { Text(strings.gameArcade) },
                                        modifier = Modifier.testTag("nav_item_games")
                                    )
                                    NavigationBarItem(
                                        selected = uiState.currentScreen == ScreenDestination.REWARDS,
                                        onClick = { viewModel.navigateTo(ScreenDestination.REWARDS) },
                                        icon = { Icon(Icons.Default.EmojiEvents, contentDescription = strings.rewards) },
                                        label = { Text(strings.rewards) },
                                        modifier = Modifier.testTag("nav_item_rewards")
                                    )
                                }
                            }
                        }
                    ) { innerPadding ->
                        Crossfade(
                            targetState = uiState.currentScreen,
                            label = "screen_crossfade",
                            modifier = Modifier.padding(innerPadding)
                        ) { screen ->
                            when (screen) {
                                ScreenDestination.LANGUAGE_ONBOARDING -> LanguageSelectionScreen(uiState, viewModel)
                                ScreenDestination.HOME -> HomeScreen(uiState, viewModel)
                                ScreenDestination.MULTIPLICATION_ACADEMY -> MultiplicationAcademyScreen(uiState, viewModel)
                                ScreenDestination.DIVISION_ACADEMY -> DivisionAcademyScreen(uiState, viewModel)
                                ScreenDestination.FACT_FAMILY_LAB, ScreenDestination.MENTAL_MATH_LAB -> MentalMathLabScreen(uiState, viewModel)
                                ScreenDestination.GAME_ARCADE -> GameArcadeScreen(uiState, viewModel)
                                ScreenDestination.ACTIVE_GAME -> ActiveGameScreen(uiState, viewModel)
                                ScreenDestination.ASSESSMENT -> AssessmentScreen(uiState, viewModel)
                                ScreenDestination.REWARDS -> RewardsScreen(uiState, viewModel)
                                ScreenDestination.PARENT_ZONE -> ParentZoneScreen(uiState, viewModel)
                                ScreenDestination.SETTINGS -> SettingsScreen(uiState, viewModel)
                            }
                        }

                        // Celebration Dialog
                        if (uiState.isCelebrationOpen) {
                            CelebrationDialog(
                                starsEarned = uiState.celebrationStars,
                                coinsEarned = uiState.celebrationCoins,
                                message = uiState.celebrationMessage,
                                strings = strings,
                                numeralFormat = uiState.numeralFormat,
                                onDismiss = { viewModel.dismissCelebration() }
                            )
                        }
                    }
                }
            }
        }
    }
}
