package com.blindhub.app.services

import android.content.Context
import android.graphics.Bitmap
import android.speech.tts.TextToSpeech

/**
 * نسخة مبسطة لوصف الكائنات لضمان نجاح البناء في البيئة المحدودة.
 */
class ObjectDetectionManager(private val context: Context, private val tts: TextToSpeech) {
    fun analyzeImage(bitmap: Bitmap) {
        speak("أرى كائناً أمامك. (هذه نسخة تجريبية)")
    }
    private fun speak(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "ObjectDetectionID")
    }
}
