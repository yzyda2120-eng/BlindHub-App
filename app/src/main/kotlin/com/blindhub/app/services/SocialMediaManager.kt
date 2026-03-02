package com.blindhub.app.services

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log

/**
 * مدير التواصل الاجتماعي - Social Media Manager (Twitter + WhatsApp for Blind)
 * يوفر ميزات: الخط الزمني الصوتي، المتابعة، والرسائل الخاصة.
 */
class SocialMediaManager(private val context: Context, private val tts: TextToSpeech) {

    // بيانات تجريبية (Mock Data)
    private val timelinePosts = listOf(
        "أحمد: السلام عليكم، كيف حالكم يا أصدقاء بلايند هاب؟",
        "سارة: هل جربتم ميزة الألعاب الصوتية الجديدة؟ رائعة جداً!",
        "محمد: من يريد الانضمام لمجلس التقنية الليلة؟"
    )

    private val directMessages = mapOf(
        "أحمد" to listOf("كيف حالك؟", "هل أنت متاح للحديث؟"),
        "نورة" to listOf("شكراً على المساعدة اليوم.")
    )

    fun openTimeline() {
        speak("أهلاً بك في الخط الزمني الصوتي. إليك آخر التغريدات الصوتية من أصدقائك.")
        timelinePosts.forEachIndexed { index, post ->
            speak("التغريدة رقم ${index + 1}: $post")
        }
        speak("قل: رد، أو: متابعة، للتفاعل.")
    }

    fun openDirectMessages() {
        speak("لديك رسائل جديدة من ${directMessages.keys.joinToString(" و ")}.")
        speak("قل اسم الشخص لسماع الرسائل أو إرسال رد.")
    }

    fun sendVoiceMessage(recipient: String, message: String) {
        speak("جاري إرسال رسالتك الصوتية إلى $recipient.")
        Log.d("SocialMedia", "إرسال رسالة إلى $recipient: $message")
        speak("تم الإرسال بنجاح.")
    }

    private fun speak(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_ADD, null, "SocialMediaID")
    }
}
