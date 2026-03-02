package com.blindhub.app.services

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log

/**
 * مركز الألعاب الصوتية - Audio Games Hub
 * يوفر ألعاباً تفاعلية تعتمد كلياً على الصوت (مثل: مسابقات المعلومات، ألعاب الذاكرة السمعية).
 */
class AudioGamesManager(private val context: Context, private val tts: TextToSpeech) {

    private val gamesList = listOf("مسابقة المعلومات العامة", "تحدي الذاكرة السمعية", "لعبة الأرقام")

    fun listGames() {
        val gamesText = "الألعاب المتاحة هي: " + gamesList.joinToString("، ") + ". قل اسم اللعبة لبدء اللعب."
        speak(gamesText)
    }

    fun startGame(gameName: String) {
        when {
            gameName.contains("معلومات") -> startQuizGame()
            gameName.contains("ذاكرة") -> startMemoryGame()
            gameName.contains("أرقام") -> startNumbersGame()
            else -> speak("عذراً، هذه اللعبة غير متوفرة حالياً.")
        }
    }

    private fun startQuizGame() {
        speak("بدأت مسابقة المعلومات العامة. السؤال الأول: ما هي عاصمة المملكة العربية السعودية؟")
        // منطق اللعبة: انتظار الإجابة الصوتية والتحقق منها
    }

    private fun startMemoryGame() {
        speak("بدأ تحدي الذاكرة السمعية. استمع للأصوات وحاول تكرارها.")
    }

    private fun startNumbersGame() {
        speak("بدأت لعبة الأرقام. سأقول رقماً وعليك قول الرقم التالي.")
    }

    private fun speak(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "AudioGameID")
    }
}
