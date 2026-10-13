package com.example.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.core.model.PlayerProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.gameDataStore: DataStore<Preferences> by preferencesDataStore(name = "aku_pintar_prefs")

class GamePreferencesRepository(private val context: Context) {

    companion object {
        val KEY_UNLOCKED_LEVEL = intPreferencesKey("unlocked_level")
        val KEY_UNLOCKED_WORLD = intPreferencesKey("unlocked_world")
        val KEY_STARS_MAP = stringPreferencesKey("stars_map_encoded")
        val KEY_SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val KEY_MUSIC_ENABLED = booleanPreferencesKey("music_enabled")
        val KEY_TTS_ENABLED = booleanPreferencesKey("tts_enabled")
    }

    val playerProgressFlow: Flow<PlayerProgress> = context.gameDataStore.data.map { prefs ->
        val unlockedLevel = prefs[KEY_UNLOCKED_LEVEL] ?: 1
        val unlockedWorld = prefs[KEY_UNLOCKED_WORLD] ?: 1
        val starsEncoded = prefs[KEY_STARS_MAP] ?: ""
        val sound = prefs[KEY_SOUND_ENABLED] ?: true
        val music = prefs[KEY_MUSIC_ENABLED] ?: true
        val tts = prefs[KEY_TTS_ENABLED] ?: true

        val starsMap = parseStarsMap(starsEncoded)
        val totalStars = starsMap.values.sum()

        PlayerProgress(
            highestUnlockedLevel = unlockedLevel,
            highestUnlockedWorld = unlockedWorld,
            starsByLevel = starsMap,
            totalStars = totalStars,
            soundEnabled = sound,
            musicEnabled = music,
            ttsEnabled = tts
        )
    }

    suspend fun recordLevelCompleted(levelNumber: Int, starsEarned: Int) {
        context.gameDataStore.edit { prefs ->
            val currentUnlockedLevel = prefs[KEY_UNLOCKED_LEVEL] ?: 1
            val nextLevel = levelNumber + 1
            if (nextLevel > currentUnlockedLevel && nextLevel <= 200) {
                prefs[KEY_UNLOCKED_LEVEL] = nextLevel
            }

            // Calculate world unlock (each world has 40 levels)
            val currentUnlockedWorld = prefs[KEY_UNLOCKED_WORLD] ?: 1
            val worldJustCompleted = levelNumber / 40
            if (levelNumber % 40 == 0 && worldJustCompleted + 1 > currentUnlockedWorld && worldJustCompleted < 5) {
                prefs[KEY_UNLOCKED_WORLD] = worldJustCompleted + 1
            }

            // Update stars
            val starsEncoded = prefs[KEY_STARS_MAP] ?: ""
            val starsMap = parseStarsMap(starsEncoded).toMutableMap()
            val existingStars = starsMap[levelNumber] ?: 0
            if (starsEarned > existingStars) {
                starsMap[levelNumber] = starsEarned
            }
            prefs[KEY_STARS_MAP] = encodeStarsMap(starsMap)
        }
    }

    suspend fun setSoundEnabled(enabled: Boolean) {
        context.gameDataStore.edit { prefs ->
            prefs[KEY_SOUND_ENABLED] = enabled
        }
    }

    suspend fun setMusicEnabled(enabled: Boolean) {
        context.gameDataStore.edit { prefs ->
            prefs[KEY_MUSIC_ENABLED] = enabled
        }
    }

    suspend fun setTtsEnabled(enabled: Boolean) {
        context.gameDataStore.edit { prefs ->
            prefs[KEY_TTS_ENABLED] = enabled
        }
    }

    private fun parseStarsMap(encoded: String): Map<Int, Int> {
        if (encoded.isBlank()) return emptyMap()
        return try {
            encoded.split(";").mapNotNull { entry ->
                val parts = entry.split(":")
                if (parts.size == 2) {
                    val lvl = parts[0].toIntOrNull()
                    val stars = parts[1].toIntOrNull()
                    if (lvl != null && stars != null) lvl to stars else null
                } else null
            }.toMap()
        } catch (_: Exception) {
            emptyMap()
        }
    }

    private fun encodeStarsMap(map: Map<Int, Int>): String {
        return map.entries.joinToString(";") { "${it.key}:${it.value}" }
    }
}
