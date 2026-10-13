package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.MusicOff
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VoiceOverOff
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun BigGameButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    primaryColor: Color = Color(0xFFFF9800),
    secondaryColor: Color = Color(0xFFF57C00),
    icon: (@Composable () -> Unit)? = null,
    testTag: String = "game_button"
) {
    val scaleAnim = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .scale(scaleAnim.value)
            .testTag(testTag)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                scope.launch {
                    scaleAnim.animateTo(0.92f, spring(stiffness = Spring.StiffnessHigh))
                    scaleAnim.animateTo(1.0f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
                }
                onClick()
            }
    ) {
        // Bottom 3D shadow layer
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = 6.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(secondaryColor.copy(alpha = 0.9f))
        )

        // Front tactile button surface
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(primaryColor, secondaryColor)
                    )
                )
                .padding(horizontal = 24.dp, vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (icon != null) {
                    icon()
                    Spacer(modifier = Modifier.width(10.dp))
                }
                Text(
                    text = text,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

@Composable
fun StarRatingBar(
    stars: Int,
    modifier: Modifier = Modifier,
    maxStars: Int = 3,
    starSize: Dp = 38.dp
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..maxStars) {
            val filled = i <= stars
            val starColor = if (filled) Color(0xFFFFD700) else Color(0xFFE0E0E0)
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = if (filled) "Bintang diraih" else "Bintang belum",
                tint = starColor,
                modifier = Modifier
                    .size(starSize)
                    .padding(horizontal = 2.dp)
                    .shadow(if (filled) 4.dp else 0.dp, CircleShape)
            )
        }
    }
}

@Composable
fun SoundSettingsDialog(
    soundEnabled: Boolean,
    musicEnabled: Boolean,
    ttsEnabled: Boolean,
    onSoundToggle: (Boolean) -> Unit,
    onMusicToggle: (Boolean) -> Unit,
    onTtsToggle: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Pengaturan Suara",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color(0xFF2E7D32)
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SettingSwitchRow(
                    label = "Efek Suara (SFX)",
                    checked = soundEnabled,
                    icon = if (soundEnabled) Icons.Filled.VolumeUp else Icons.Filled.VolumeOff,
                    onCheckedChange = onSoundToggle
                )
                SettingSwitchRow(
                    label = "Musik Latar",
                    checked = musicEnabled,
                    icon = if (musicEnabled) Icons.Filled.MusicNote else Icons.Filled.MusicOff,
                    onCheckedChange = onMusicToggle
                )
                SettingSwitchRow(
                    label = "Suara Narasi (TTS)",
                    checked = ttsEnabled,
                    icon = if (ttsEnabled) Icons.Filled.RecordVoiceOver else Icons.Filled.VoiceOverOff,
                    onCheckedChange = onTtsToggle
                )
            }
        },
        confirmButton = {
            BigGameButton(
                text = "Tutup",
                onClick = onDismiss,
                primaryColor = Color(0xFF4CAF50),
                secondaryColor = Color(0xFF388E3C),
                testTag = "settings_close_button"
            )
        },
        shape = RoundedCornerShape(24.dp),
        containerColor = Color.White
    )
}

@Composable
private fun SettingSwitchRow(
    label: String,
    checked: Boolean,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (checked) Color(0xFF2E7D32) else Color.Gray,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = label,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF37474F)
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF4CAF50)
            )
        )
    }
}
