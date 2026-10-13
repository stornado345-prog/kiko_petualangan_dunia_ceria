package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.audio.GameAudioManager
import com.example.core.model.AnswerOption
import com.example.core.model.BadgeReward
import com.example.core.model.GameLevel
import com.example.core.model.PlayerProgress
import com.example.core.model.Question
import com.example.data.local.GamePreferencesRepository
import com.example.data.questions.QuestionBank
import com.example.data.questions.WorldCatalog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ScreenType {
    SPLASH,
    HOME,
    WORLD_MAP,
    LEVEL_MAP,
    GAMEPLAY,
    RESULT,
    BADGES
}

enum class FeedbackType {
    NONE,
    CORRECT,
    TRY_AGAIN
}

data class GameplayUiState(
    val currentLevel: GameLevel? = null,
    val questionIndex: Int = 0, // 0..4
    val currentQuestion: Question? = null,
    val selectedOption: AnswerOption? = null,
    val feedback: FeedbackType = FeedbackType.NONE,
    val feedbackMessage: String = "",
    val mistakesThisLevel: Int = 0,
    val tappedItemIndices: Set<Int> = emptySet()
)

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = GamePreferencesRepository(application)
    val audioManager = GameAudioManager(application)

    val progressState: StateFlow<PlayerProgress> = repository.playerProgressFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PlayerProgress()
    )

    private val _currentScreen = MutableStateFlow(ScreenType.SPLASH)
    val currentScreen: StateFlow<ScreenType> = _currentScreen.asStateFlow()

    private val _selectedWorldId = MutableStateFlow(1)
    val selectedWorldId: StateFlow<Int> = _selectedWorldId.asStateFlow()

    private val _gameplayState = MutableStateFlow(GameplayUiState())
    val gameplayState: StateFlow<GameplayUiState> = _gameplayState.asStateFlow()

    private val _completedLevelStars = MutableStateFlow(3)
    val completedLevelStars: StateFlow<Int> = _completedLevelStars.asStateFlow()

    private val _showSoundSettings = MutableStateFlow(false)
    val showSoundSettings: StateFlow<Boolean> = _showSoundSettings.asStateFlow()

    private val _newlyUnlockedBadge = MutableStateFlow<BadgeReward?>(null)
    val newlyUnlockedBadge: StateFlow<BadgeReward?> = _newlyUnlockedBadge.asStateFlow()

    init {
        // Sync audio settings with saved preferences
        viewModelScope.launch {
            progressState.collect { progress ->
                audioManager.soundEnabled = progress.soundEnabled
                audioManager.musicEnabled = progress.musicEnabled
                audioManager.ttsEnabled = progress.ttsEnabled
                if (progress.musicEnabled) {
                    audioManager.startBackgroundMusic()
                } else {
                    audioManager.stopBackgroundMusic()
                }
            }
        }
    }

    fun navigateTo(screen: ScreenType) {
        audioManager.playButtonClick()
        _currentScreen.value = screen
    }

    fun selectWorld(worldId: Int) {
        audioManager.playButtonClick()
        _selectedWorldId.value = worldId
        _currentScreen.value = ScreenType.LEVEL_MAP
    }

    fun startLevel(levelNumber: Int) {
        audioManager.playButtonClick()
        val level = QuestionBank.getLevel(levelNumber)
        _gameplayState.value = GameplayUiState(
            currentLevel = level,
            questionIndex = 0,
            currentQuestion = level.questions.firstOrNull(),
            selectedOption = null,
            feedback = FeedbackType.NONE,
            feedbackMessage = "",
            mistakesThisLevel = 0,
            tappedItemIndices = emptySet()
        )
        _currentScreen.value = ScreenType.GAMEPLAY

        // Read question via TTS
        level.questions.firstOrNull()?.let { q ->
            audioManager.speak(q.spokenPrompt)
        }
    }

    fun onTappedCountableItem(itemIndex: Int) {
        val currentSet = _gameplayState.value.tappedItemIndices.toMutableSet()
        if (!currentSet.contains(itemIndex)) {
            currentSet.add(itemIndex)
            _gameplayState.value = _gameplayState.value.copy(tappedItemIndices = currentSet)
            audioManager.playObjectTap(currentSet.size)
        } else {
            // Re-tapping plays pleasant pop
            audioManager.playObjectTap(itemIndex + 1)
        }
    }

    fun selectAnswer(option: AnswerOption) {
        val state = _gameplayState.value
        val question = state.currentQuestion ?: return

        _gameplayState.value = state.copy(selectedOption = option)

        if (option.isCorrect) {
            audioManager.playCorrectAnswer()
            _gameplayState.value = _gameplayState.value.copy(
                feedback = FeedbackType.CORRECT,
                feedbackMessage = question.praiseFeedback
            )
        } else {
            audioManager.playTryAgain()
            _gameplayState.value = _gameplayState.value.copy(
                feedback = FeedbackType.TRY_AGAIN,
                feedbackMessage = "Ayo coba lagi bersama Kiko!",
                mistakesThisLevel = state.mistakesThisLevel + 1
            )
        }
    }

    fun proceedToNextQuestion() {
        val state = _gameplayState.value
        val level = state.currentLevel ?: return
        val nextIndex = state.questionIndex + 1

        if (nextIndex < level.questions.size) {
            // Next question in level
            val nextQuestion = level.questions[nextIndex]
            _gameplayState.value = state.copy(
                questionIndex = nextIndex,
                currentQuestion = nextQuestion,
                selectedOption = null,
                feedback = FeedbackType.NONE,
                feedbackMessage = "",
                tappedItemIndices = emptySet()
            )
            audioManager.speak(nextQuestion.spokenPrompt)
        } else {
            // Level Completed!
            val starsEarned = when (state.mistakesThisLevel) {
                0 -> 3
                in 1..2 -> 2
                else -> 1
            }
            _completedLevelStars.value = starsEarned

            viewModelScope.launch {
                repository.recordLevelCompleted(level.levelNumber, starsEarned)

                // Check for badge unlock
                val unlockedBadge = WorldCatalog.badges.firstOrNull { it.requiredLevel == level.levelNumber }
                if (unlockedBadge != null) {
                    _newlyUnlockedBadge.value = unlockedBadge
                    audioManager.playVictoryFanfare()
                } else {
                    audioManager.playStarEarned()
                }
            }

            _currentScreen.value = ScreenType.RESULT
        }
    }

    fun repeatSpokenInstruction() {
        _gameplayState.value.currentQuestion?.let { q ->
            audioManager.speak(q.spokenPrompt)
        }
    }

    fun dismissBadgeDialog() {
        _newlyUnlockedBadge.value = null
    }

    fun openSoundSettings() {
        audioManager.playButtonClick()
        _showSoundSettings.value = true
    }

    fun closeSoundSettings() {
        audioManager.playButtonClick()
        _showSoundSettings.value = false
    }

    fun toggleSound(enabled: Boolean) {
        viewModelScope.launch { repository.setSoundEnabled(enabled) }
    }

    fun toggleMusic(enabled: Boolean) {
        viewModelScope.launch { repository.setMusicEnabled(enabled) }
    }

    fun toggleTts(enabled: Boolean) {
        viewModelScope.launch { repository.setTtsEnabled(enabled) }
    }

    override fun onCleared() {
        super.onCleared()
        audioManager.release()
    }
}
