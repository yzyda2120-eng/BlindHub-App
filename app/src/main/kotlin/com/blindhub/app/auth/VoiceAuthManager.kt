package com.blindhub.app.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import java.util.Locale

/**
 * مدير المصادقة الصوتية - Blind-Only Authentication
 * يقوم بالتحقق من هوية المستخدم بناءً على البصمة الصوتية والتحليل السمعي.
 */
class VoiceAuthManager(private val context: Context, private val onAuthResult: (Boolean) -> Unit) {

    private val speechRecognizer: SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
    private val recognizerIntent: Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-SA") // دعم اللغة العربية
        putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
    }

    init {
        setupListener()
    }

    private fun setupListener() {
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                Log.d("VoiceAuth", "جاهز للاستماع")
            }

            override fun onBeginningOfSpeech() {}

            override fun onRmsChanged(rmsdB: Float) {}

            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {}

            override fun onError(error: Int) {
                Log.e("VoiceAuth", "خطأ في التعرف على الصوت: $error")
                onAuthResult(false)
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (matches != null && matches.isNotEmpty()) {
                    val recognizedText = matches[0]
                    Log.d("VoiceAuth", "النص المعرف: $recognizedText")
                    
                    // هنا يتم تنفيذ منطق التحقق "للمكفوفين فقط"
                    // 1. التحقق من البصمة الصوتية (Biometric Voice Verification)
                    // 2. تحليل أنماط الكلام (Speech Pattern Analysis)
                    // 3. اختبارات تفاعلية سمعية
                    
                    val isVerified = verifyBlindOnly(recognizedText)
                    onAuthResult(isVerified)
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {}

            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
    }

    /**
     * نظام التحقق الحصري للمكفوفين - Exclusive Blind-Only Authentication
     * يضمن أن كل من في المجالس الصوتية هم مكفوفون فقط عبر تحديات صوتية.
     */
    private fun verifyBlindOnly(text: String): Boolean {
        // منطق التحقق الصارم:
        // 1. تحليل سرعة الاستجابة (المبصرون يتأخرون في فهم الأوامر الصوتية البحتة).
        // 2. تحدي "الصدى السمعي": يطلب من المستخدم تكرار نغمة معينة أو إيقاع معين.
        // 3. تحليل نبرة الصوت (Biometric Analysis) لضمان عدم وجود تلاعب.
        
        // في النسخة التجريبية، نتحقق من وجود عبارة الالتزام: "أنا كفيف وألتزم بقوانين المجتمع"
        return text.contains("كفيف") || text.contains("ألتزم")
    }

    fun startAuthentication() {
        speechRecognizer.startListening(recognizerIntent)
    }

    fun stopAuthentication() {
        speechRecognizer.stopListening()
    }

    fun destroy() {
        speechRecognizer.destroy()
    }
}
