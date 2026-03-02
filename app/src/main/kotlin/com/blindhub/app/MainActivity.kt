package com.blindhub.app

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Locale

/**
 * النشاط الرئيسي - BlindHub Super App (TikTok + Twitter + Snap + WhatsApp)
 * منصة شاملة للمكفوفين تعتمد على اللمس وقارئات الشاشة.
 */
class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tts = TextToSpeech(this, this)
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        bottomNav = findViewById(R.id.bottom_navigation)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_tiktok -> {
                    speak("أنت الآن في مقاطع تيك توك الصوتية. اسحب للأعلى لسماع المقطع التالي.")
                    true
                }
                R.id.nav_twitter -> {
                    speak("أنت الآن في تغريدات تويتر. اسحب لسماع آراء المجتمع.")
                    true
                }
                R.id.nav_snap -> {
                    speak("أنت الآن في قصص سناب شات. هذه القصص ستختفي بعد يوم واحد.")
                    true
                }
                R.id.nav_whatsapp -> {
                    speak("أنت الآن في رسائل واتساب الخاصة. تواصل مباشرة مع أصدقائك.")
                    true
                }
                else -> false
            }
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale("ar")
            speak("أهلاً بك في منصة بلايند هاب الشاملة. المس أسفل الشاشة للتنقل بين تيك توك، تويتر، سناب، وواتساب.")
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
