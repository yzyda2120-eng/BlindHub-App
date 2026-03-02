package com.blindhub.app

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Locale

/**
 * النشاط الرئيسي - BlindHub Super App (Privacy & Security Edition)
 * منصة شاملة للمكفوفين تشمل إعدادات خصوصية وقفل دردشة.
 */
class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var btnSettings: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tts = TextToSpeech(this, this)
        setupGUI()
    }

    private fun setupGUI() {
        bottomNav = findViewById(R.id.bottom_navigation)
        btnSettings = findViewById(R.id.btn_settings)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_tiktok -> {
                    speak("أنت الآن في مقاطع تيك توك الصوتية.")
                    true
                }
                R.id.nav_twitter -> {
                    speak("أنت الآن في تغريدات تويتر.")
                    true
                }
                R.id.nav_snap -> {
                    speak("أنت الآن في قصص سناب شات.")
                    true
                }
                R.id.nav_whatsapp -> {
                    speak("أنت الآن في رسائل واتساب الخاصة.")
                    true
                }
                else -> false
            }
        }

        btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
            speak("فتح إعدادات الخصوصية والأمان.")
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale("ar")
            speak("أهلاً بك في منصة بلايند هاب الشاملة. المس أسفل الشاشة للتنقل، أو المس أعلى اليمين للإعدادات والخصوصية.")
        }
    }

    private fun speak(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "SuperAppID")
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}
