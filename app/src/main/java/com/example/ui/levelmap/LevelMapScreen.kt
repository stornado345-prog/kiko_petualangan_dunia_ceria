package com.example.ui.levelmap

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.model.PlayerProgress
import com.example.data.questions.WorldCatalog
import com.example.ui.components.AnimatedGameBackground

@Composable
fun LevelMapScreen(
    worldId: Int,
    progress: PlayerProgress,
    onLevelSelected: (Int) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val world = WorldCatalog.worlds.firstOrNull { it.id == worldId } ?: WorldCatalog.worlds.first()
    val worldColor = Color(world.primaryColor)
    val levels = (world.startLevel..world.endLevel).toList()

    AnimatedGameBackground(
        primaryColor = worldColor,
        secondaryColor = Color(world.secondaryColor),
        modifier = modifier
            .fillMaxSize()
            .testTag("level_map_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Header
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
                        .testTag("back_to_world_map_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali ke Peta Dunia",
                        tint = worldColor,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = world.name,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF1B5E20)
                    )
                    Text(
                        text = "40 Level Petualangan Ceria",
                        fontSize = 13.sp,
                        color = Color(0xFF33691E),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 40 Levels Adventure Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp, top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(levels) { levelNumber ->
                    val isUnlocked = progress.isLevelUnlocked(levelNumber)
                    val isCurrentActive = levelNumber == progress.highestUnlockedLevel
                    val stars = progress.getStarsForLevel(levelNumber)
                    val isBoss = (levelNumber % 40 == 0)

                    LevelNodeItem(
                        levelNumber = levelNumber,
                        isUnlocked = isUnlocked,
                        isCurrentActive = isCurrentActive,
                        stars = stars,
                        isBoss = isBoss,
                        worldColor = worldColor,
                        onClick = {
                            if (isUnlocked) onLevelSelected(levelNumber)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun LevelNodeItem(
    levelNumber: Int,
    isUnlocked: Boolean,
    isCurrentActive: Boolean,
    stars: Int,
    isBoss: Boolean,
    worldColor: Color,
    onClick: () -> Unit
) {
    val transition = rememberInfiniteTransition(label = "pulse_active")
    val pulseScale by transition.animateFloat(
        initialValue = 1f,
        targetValue = if (isCurrentActive) 1.08f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "active_scale"
    )

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isBoss && isUnlocked -> Color(0xFFFFF8E1)
                isCurrentActive -> Color.White
                isUnlocked -> Color.White
                else -> Color(0xFFECEFF1)
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isUnlocked) 5.dp else 1.dp),
        modifier = Modifier
            .scale(pulseScale)
            .clickable(enabled = isUnlocked) { onClick() }
            .testTag("level_node_$levelNumber")
            .then(
                if (isCurrentActive) {
                    Modifier.border(3.dp, Color(0xFFFF9800), RoundedCornerShape(18.dp))
                } else if (isBoss && isUnlocked) {
                    Modifier.border(2.dp, Color(0xFFFFB300), RoundedCornerShape(18.dp))
                } else Modifier
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (isUnlocked) {
                if (isBoss) {
                    Icon(
                        imageVector = Icons.Filled.EmojiEvents,
                        contentDescription = "Boss Level",
                        tint = Color(0xFFFFB300),
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = "$levelNumber",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isCurrentActive) Color(0xFFFF6F00) else Color(0xFF37474F)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Stars display (0..3)
                Row(horizontalArrangement = Arrangement.Center) {
                    for (i in 1..3) {
                        val filled = i <= stars
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = if (filled) Color(0xFFFFD54F) else Color(0xFFCFD8DC),
                            modifier = Modifier.size(11.dp)
                        )
                    }
                }
            } else {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = "Level Terkunci",
                    tint = Color(0xFF90A4AE),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$levelNumber",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFB0BEC5)
                )
            }
        }
    }
}
