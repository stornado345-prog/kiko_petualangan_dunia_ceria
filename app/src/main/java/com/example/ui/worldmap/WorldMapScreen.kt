package com.example.ui.worldmap

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.model.PlayerProgress
import com.example.core.model.WorldInfo
import com.example.data.questions.WorldCatalog
import com.example.ui.components.AnimatedGameBackground

@Composable
fun WorldMapScreen(
    progress: PlayerProgress,
    onSelectWorld: (Int) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedGameBackground(
        modifier = modifier
            .fillMaxSize()
            .testTag("world_map_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Header with Back button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .shadow(4.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable { onBack() }
                        .testTag("back_to_home_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali ke Beranda",
                        tint = Color(0xFF2E7D32),
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "Peta Petualangan",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1B5E20)
                    )
                    Text(
                        text = "Pilih dunia yang ingin kamu jelajahi!",
                        fontSize = 13.sp,
                        color = Color(0xFF558B2F)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Worlds list
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(WorldCatalog.worlds) { world ->
                    val isUnlocked = progress.isWorldUnlocked(world.id)
                    WorldCard(
                        world = world,
                        isUnlocked = isUnlocked,
                        progress = progress,
                        onClick = { if (isUnlocked) onSelectWorld(world.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun WorldCard(
    world: WorldInfo,
    isUnlocked: Boolean,
    progress: PlayerProgress,
    onClick: () -> Unit
) {
    val worldColor = Color(world.primaryColor)
    val cardBg = if (isUnlocked) Color.White else Color(0xFFECEFF1)

    // Calculate completed levels in this world
    val completedCount = (world.startLevel..world.endLevel).count { lvl ->
        (progress.starsByLevel[lvl] ?: 0) > 0
    }
    val progressFraction = (completedCount.toFloat() / world.totalLevels).coerceIn(0f, 1f)

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isUnlocked) 6.dp else 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = isUnlocked) { onClick() }
            .testTag("world_card_${world.id}")
            .then(
                if (isUnlocked) {
                    Modifier.border(2.dp, worldColor.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                } else Modifier
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // World badge circle
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(
                                if (isUnlocked) Brush.radialGradient(
                                    listOf(Color(world.secondaryColor), worldColor)
                                ) else Brush.radialGradient(
                                    listOf(Color(0xFFB0BEC5), Color(0xFF78909C))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isUnlocked) {
                            Text(
                                text = "${world.id}",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Filled.Lock,
                                contentDescription = "Terkunci",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = world.name,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isUnlocked) Color(0xFF263238) else Color(0xFF78909C)
                        )
                        Text(
                            text = world.subtitle,
                            fontSize = 13.sp,
                            color = if (isUnlocked) worldColor else Color(0xFF90A4AE),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Level Range Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isUnlocked) worldColor.copy(alpha = 0.15f) else Color(0xFFCFD8DC))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "Lv ${world.startLevel}–${world.endLevel}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isUnlocked) worldColor else Color(0xFF546E7A)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = world.description,
                fontSize = 13.sp,
                color = if (isUnlocked) Color(0xFF455A64) else Color(0xFF78909C),
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Progress bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LinearProgressIndicator(
                    progress = { progressFraction },
                    modifier = Modifier
                        .weight(1f)
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp)),
                    color = worldColor,
                    trackColor = Color(0xFFE0E0E0)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "$completedCount/${world.totalLevels}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isUnlocked) worldColor else Color.Gray
                )
            }
        }
    }
}
