package com.example.ui.result

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.model.BadgeReward
import com.example.ui.components.AnimatedGameBackground
import com.example.ui.components.BigGameButton
import com.example.ui.components.KikoMascot
import com.example.ui.components.KikoPose
import com.example.ui.components.StarRatingBar

@Composable
fun ResultScreen(
    levelNumber: Int,
    starsEarned: Int,
    unlockedBadge: BadgeReward?,
    onDismissBadge: () -> Unit,
    onNextLevel: () -> Unit,
    onRetryLevel: () -> Unit,
    onBackToMap: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedGameBackground(
        modifier = modifier
            .fillMaxSize()
            .testTag("result_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Victory Title
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "HEBAT SEKALI!",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF2E7D32),
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Level $levelNumber Berhasil Dituntaskan!",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF6F00)
                )
            }

            // Kiko Celebrates
            KikoMascot(
                size = 190.dp,
                pose = KikoPose.CELEBRATE,
                speechText = "Kamu anak yang sangat pintar!"
            )

            // Star Rating Card
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    StarRatingBar(
                        stars = starsEarned,
                        starSize = 48.dp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = when (starsEarned) {
                            3 -> "Sempurna! 3 Bintang Penuh!"
                            2 -> "Bagus Sekali! 2 Bintang!"
                            else -> "Kamu Berhasil! 1 Bintang!"
                        },
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE65100),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Action Buttons
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (levelNumber < 200) {
                    BigGameButton(
                        text = "LANJUT LEVEL",
                        onClick = onNextLevel,
                        primaryColor = Color(0xFF4CAF50),
                        secondaryColor = Color(0xFF2E7D32),
                        modifier = Modifier.fillMaxWidth(0.85f),
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.PlayArrow,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )
                        },
                        testTag = "result_next_level_button"
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(0.85f),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    BigGameButton(
                        text = "COBA LAGI",
                        onClick = onRetryLevel,
                        primaryColor = Color(0xFFFF9800),
                        secondaryColor = Color(0xFFE65100),
                        modifier = Modifier.weight(1f),
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Refresh,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        testTag = "result_retry_button"
                    )

                    BigGameButton(
                        text = "PETA LEVEL",
                        onClick = onBackToMap,
                        primaryColor = Color(0xFF03A9F4),
                        secondaryColor = Color(0xFF0288D1),
                        modifier = Modifier.weight(1f),
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Map,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        testTag = "result_map_button"
                    )
                }
            }
        }

        // Newly Unlocked Badge Popup Dialog
        if (unlockedBadge != null) {
            AlertDialog(
                onDismissRequest = onDismissBadge,
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "LENCANA BARU!",
                            fontWeight = FontWeight.Black,
                            fontSize = 22.sp,
                            color = Color(0xFFFF8F00)
                        )
                    }
                },
                text = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = unlockedBadge.iconEmoji,
                            fontSize = 54.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = unlockedBadge.title,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = Color(0xFF2E7D32)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = unlockedBadge.description,
                            fontSize = 14.sp,
                            color = Color(0xFF546E7A),
                            textAlign = TextAlign.Center
                        )
                    }
                },
                confirmButton = {
                    BigGameButton(
                        text = "Hore! Terima Kasih",
                        onClick = onDismissBadge,
                        primaryColor = Color(0xFF4CAF50),
                        secondaryColor = Color(0xFF2E7D32),
                        testTag = "dismiss_badge_button"
                    )
                },
                shape = RoundedCornerShape(24.dp),
                containerColor = Color.White
            )
        }
    }
}
