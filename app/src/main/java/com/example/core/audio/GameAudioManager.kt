package com.example.core.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.speech.tts.TextToSpeech
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.PI
import kotlin.math.sin

class GameAudioManager(private val context: Context) : TextToSpeech.OnInitListener {

    private val scope = CoroutineScope(Dispatchers.Default)

    var soundEnabled: Boolean = true
    var musicEnabled: Boolean = true
    var ttsEnabled: Boolean = true

    private var tts: TextToSpeech? = null
    private var isTtsReady: Boolean = false

    private var musicJob: Job? = null
    private var isMusicRunning: Boolean = false

    init {
        try {
            tts = TextToSpeech(context.applicationContext, this)
        } catch (_: Exception) {
            isTtsReady = false
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale("id", "ID"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Fallback to default locale
                tts?.language = Locale.getDefault()
            }
            tts?.setPitch(1.15f) // Slightly higher, friendly voice for children
            tts?.setSpeechRate(0.92f) // Slightly slower, easy to understand for preschool
            isTtsReady = true
        }
    }

    fun speak(text: String) {
        if (!ttsEnabled || !isTtsReady) return
        try {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "GameQuestionSpeech")
        } catch (_: Exception) {
            // Safe fallback
        }
    }

    fun stopSpeaking() {
        try {
            tts?.stop()
        } catch (_: Exception) {
        }
    }

    fun playButtonClick() {
        if (!soundEnabled) return
        scope.launch {
            playTone(frequency = 600.0, durationMs = 60, volume = 0.5f)
        }
    }

    fun playObjectTap(index: Int = 0) {
        if (!soundEnabled) return
        scope.launch {
            // Increasing pitch for tap counting (e.g., 1, 2, 3...)
            val baseFreq = 440.0 + (index % 10) * 45.0
            playTone(frequency = baseFreq, durationMs = 80, volume = 0.6f, decay = true)
        }
    }

    fun playCorrectAnswer() {
        if (!soundEnabled) return
        scope.launch {
            // Happy cheerful arpeggio: C5 (523Hz), E5 (659Hz), G5 (784Hz), C6 (1046Hz)
            val notes = listOf(523.25, 659.25, 783.99, 1046.50)
            for (freq in notes) {
                playTone(frequency = freq, durationMs = 90, volume = 0.7f, decay = true)
                delay(80)
            }
        }
    }

    fun playTryAgain() {
        if (!soundEnabled) return
        scope.launch {
            // Gentle soft encouragement: G4 (392Hz) -> E4 (329Hz)
            playTone(frequency = 392.00, durationMs = 120, volume = 0.4f, decay = true)
            delay(100)
            playTone(frequency = 329.63, durationMs = 160, volume = 0.4f, decay = true)
        }
    }

    fun playStarEarned() {
        if (!soundEnabled) return
        scope.launch {
            val notes = listOf(659.25, 880.0, 1174.66)
            for (freq in notes) {
                playTone(frequency = freq, durationMs = 100, volume = 0.7f, decay = true)
                delay(90)
            }
        }
    }

    fun playVictoryFanfare() {
        if (!soundEnabled) return
        scope.launch {
            val melody = listOf(
                523.25 to 120L,
                659.25 to 120L,
                783.99 to 150L,
                1046.50 to 300L
            )
            for ((freq, dur) in melody) {
                playTone(frequency = freq, durationMs = dur.toInt(), volume = 0.8f, decay = true)
                delay(dur - 20)
            }
        }
    }

    fun playKikoGiggle() {
        if (!soundEnabled) return
        scope.launch {
            val notes = listOf(700.0, 900.0, 750.0, 950.0)
            for (freq in notes) {
                playTone(frequency = freq, durationMs = 50, volume = 0.5f, decay = true)
                delay(45)
            }
        }
    }

    fun startBackgroundMusic() {
        if (isMusicRunning) return
        isMusicRunning = true
        musicJob = scope.launch {
            // Gentle soothing music box melody loop
            val pentatonicScale = listOf(
                392.00, // G4
                440.00, // A4
                523.25, // C5
                587.33, // D5
                659.25, // E5
                783.99  // G5
            )
            val melodyPattern = listOf(0, 2, 4, 3, 2, 4, 5, 4, 3, 2, 0, 1, 2, 0)
            var idx = 0
            while (isActive && isMusicRunning) {
                if (musicEnabled) {
                    val noteFreq = pentatonicScale[melodyPattern[idx % melodyPattern.size] % pentatonicScale.size]
                    playTone(frequency = noteFreq, durationMs = 280, volume = 0.16f, decay = true)
                    idx++
                    delay(380)
                } else {
                    delay(500)
                }
            }
        }
    }

    fun stopBackgroundMusic() {
        isMusicRunning = false
        musicJob?.cancel()
        musicJob = null
    }

    private fun playTone(frequency: Double, durationMs: Int, volume: Float = 0.5f, decay: Boolean = false) {
        val sampleRate = 22050
        val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
        if (numSamples <= 0) return

        val buffer = ShortArray(numSamples)
        val angularFrequency = 2.0 * PI * frequency / sampleRate

        for (i in 0 until numSamples) {
            val env = if (decay) {
                (1.0 - (i.toDouble() / numSamples))
            } else 1.0
            val sample = (sin(angularFrequency * i) * Short.MAX_VALUE * volume * env).toInt()
            buffer[i] = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
        }

        try {
            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(buffer.size * 2)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            audioTrack.write(buffer, 0, buffer.size)
            audioTrack.play()
            // Clean up after completion
            scope.launch {
                delay(durationMs.toLong() + 50)
                try {
                    audioTrack.stop()
                    audioTrack.release()
                } catch (_: Exception) {}
            }
        } catch (_: Exception) {
            // AudioTrack error fallback
        }
    }

    fun release() {
        stopSpeaking()
        stopBackgroundMusic()
        try {
            tts?.shutdown()
        } catch (_: Exception) {}
    }
}
