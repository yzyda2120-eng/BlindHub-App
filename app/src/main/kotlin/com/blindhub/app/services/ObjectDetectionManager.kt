package com.blindhub.app.services

import android.content.Context
import android.graphics.Bitmap
import android.speech.tts.TextToSpeech
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions

/**
 * مدير وصف الكائنات بالذكاء الاصطناعي - AI Object Description
 * يستخدم Google ML Kit لتحليل الصور من الكاميرا ووصفها صوتياً للمكفوفين.
 */
class ObjectDetectionManager(private val context: Context, private val tts: TextToSpeech) {

    private val options = ObjectDetectorOptions.Builder()
        .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
        .enableClassification() // تفعيل تصنيف الكائنات (مثل: طاوله، كرسي)
        .build()

    private val objectDetector = ObjectDetection.getClient(options)

    fun analyzeImage(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)
        
        objectDetector.process(image)
            .addOnSuccessListener { detectedObjects ->
                for (obj in detectedObjects) {
                    val label = obj.labels.firstOrNull()?.text ?: "كائن غير معروف"
                    speak("أرى $label أمامك.")
                }
            }
            .addOnFailureListener { e ->
                speak("عذراً، فشل تحليل الصورة.")
            }
    }

    private fun speak(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "ObjectDetectionID")
    }
}
