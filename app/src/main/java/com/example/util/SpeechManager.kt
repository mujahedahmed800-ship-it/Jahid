package com.example.util

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class SpeechManager(context: Context) {
    private var tts: TextToSpeech? = null
    private var isInitialized = false

    init {
        tts = TextToSpeech(context.applicationContext) { status ->
            isInitialized = status == TextToSpeech.SUCCESS
        }
    }

    fun speak(text: String, languageTag: String, onUnavailable: () -> Unit) {
        val engine = tts
        if (!isInitialized || engine == null) {
            onUnavailable()
            return
        }

        val locale = Locale.forLanguageTag(languageTag)
        val result = engine.setLanguage(locale)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            // Fall back to default engine locale only if English, otherwise notify caller
            if (languageTag.startsWith("en")) {
                engine.language = Locale.US
            } else {
                onUnavailable()
                return
            }
        }
        engine.speak(text, TextToSpeech.QUEUE_FLUSH, null, System.currentTimeMillis().toString())
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
