package com.blindhub.app.services

import android.content.Context
import android.speech.tts.TextToSpeech

/**
 * نسخة مبسطة للملاحة لضمان نجاح البناء في البيئة المحدودة.
 */
class NavigationManager(private val context: Context, private val tts: TextToSpeech) {
    fun startNavigation(destination: String) {
        speak("بدأت الملاحة إلى $destination. (هذه نسخة تجريبية)")
    }
    private fun speak(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "NavigationID")
    }
}
