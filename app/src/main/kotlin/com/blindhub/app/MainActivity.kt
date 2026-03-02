package com.blindhub.app

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.blindhub.app.services.LegendaryAudioEngine
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Locale

/**
 * النشاط الرئيسي - BlindHub Legendary Edition (v3.0)
 * المنصة الأسطورية للمكفوفين، مع محرك صوتي من الجيل الجديد واستجابة فورية.
 */
class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var audioEngine: LegendaryAudioEngine
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var btnSettings: ImageButton
    private lateinit var btnSearch: ImageButton
    private lateinit var fabLegendary: FloatingActionButton
    private var isRecording = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tts = TextToSpeech(this, this)
        audioEngine = LegendaryAudioEngine(this)
        
        setupLegendaryGUI()
    }

    private fun setupLegendaryGUI() {
        bottomNav = findViewById(R.id.bottom_navigation)
        btnSettings = findViewById(R.id.btn_settings)
        btnSearch = findViewById(R.id.btn_search)
        fabLegendary = findViewById(R.id.fab_legendary_audio)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_tiktok -> {
                    speak("تيك توك الأسطوري: اسحب للأعلى والأسفل لسماع الإبداع.")
                    true
                }
                R.id.nav_twitter -> {
                    speak("تويتر الأسطوري: آخر أخبار المجتمع.")
                    true
                }
                R.id.nav_snap -> {
                    speak("سناب شات الأسطوري: قصص صوتية حصرية.")
                    true
                }
                R.id.nav_whatsapp -> {
                    speak("واتساب الأسطوري: محادثاتك الخاصة والمشفرة.")
                    true
                }
                else -> false
            }
        }

        btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
            speak("فتح إعدادات الخصوصية والأمان الأسطورية.")
        }

        btnSearch.setOnClickListener {
            speak("البحث العالمي الأسطوري: ابحث عن أصدقاء أو مواضيع بدقة متناهية.")
            Toast.makeText(this, "البحث الأسطوري قيد العمل...", Toast.LENGTH_SHORT).show()
        }

        fabLegendary.setOnClickListener {
            if (!isRecording) {
                audioEngine.startInstantRecording()
                speak("بدء تسجيل منشور صوتي أسطوري.")
                fabLegendary.setImageResource(android.R.drawable.ic_media_pause)
                isRecording = true
            } else {
                val filePath = audioEngine.stopInstantRecording()
                speak("تم إنهاء التسجيل بنجاح. المنشور جاهز للنشر.")
                fabLegendary.setImageResource(android.R.drawable.ic_btn_speak_now)
                isRecording = false
                Log.d("Legendary", "File saved at: $filePath")
            }
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale("ar")
            speak("مرحباً بك في بلايند هاب الإصدار الأسطوري. التجربة الأكمل للمكفوفين.")
        }
    }

    private fun speak(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "LegendaryID")
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        audioEngine.stopAnyPlayback()
        super.onDestroy()
    }
}
