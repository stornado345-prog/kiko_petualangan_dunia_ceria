package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path

@Composable
fun AnimatedGameBackground(
    primaryColor: Color = Color(0xFF4CAF50),
    secondaryColor: Color = Color(0xFF81C784),
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val transition = rememberInfiniteTransition(label = "bg_motion")

    // Slow cloud drift
    val cloudOffset1 by transition.animateFloat(
        initialValue = -150f,
        targetValue = 600f,
        animationSpec = infiniteRepeatable(
            animation = tween(22000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "cloud1"
    )

    val cloudOffset2 by transition.animateFloat(
        initialValue = 600f,
        targetValue = -180f,
        animationSpec = infiniteRepeatable(
            animation = tween(28000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "cloud2"
    )

    // Gentle sun sway
    val sunScale by transition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sun"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE1F5FE), // Soft blue sky top
                        Color(0xFFFFF9C4), // Warm sunlight middle
                        secondaryColor.copy(alpha = 0.35f),
                        primaryColor.copy(alpha = 0.75f)
                    )
                )
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Smiling Sun
            val sunCenter = Offset(w * 0.88f, h * 0.12f)
            drawCircle(
                Brush.radialGradient(
                    colors = listOf(Color(0xFFFFF176), Color(0xFFFFB300)),
                    center = sunCenter
                ),
                radius = 48f * sunScale,
                center = sunCenter
            )

            // Sun rays
            for (i in 0..7) {
                val angle = i * (Math.PI.toFloat() / 4f)
                val rayStart = Offset(
                    sunCenter.x + (54f * kotlin.math.cos(angle)),
                    sunCenter.y + (54f * kotlin.math.sin(angle))
                )
                val rayEnd = Offset(
                    sunCenter.x + (72f * kotlin.math.cos(angle)),
                    sunCenter.y + (72f * kotlin.math.sin(angle))
                )
                drawLine(Color(0xFFFFCA28), rayStart, rayEnd, strokeWidth = 5f)
            }

            // Cloud 1
            val c1Y = h * 0.14f
            val c1X = (cloudOffset1 % (w + 200)) - 100
            drawCircle(Color(0xE6FFFFFF), radius = 35f, center = Offset(c1X, c1Y))
            drawCircle(Color(0xE6FFFFFF), radius = 45f, center = Offset(c1X + 35f, c1Y - 10f))
            drawCircle(Color(0xE6FFFFFF), radius = 35f, center = Offset(c1X + 70f, c1Y))

            // Cloud 2
            val c2Y = h * 0.22f
            val c2X = (cloudOffset2 % (w + 300)) - 50
            drawCircle(Color(0xD9FFFFFF), radius = 28f, center = Offset(c2X, c2Y))
            drawCircle(Color(0xD9FFFFFF), radius = 36f, center = Offset(c2X + 28f, c2Y - 8f))
            drawCircle(Color(0xD9FFFFFF), radius = 28f, center = Offset(c2X + 56f, c2Y))

            // Rolling green hill curves in background
            val hill1 = Path().apply {
                moveTo(0f, h * 0.88f)
                cubicTo(w * 0.35f, h * 0.82f, w * 0.7f, h * 0.94f, w, h * 0.86f)
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }
            drawPath(hill1, primaryColor.copy(alpha = 0.45f))

            val hill2 = Path().apply {
                moveTo(0f, h * 0.92f)
                cubicTo(w * 0.4f, h * 0.96f, w * 0.75f, h * 0.88f, w, h * 0.94f)
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }
            drawPath(hill2, primaryColor.copy(alpha = 0.7f))
        }

        content()
    }
}
