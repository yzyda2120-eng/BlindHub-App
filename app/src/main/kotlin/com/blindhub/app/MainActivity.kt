package com.blindhub.app

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import java.util.Locale

/**
 * النشاط الرئيسي - BlindHub Creator Edition (v3.1)
 * منصة احترافية تدعم الإنشاء والنشر المخصص لكل قسم (تيك توك، تويتر، سناب، واتساب).
 */
class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var fabCreate: ExtendedFloatingActionButton
    private lateinit var tvSectionTitle: TextView
    private lateinit var tvSectionContent: TextView
    private var currentSection = "tiktok"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tts = TextToSpeech(this, this)
        setupCreatorGUI()
    }

    private fun setupCreatorGUI() {
        bottomNav = findViewById(R.id.bottom_navigation)
        fabCreate = findViewById(R.id.fab_create)
        tvSectionTitle = findViewById(R.id.tv_title)
        tvSectionContent = findViewById(R.id.tv_section_content)

        val btnSettings = findViewById<ImageButton>(R.id.btn_settings)
        val btnSearch = findViewById<ImageButton>(R.id.btn_search)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_tiktok -> {
                    updateSection("تيك توك", "مقاطع صوتية إبداعية للمكفوفين", "نشر فيديو صوتي", "tiktok")
                    true
                }
                R.id.nav_twitter -> {
                    updateSection("تويتر", "تغريدات وآراء المجتمع الصوتي", "كتابة تغريدة", "twitter")
                    true
                }
                R.id.nav_snap -> {
                    updateSection("سناب شات", "قصصك اليومية المباشرة", "إضافة قصة", "snap")
                    true
                }
                R.id.nav_whatsapp -> {
                    updateSection("واتساب", "محادثاتك الخاصة والمشفرة", "إرسال رسالة", "whatsapp")
                    true
                }
                else -> false
            }
        }

        fabCreate.setOnClickListener {
            val action = when (currentSection) {
                "tiktok" -> "فتح مسجل الفيديو الصوتي للنشر في تيك توك..."
                "twitter" -> "فتح واجهة التغريد الصوتي في تويتر..."
                "snap" -> "فتح كاميرا القصص الصوتية في سناب شات..."
                "whatsapp" -> "فتح قائمة جهات الاتصال لبدء محادثة واتساب..."
                else -> "فتح واجهة الإنشاء..."
            }
            speak(action)
            Toast.makeText(this, action, Toast.LENGTH_SHORT).show()
        }

        btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
            speak("فتح إعدادات الخصوصية والأمان المتقدمة.")
        }

        btnSearch.setOnClickListener {
            speak("البحث العالمي: ابحث عن أصدقاء أو مواضيع بدقة متناهية.")
        }
    }

    private fun updateSection(title: String, content: String, fabText: String, sectionId: String) {
        currentSection = sectionId
        tvSectionTitle.text = "BlindHub $title"
        tvSectionContent.text = content
        fabCreate.text = fabText
        fabCreate.contentDescription = "زر $fabText جديد"
        speak("انتقلت إلى قسم $title. $content.")
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale("ar")
            speak("مرحباً بك في بلايند هاب إصدار المنشئين. كل قسم الآن يدعم النشر المخصص.")
        }
    }

    private fun speak(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "CreatorAppID")
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}
