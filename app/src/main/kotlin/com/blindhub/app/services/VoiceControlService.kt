package com.blindhub.app.services

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

/**
 * خدمة التحكم الصوتي - Voice Control Service (Social Edition)
 * تدير التفاعل الاجتماعي (تويتر + واتساب) عبر الأوامر الصوتية باللغة العربية.
 */
class VoiceControlService : Service(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var socialMediaManager: SocialMediaManager

    override fun onCreate() {
        super.onCreate()
        tts = TextToSpeech(this, this)
        setupSpeechRecognizer()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale("ar")
            socialMediaManager = SocialMediaManager(this, tts)
            speak("تم تفعيل نظام التواصل الاجتماعي. قل: تغريدات، لسماع الخط الزمني، أو: رسائل، لفتح المحادثات الخاصة.")
            startListening()
        }
    }

    private fun setupSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onError(error: Int) {
                startListening() // إعادة المحاولة عند الخطأ
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (matches != null && matches.isNotEmpty()) {
                    handleSocialCommand(matches[0])
                }
                startListening()
            }

            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
    }

    private fun handleSocialCommand(command: String) {
        val lowerCommand = command.lowercase()
        when {
            lowerCommand.contains("تغريدات") || lowerCommand.contains("تويتر") || lowerCommand.contains("إكس") -> {
                socialMediaManager.openTimeline()
            }
            lowerCommand.contains("رسائل") || lowerCommand.contains("واتساب") || lowerCommand.contains("خاص") -> {
                socialMediaManager.openDirectMessages()
            }
            lowerCommand.contains("رد") || lowerCommand.contains("إرسال") -> {
                speak("لمن تود إرسال الرسالة؟")
            }
            lowerCommand.contains("مساعدة") -> {
                speak("يمكنك قول: تغريدات، رسائل، أو متابعة شخص جديد.")
            }
            else -> {
                speak("عذراً، لم أفهم الأمر الاجتماعي. قل مساعدة.")
            }
        }
    }

    fun startListening() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-SA")
        }
        speechRecognizer.startListening(intent)
    }

    private fun speak(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "SocialVoiceID")
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        tts.stop()
        tts.shutdown()
        speechRecognizer.destroy()
        super.onDestroy()
    }
}
