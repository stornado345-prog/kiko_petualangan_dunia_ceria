package com.example.ui.gameplay

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.model.AnswerOption
import com.example.core.model.QuestionType
import com.example.data.questions.WorldCatalog
import com.example.ui.FeedbackType
import com.example.ui.GameplayUiState
import com.example.ui.components.AnimatedGameBackground
import com.example.ui.components.BigGameButton
import com.example.ui.components.VisualItemRenderer

@Composable
fun GameplayScreen(
    state: GameplayUiState,
    onAnswerSelected: (AnswerOption) -> Unit,
    onProceedNext: () -> Unit,
    onItemTapped: (Int) -> Unit,
    onRepeatVoice: () -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val level = state.currentLevel ?: return
    val question = state.currentQuestion ?: return
    val world = WorldCatalog.getWorldForLevel(level.levelNumber)
    val worldColor = Color(world.primaryColor)

    AnimatedGameBackground(
        primaryColor = worldColor,
        secondaryColor = Color(world.secondaryColor),
        modifier = modifier
            .fillMaxSize()
            .testTag("gameplay_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar: Back Button, Level Title, Progress Dots, Repeat Voice
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Exit button
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .shadow(4.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable { onExit() }
                        .testTag("exit_gameplay_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali ke Peta Level",
                        tint = worldColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Level & Question Progress Indicator
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Level ${level.levelNumber}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF1B5E20)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    // 5 dots for the 5 questions
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        for (i in 0 until 5) {
                            val isCompleted = i < state.questionIndex
                            val isCurrent = i == state.questionIndex
                            Box(
                                modifier = Modifier
                                    .size(if (isCurrent) 12.dp else 10.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isCompleted -> Color(0xFF4CAF50)
                                            isCurrent -> Color(0xFFFF9800)
                                            else -> Color(0xFFCFD8DC)
                                        }
                                    )
                            )
                        }
                    }
                }

                // Voice Read-Aloud Button
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .shadow(4.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color(0xFFFFF3E0))
                        .clickable { onRepeatVoice() }
                        .testTag("tts_voice_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.VolumeUp,
                        contentDescription = "Dengarkan Instruksi",
                        tint = Color(0xFFE65100),
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            // Question Banner Card
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = question.instruction,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF2E7D32),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Sentuh objek untuk menghitungnya bersama Kiko!",
                        fontSize = 12.sp,
                        color = Color(0xFF7CB342),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Visual Items Display Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White.copy(alpha = 0.85f))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                when (question.questionType) {
                    QuestionType.ADDITION_VISUAL -> {
                        // Display group 1 items + plus sign + group 2 items
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Group 1
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    for (i in 0 until question.firstGroupCount) {
                                        val tapIndex = if (state.tappedItemIndices.contains(i)) {
                                            state.tappedItemIndices.indexOf(i) + 1
                                        } else null
                                        VisualItemRenderer(
                                            itemType = question.visualItem,
                                            size = 56.dp,
                                            tapIndex = tapIndex,
                                            onItemTapped = { onItemTapped(i) }
                                        )
                                    }
                                }
                            }

                            Text(
                                text = "+",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFE65100),
                                modifier = Modifier.padding(horizontal = 10.dp)
                            )

                            // Group 2
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    for (j in 0 until question.secondGroupCount) {
                                        val overallIdx = question.firstGroupCount + j
                                        val tapIndex = if (state.tappedItemIndices.contains(overallIdx)) {
                                            state.tappedItemIndices.indexOf(overallIdx) + 1
                                        } else null
                                        VisualItemRenderer(
                                            itemType = question.visualItem,
                                            size = 56.dp,
                                            tapIndex = tapIndex,
                                            onItemTapped = { onItemTapped(overallIdx) }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    QuestionType.MATCH_NAME -> {
                        // Big prominent single visual item for matching name
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            VisualItemRenderer(
                                itemType = question.visualItem,
                                size = 110.dp,
                                onItemTapped = { onItemTapped(0) }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Benda apakah ini?",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF37474F)
                            )
                        }
                    }

                    else -> {
                        // Single countable group (1..10)
                        val total = question.firstGroupCount
                        val columns = if (total <= 4) total.coerceAtLeast(2) else 4
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(columns),
                            horizontalArrangement = Arrangement.Center,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            items(total) { index ->
                                val tapIndex = if (state.tappedItemIndices.contains(index)) {
                                    state.tappedItemIndices.indexOf(index) + 1
                                } else null
                                VisualItemRenderer(
                                    itemType = question.visualItem,
                                    size = if (total > 6) 54.dp else 66.dp,
                                    tapIndex = tapIndex,
                                    onItemTapped = { onItemTapped(index) },
                                    modifier = Modifier.padding(4.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Feedback Banner (Animated)
            AnimatedVisibility(
                visible = state.feedback != FeedbackType.NONE,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (state.feedback == FeedbackType.CORRECT) Color(0xFFE8F5E9) else Color(0xFFFFF3E0)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 6.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = state.feedbackMessage,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = if (state.feedback == FeedbackType.CORRECT) Color(0xFF2E7D32) else Color(0xFFE65100)
                        )
                        if (state.feedback == FeedbackType.CORRECT) {
                            BigGameButton(
                                text = "Lanjut ➔",
                                onClick = onProceedNext,
                                primaryColor = Color(0xFF4CAF50),
                                secondaryColor = Color(0xFF2E7D32),
                                testTag = "next_question_button"
                            )
                        }
                    }
                }
            }

            // Answer Options Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                question.options.forEach { option ->
                    val isSelected = state.selectedOption?.id == option.id
                    val isCorrectOption = option.isCorrect
                    val showCorrectHighlight = isSelected && state.feedback == FeedbackType.CORRECT

                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = when {
                                showCorrectHighlight -> Color(0xFF4CAF50)
                                isSelected && state.feedback == FeedbackType.TRY_AGAIN -> Color(0xFFFFCC80)
                                else -> Color.White
                            }
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(64.dp)
                            .clickable(enabled = state.feedback != FeedbackType.CORRECT) {
                                onAnswerSelected(option)
                            }
                            .testTag("option_${option.text}")
                            .then(
                                if (showCorrectHighlight) {
                                    Modifier.border(3.dp, Color(0xFF2E7D32), RoundedCornerShape(20.dp))
                                } else Modifier
                            )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = option.text,
                                fontSize = if (option.text.length > 5) 15.sp else 24.sp,
                                fontWeight = FontWeight.Black,
                                color = if (showCorrectHighlight) Color.White else Color(0xFF37474F),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}
