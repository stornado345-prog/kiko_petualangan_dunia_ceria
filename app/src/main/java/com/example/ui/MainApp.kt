package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.badges.BadgeCollectionScreen
import com.example.ui.components.SoundSettingsDialog
import com.example.ui.gameplay.GameplayScreen
import com.example.ui.home.HomeScreen
import com.example.ui.levelmap.LevelMapScreen
import com.example.ui.result.ResultScreen
import com.example.ui.splash.SplashScreen
import com.example.ui.worldmap.WorldMapScreen

@Composable
fun MainApp(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val progress by viewModel.progressState.collectAsState()
    val selectedWorldId by viewModel.selectedWorldId.collectAsState()
    val gameplayState by viewModel.gameplayState.collectAsState()
    val completedStars by viewModel.completedLevelStars.collectAsState()
    val showSoundSettings by viewModel.showSoundSettings.collectAsState()
    val newlyUnlockedBadge by viewModel.newlyUnlockedBadge.collectAsState()

    // Handle system back navigation cleanly
    BackHandler(enabled = currentScreen != ScreenType.HOME && currentScreen != ScreenType.SPLASH) {
        when (currentScreen) {
            ScreenType.WORLD_MAP -> viewModel.navigateTo(ScreenType.HOME)
            ScreenType.LEVEL_MAP -> viewModel.navigateTo(ScreenType.WORLD_MAP)
            ScreenType.GAMEPLAY -> viewModel.navigateTo(ScreenType.LEVEL_MAP)
            ScreenType.RESULT -> viewModel.navigateTo(ScreenType.LEVEL_MAP)
            ScreenType.BADGES -> viewModel.navigateTo(ScreenType.HOME)
            else -> {}
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        AnimatedContent(
            targetState = currentScreen,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "screen_transition"
        ) { screen ->
            when (screen) {
                ScreenType.SPLASH -> {
                    SplashScreen(
                        onSplashFinished = { viewModel.navigateTo(ScreenType.HOME) }
                    )
                }

                ScreenType.HOME -> {
                    HomeScreen(
                        progress = progress,
                        onStartPlay = {
                            viewModel.startLevel(progress.highestUnlockedLevel)
                        },
                        onSelectWorld = {
                            viewModel.navigateTo(ScreenType.WORLD_MAP)
                        },
                        onViewBadges = {
                            viewModel.navigateTo(ScreenType.BADGES)
                        },
                        onOpenSettings = {
                            viewModel.openSoundSettings()
                        }
                    )
                }

                ScreenType.WORLD_MAP -> {
                    WorldMapScreen(
                        progress = progress,
                        onSelectWorld = { worldId ->
                            viewModel.selectWorld(worldId)
                        },
                        onBack = {
                            viewModel.navigateTo(ScreenType.HOME)
                        }
                    )
                }

                ScreenType.LEVEL_MAP -> {
                    LevelMapScreen(
                        worldId = selectedWorldId,
                        progress = progress,
                        onLevelSelected = { levelNumber ->
                            viewModel.startLevel(levelNumber)
                        },
                        onBack = {
                            viewModel.navigateTo(ScreenType.WORLD_MAP)
                        }
                    )
                }

                ScreenType.GAMEPLAY -> {
                    GameplayScreen(
                        state = gameplayState,
                        onAnswerSelected = { option ->
                            viewModel.selectAnswer(option)
                        },
                        onProceedNext = {
                            viewModel.proceedToNextQuestion()
                        },
                        onItemTapped = { index ->
                            viewModel.onTappedCountableItem(index)
                        },
                        onRepeatVoice = {
                            viewModel.repeatSpokenInstruction()
                        },
                        onExit = {
                            viewModel.navigateTo(ScreenType.LEVEL_MAP)
                        }
                    )
                }

                ScreenType.RESULT -> {
                    val finishedLevel = gameplayState.currentLevel?.levelNumber ?: 1
                    ResultScreen(
                        levelNumber = finishedLevel,
                        starsEarned = completedStars,
                        unlockedBadge = newlyUnlockedBadge,
                        onDismissBadge = {
                            viewModel.dismissBadgeDialog()
                        },
                        onNextLevel = {
                            val nextLevel = (finishedLevel + 1).coerceAtMost(200)
                            viewModel.startLevel(nextLevel)
                        },
                        onRetryLevel = {
                            viewModel.startLevel(finishedLevel)
                        },
                        onBackToMap = {
                            viewModel.navigateTo(ScreenType.LEVEL_MAP)
                        }
                    )
                }

                ScreenType.BADGES -> {
                    BadgeCollectionScreen(
                        progress = progress,
                        onBack = {
                            viewModel.navigateTo(ScreenType.HOME)
                        }
                    )
                }
            }
        }

        // Sound & Narration Settings Dialog
        if (showSoundSettings) {
            SoundSettingsDialog(
                soundEnabled = progress.soundEnabled,
                musicEnabled = progress.musicEnabled,
                ttsEnabled = progress.ttsEnabled,
                onSoundToggle = { viewModel.toggleSound(it) },
                onMusicToggle = { viewModel.toggleMusic(it) },
                onTtsToggle = { viewModel.toggleTts(it) },
                onDismiss = { viewModel.closeSoundSettings() }
            )
        }
    }
}
