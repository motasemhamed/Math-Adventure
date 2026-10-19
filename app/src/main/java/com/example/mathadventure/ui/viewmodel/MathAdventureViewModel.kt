package com.example.mathadventure.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mathadventure.data.db.AppDatabase
import com.example.mathadventure.data.model.AssessmentResult
import com.example.mathadventure.data.model.BadgesCatalog
import com.example.mathadventure.data.model.ChildProfile
import com.example.mathadventure.data.model.LearningTier
import com.example.mathadventure.data.model.MathOperation
import com.example.mathadventure.data.model.TopicProgress
import com.example.mathadventure.data.model.UnlockedAchievement
import com.example.mathadventure.data.repository.MathRepository
import com.example.mathadventure.engine.DeterministicMathEngine
import com.example.mathadventure.engine.MathProblem
import com.example.mathadventure.engine.SoundEffect
import com.example.mathadventure.engine.SoundManager
import com.example.mathadventure.localization.AppLanguage
import com.example.mathadventure.localization.AppStrings
import com.example.mathadventure.localization.NumeralFormat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ScreenDestination {
    LANGUAGE_ONBOARDING,
    HOME,
    MULTIPLICATION_ACADEMY,
    DIVISION_ACADEMY,
    FACT_FAMILY_LAB,
    MENTAL_MATH_LAB,
    GAME_ARCADE,
    ACTIVE_GAME,
    ASSESSMENT,
    REWARDS,
    PARENT_ZONE,
    SETTINGS
}

data class UiState(
    val language: AppLanguage = AppLanguage.ENGLISH,
    val numeralFormat: NumeralFormat = NumeralFormat.WESTERN,
    val currentScreen: ScreenDestination = ScreenDestination.HOME,
    val previousScreen: ScreenDestination = ScreenDestination.HOME,
    val activeProfile: ChildProfile? = null,
    val currentProblem: MathProblem? = null,
    val isAnswerChecked: Boolean = false,
    val isLastAnswerCorrect: Boolean = false,
    val lastSelectedOption: Int? = null,
    val activeGameId: String? = null,
    val activeGameScore: Int = 0,
    val activeGameProgress: Float = 0f,
    val isCelebrationOpen: Boolean = false,
    val celebrationStars: Int = 0,
    val celebrationCoins: Int = 0,
    val celebrationMessage: String = "",
    val soundEnabled: Boolean = true,
    val speechEnabled: Boolean = false,
    val sessionMinutes: Int = 12
)

class MathAdventureViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MathRepository(AppDatabase.getInstance(application))
    val soundManager = SoundManager(application)

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    val topicProgressList: StateFlow<List<TopicProgress>> = MutableStateFlow(emptyList<TopicProgress>())
    val assessmentHistory: StateFlow<List<AssessmentResult>> = MutableStateFlow(emptyList<AssessmentResult>())
    val unlockedBadges: StateFlow<List<UnlockedAchievement>> = MutableStateFlow(emptyList<UnlockedAchievement>())

    init {
        viewModelScope.launch {
            val profile = repository.getOrCreateActiveProfile()
            _uiState.value = _uiState.value.copy(activeProfile = profile)
            loadNewProblem(MathOperation.MULTIPLICATION, profile.tier)
        }
    }

    fun setLanguage(lang: AppLanguage) {
        soundManager.playSound(SoundEffect.CLICK)
        _uiState.value = _uiState.value.copy(language = lang)
    }

    fun setNumeralFormat(format: NumeralFormat) {
        soundManager.playSound(SoundEffect.CLICK)
        _uiState.value = _uiState.value.copy(numeralFormat = format)
    }

    fun navigateTo(screen: ScreenDestination) {
        soundManager.playSound(SoundEffect.CLICK)
        _uiState.value = _uiState.value.copy(
            previousScreen = _uiState.value.currentScreen,
            currentScreen = screen
        )
    }

    fun navigateBack() {
        soundManager.playSound(SoundEffect.CLICK)
        val prev = _uiState.value.previousScreen
        val target = if (prev == _uiState.value.currentScreen || prev == ScreenDestination.LANGUAGE_ONBOARDING) {
            ScreenDestination.HOME
        } else {
            prev
        }
        _uiState.value = _uiState.value.copy(currentScreen = target)
    }

    fun toggleSound() {
        val next = !_uiState.value.soundEnabled
        soundManager.soundEnabled = next
        _uiState.value = _uiState.value.copy(soundEnabled = next)
    }

    fun toggleSpeech() {
        val next = !_uiState.value.speechEnabled
        soundManager.speechEnabled = next
        _uiState.value = _uiState.value.copy(speechEnabled = next)
    }

    fun loadNewProblem(operation: MathOperation, tier: LearningTier? = null, topicKey: String? = null) {
        val currentTier = tier ?: _uiState.value.activeProfile?.tier ?: LearningTier.BEGINNER
        val problem = DeterministicMathEngine.generateProblem(operation, currentTier, topicKey)
        _uiState.value = _uiState.value.copy(
            currentProblem = problem,
            isAnswerChecked = false,
            isLastAnswerCorrect = false,
            lastSelectedOption = null
        )

        // Text-to-speech auto-read if enabled
        val isAr = _uiState.value.language == AppLanguage.ARABIC
        val speechText = if (operation == MathOperation.MULTIPLICATION) {
            if (isAr) "${problem.numA} ضرب ${problem.numB}" else "${problem.numA} times ${problem.numB}"
        } else {
            if (isAr) "${problem.numA} تقسيم ${problem.numB}" else "${problem.numA} divided by ${problem.numB}"
        }
        soundManager.speak(speechText, isAr)
    }

    fun checkAnswer(selectedOption: Int) {
        val problem = _uiState.value.currentProblem ?: return
        val isCorrect = selectedOption == problem.correctAnswer

        _uiState.value = _uiState.value.copy(
            isAnswerChecked = true,
            isLastAnswerCorrect = isCorrect,
            lastSelectedOption = selectedOption
        )

        val profile = _uiState.value.activeProfile
        if (isCorrect) {
            soundManager.playSound(SoundEffect.CORRECT_CHIME)
            if (profile != null) {
                viewModelScope.launch {
                    val starReward = 2
                    val coinReward = 5
                    repository.addRewards(profile.id, starReward, coinReward)
                    repository.recordTopicAnswer(
                        profile.id,
                        "${problem.operation.name.lowercase()}_${problem.numA}",
                        problem.operation,
                        true
                    )
                    _uiState.value = _uiState.value.copy(
                        activeProfile = profile.copy(
                            stars = profile.stars + starReward,
                            coins = profile.coins + coinReward
                        )
                    )
                }
            }
        } else {
            soundManager.playSound(SoundEffect.TRY_AGAIN)
            if (profile != null) {
                viewModelScope.launch {
                    repository.recordTopicAnswer(
                        profile.id,
                        "${problem.operation.name.lowercase()}_${problem.numA}",
                        problem.operation,
                        false
                    )
                }
            }
        }
    }

    fun showCelebration(stars: Int, coins: Int, message: String) {
        soundManager.playSound(SoundEffect.LEVEL_UP)
        val profile = _uiState.value.activeProfile
        if (profile != null) {
            viewModelScope.launch {
                repository.addRewards(profile.id, stars, coins)
                _uiState.value = _uiState.value.copy(
                    activeProfile = profile.copy(
                        stars = profile.stars + stars,
                        coins = profile.coins + coins
                    ),
                    isCelebrationOpen = true,
                    celebrationStars = stars,
                    celebrationCoins = coins,
                    celebrationMessage = message
                )
            }
        }
    }

    fun dismissCelebration() {
        _uiState.value = _uiState.value.copy(isCelebrationOpen = false)
    }

    fun launchGame(gameId: String) {
        soundManager.playSound(SoundEffect.CLICK)
        _uiState.value = _uiState.value.copy(
            activeGameId = gameId,
            activeGameScore = 0,
            activeGameProgress = 0f,
            previousScreen = _uiState.value.currentScreen,
            currentScreen = ScreenDestination.ACTIVE_GAME
        )
        loadNewProblem(MathOperation.MULTIPLICATION)
    }

    fun updateGameProgress(scoreDelta: Int, progressDelta: Float) {
        val newScore = _uiState.value.activeGameScore + scoreDelta
        val newProgress = (_uiState.value.activeGameProgress + progressDelta).coerceIn(0f, 1f)
        _uiState.value = _uiState.value.copy(
            activeGameScore = newScore,
            activeGameProgress = newProgress
        )
        if (newProgress >= 1f) {
            val strings = AppStrings(_uiState.value.language)
            val msg = if (strings.isAr) "مبروك! لقد أكملت اللعبة بنجاح خارق!" else "Congratulations! You completed the game with flying colors!"
            showCelebration(stars = 10, coins = 25, message = msg)
        }
    }

    fun updateProfileTier(tier: LearningTier) {
        val profile = _uiState.value.activeProfile ?: return
        val updated = profile.copy(tier = tier)
        viewModelScope.launch {
            repository.updateProfile(updated)
            _uiState.value = _uiState.value.copy(activeProfile = updated)
        }
    }

    fun resetAllProgress() {
        val profile = _uiState.value.activeProfile ?: return
        viewModelScope.launch {
            repository.resetAllData(profile.id)
            val refreshed = repository.getOrCreateActiveProfile()
            _uiState.value = _uiState.value.copy(activeProfile = refreshed)
            soundManager.playSound(SoundEffect.TRY_AGAIN)
        }
    }

    override fun onCleared() {
        super.onCleared()
        soundManager.release()
    }
}
