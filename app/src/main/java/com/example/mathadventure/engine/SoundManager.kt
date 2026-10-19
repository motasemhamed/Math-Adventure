package com.example.mathadventure.engine

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.speech.tts.TextToSpeech
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.sin

enum class SoundEffect {
    CLICK,
    CORRECT_CHIME,
    TRY_AGAIN,
    LEVEL_UP,
    ROCKET_LAUNCH,
    COIN_COLLECT
}

class SoundManager(private val context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isTtsReady = false
    private val scope = CoroutineScope(Dispatchers.Default)

    var soundEnabled: Boolean = true
    var speechEnabled: Boolean = false

    init {
        try {
            tts = TextToSpeech(context.applicationContext, this)
        } catch (_: Exception) {
            isTtsReady = false
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isTtsReady = true
            tts?.setPitch(1.1f)
            tts?.setSpeechRate(0.95f)
        }
    }

    fun playSound(effect: SoundEffect) {
        if (!soundEnabled) return
        scope.launch {
            try {
                when (effect) {
                    SoundEffect.CLICK -> playToneSequence(listOf(Pair(600, 40)))
                    SoundEffect.CORRECT_CHIME -> playToneSequence(listOf(Pair(523, 70), Pair(659, 70), Pair(784, 70), Pair(1046, 180)))
                    SoundEffect.TRY_AGAIN -> playToneSequence(listOf(Pair(350, 90), Pair(300, 150)))
                    SoundEffect.LEVEL_UP -> playToneSequence(listOf(Pair(440, 80), Pair(554, 80), Pair(659, 80), Pair(880, 220)))
                    SoundEffect.ROCKET_LAUNCH -> playToneSequence(listOf(Pair(200, 100), Pair(300, 100), Pair(450, 100), Pair(600, 120), Pair(800, 200)))
                    SoundEffect.COIN_COLLECT -> playToneSequence(listOf(Pair(987, 60), Pair(1318, 140)))
                }
            } catch (_: Exception) {
                // Audio synthesis gracefully ignored on low-level failure
            }
        }
    }

    fun speak(text: String, isArabic: Boolean) {
        if (!speechEnabled || !isTtsReady || tts == null) return
        try {
            val targetLocale = if (isArabic) Locale("ar") else Locale.US
            tts?.language = targetLocale
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "MathAdventureSpeech")
        } catch (_: Exception) {
            // Ignore TTS errors gracefully
        }
    }

    private fun playToneSequence(tones: List<Pair<Int, Int>>) {
        val sampleRate = 44100
        val totalMs = tones.sumOf { it.second }
        val totalSamples = (sampleRate * totalMs) / 1000
        val buffer = ShortArray(totalSamples)

        var sampleIndex = 0
        for ((freq, durationMs) in tones) {
            val count = (sampleRate * durationMs) / 1000
            val angularFreq = 2.0 * Math.PI * freq / sampleRate
            for (i in 0 until count) {
                if (sampleIndex < buffer.size) {
                    // Apply smooth envelope to avoid audio clicks
                    val envelope = when {
                        i < 500 -> i / 500.0
                        i > count - 500 -> (count - i) / 500.0
                        else -> 1.0
                    }
                    val sampleVal = (sin(i * angularFreq) * 16000 * envelope).toInt()
                    buffer[sampleIndex++] = sampleVal.toShort()
                }
            }
        }

        val minBuf = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        val track = AudioTrack.Builder()
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
            .setBufferSizeInBytes(buffer.size * 2 + minBuf)
            .setTransferMode(AudioTrack.MODE_STATIC)
            .build()

        track.write(buffer, 0, buffer.size)
        track.play()

        Thread.sleep(totalMs.toLong() + 50)
        track.stop()
        track.release()
    }

    fun release() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
