package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import akupintar.kiko.edukasi.game.anak.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class KikoPose {
    IDLE,
    THINKING,
    HAPPY,
    CELEBRATE
}

@Composable
fun KikoMascot(
    modifier: Modifier = Modifier,
    size: Dp = 180.dp,
    pose: KikoPose = KikoPose.IDLE,
    speechText: String? = null,
    onKikoTapped: (() -> Unit)? = null
) {
    val infiniteTransition = rememberInfiniteTransition(label = "kiko_breath")

    // Gentle breathing vertical oscillation
    val breathOffsetY by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breath_y"
    )

    // Gentle breathing scale
    val breathScale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breath_scale"
    )

    val bounceAnim = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()
    var showSpeechBubble by remember { mutableStateOf(speechText != null) }
    var currentSpeech by remember { mutableStateOf(speechText ?: "Halo sahabat kecil!") }

    LaunchedEffect(speechText) {
        if (speechText != null) {
            currentSpeech = speechText
            showSpeechBubble = true
        }
    }

    val greetingPhrases = remember {
        listOf(
            "Halo sahabat kecil! Kiko siap menemanimu!",
            "Ayo belajar dan kumpulkan banyak bintang!",
            "Kiko senang sekali bermain bersamamu!",
            "Kamu anak pintar dan hebat!"
        )
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Speech bubble
            AnimatedVisibility(
                visible = showSpeechBubble,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFFF9C4))
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = currentSpeech,
                            color = Color(0xFF5D4037),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Kiko Character Image & Interactive Bounce
            Box(
                modifier = Modifier
                    .size(size)
                    .offset(y = breathOffsetY.dp)
                    .scale(breathScale * bounceAnim.value)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        scope.launch {
                            bounceAnim.animateTo(1.22f, spring(stiffness = Spring.StiffnessHigh))
                            bounceAnim.animateTo(1.0f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
                        }
                        currentSpeech = greetingPhrases.random()
                        showSpeechBubble = true
                        onKikoTapped?.invoke()
                    },
                contentAlignment = Alignment.Center
            ) {
                // Outer glow halo
                Box(
                    modifier = Modifier
                        .size(size * 0.95f)
                        .shadow(12.dp, CircleShape, ambientColor = Color(0xFFFFB300), spotColor = Color(0xFFFF8F00))
                        .clip(CircleShape)
                        .background(Color(0x33FFE082))
                )

                // High fidelity character asset
                Image(
                    painter = painterResource(id = R.drawable.img_kiko_mascot),
                    contentDescription = "Kiko sang Kancil Lucu",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(size * 0.92f)
                        .clip(CircleShape)
                )
            }
        }
    }
}
