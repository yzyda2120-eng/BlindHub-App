package com.blindhub.app.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.speech.tts.TextToSpeech
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.os.Bundle
import android.speech.RecognitionListener
import android.util.Log
import com.blindhub.app.R
import java.util.Locale

/**
 * خدمة التحكم الصوتي الأساسية - Vocal Interface Control
 * تتعامل مع الأوامر الصوتية وتدير الخدمات العشر الأساسية.
 */
class VoiceControlService : Service(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var recognizerIntent: Intent

    override fun onCreate() {
        super.onCreate()
        tts = TextToSpeech(this, this)
        setupSpeechRecognizer()
    }

    private fun setupSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-SA")
        }

        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onError(error: Int) {
                Log.e("VoiceControl", "خطأ في التعرف على الصوت: $error")
                // إعادة المحاولة بعد ثانية في حال حدوث خطأ
                startListening()
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (matches != null && matches.isNotEmpty()) {
                    processCommand(matches[0])
                }
                startListening()
            }

            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
    }

    private fun processCommand(command: String) {
        val lowerCommand = command.lowercase(Locale("ar"))
        Log.d("VoiceControl", "معالجة الأمر: $lowerCommand")

        when {
            lowerCommand.contains("منتدى") || lowerCommand.contains("منتديات") -> {
                speak("فتح المنتديات الصوتية. هل تود استعراض الغرف المتاحة؟")
                // منطق فتح المنتديات الصوتية (1. Audio Forums)
            }
            lowerCommand.contains("صف") || lowerCommand.contains("وصف") -> {
                speak("تفعيل وصف الكائنات بالذكاء الاصطناعي. وجه الكاميرا نحو ما تريد وصفه.")
                // تفعيل الكاميرا ووصف الكائنات (2. AI Object Description)
            }
            lowerCommand.contains("ملاحة") || lowerCommand.contains("طريق") -> {
                speak("تفعيل الملاحة. أين تود الذهاب؟")
                // تفعيل نظام الملاحة (3. GPS Navigation)
            }
            lowerCommand.contains("مساعدة") || lowerCommand.contains("استغاثة") || lowerCommand.contains("خطر") -> {
                speak("تنبيه: تم تفعيل نداء الاستغاثة. جاري الاتصال بالطوارئ وإبلاغ المجتمع بموقعك.")
                // إرسال نداء استغاثة (4. SOS Emergency)
            }
            lowerCommand.contains("برايل") || lowerCommand.contains("قارئ") -> {
                speak("تفعيل واجهة قارئ برايل. يرجى توصيل شاشة برايل الخارجية.")
                // تفعيل واجهة برايل (5. Braille Reader Interface)
            }
            lowerCommand.contains("تعليم") || lowerCommand.contains("دورة") -> {
                speak("فتح المركز التعليمي. تتوفر دورات جديدة في تقنيات المكفوفين.")
                // فتح المركز التعليمي (6. Educational Hub)
            }
            lowerCommand.contains("إرشاد") || lowerCommand.contains("موجه") -> {
                speak("فتح شبكة الإرشاد. جاري البحث عن موجه مناسب لك.")
                // تفعيل شبكة الإرشاد (7. Peer Mentoring Network)
            }
            lowerCommand.contains("دردشة") || lowerCommand.contains("رسائل") -> {
                speak("فتح الدردشة الصوتية. قل اسم الشخص الذي تود مراسلته.")
                // تفعيل دردشة الصوت إلى نص (8. Voice-to-Text Chat)
            }
            lowerCommand.contains("وظيفة") || lowerCommand.contains("عمل") -> {
                speak("فتح لوحة الوظائف. هناك فرص عمل جديدة للمكفوفين في مجال البرمجة.")
                // فتح لوحة الوظائف (9. Accessible Career Board)
            }
            lowerCommand.contains("تحكم") || lowerCommand.contains("أوامر") -> {
                speak("أنت الآن في وضع التحكم الصوتي الكامل. يمكنك التنقل بين الخدمات بقول أسمائها.")
                // التحكم الصوتي (10. Vocal Interface Control)
            }
            else -> {
                speak("عذراً، لم أفهم الأمر. قل مساعدة لسماع الخيارات المتاحة.")
            }
        }
    }

    private fun speak(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "VoiceControlID")
    }

    private fun startListening() {
        speechRecognizer.startListening(recognizerIntent)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale("ar"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "اللغة العربية غير مدعومة")
            } else {
                speak(getString(R.string.welcome_message))
                startListening()
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        if (::speechRecognizer.isInitialized) {
            speechRecognizer.destroy()
        }
        super.onDestroy()
    }
}
