package com.blindhub.app

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Locale

/**
 * النشاط الرئيسي - BlindHub Premium Social App
 * منصة احترافية تجمع بين (تيك توك، تويتر، سناب، واتساب) في واجهة عصرية.
 */
class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var btnSettings: ImageButton
    private lateinit var btnSearch: ImageButton
    private lateinit var fabNew: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tts = TextToSpeech(this, this)
        setupPremiumGUI()
    }

    private fun setupPremiumGUI() {
        bottomNav = findViewById(R.id.bottom_navigation)
        btnSettings = findViewById(R.id.btn_settings)
        btnSearch = findViewById(R.id.btn_search)
        fabNew = findViewById(R.id.fab_new)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_tiktok -> {
                    speak("تيك توك: اسحب للأعلى لمقاطع صوتية إبداعية.")
                    true
                }
                R.id.nav_twitter -> {
                    speak("تويتر: آخر أخبار وآراء المجتمع.")
                    true
                }
                R.id.nav_snap -> {
                    speak("سناب شات: قصص صوتية حصرية.")
                    true
                }
                R.id.nav_whatsapp -> {
                    speak("واتساب: محادثاتك الخاصة والمشفرة.")
                    true
                }
                else -> false
            }
        }

        btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
            speak("فتح إعدادات الخصوصية والأمان المتقدمة.")
        }

        btnSearch.setOnClickListener {
            speak("البحث العالمي: ابحث عن أصدقاء أو مواضيع.")
            Toast.makeText(this, "البحث العالمي قيد التفعيل...", Toast.LENGTH_SHORT).show()
        }

        fabNew.setOnClickListener {
            speak("إضافة منشور أو رسالة صوتية جديدة.")
            Toast.makeText(this, "فتح مسجل الصوت الاحترافي...", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale("ar")
            speak("مرحباً بك في بلايند هاب بريميوم. تجربة اجتماعية عالمية للمكفوفين.")
        }
    }

    private fun speak(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "PremiumAppID")
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}
