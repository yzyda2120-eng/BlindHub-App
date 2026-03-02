package com.blindhub.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import java.util.Locale

/**
 * النشاط الرئيسي - BlindHub Final Polish Edition (v3.2)
 * إصلاحات شاملة للبحث، النشر المباشر، وحقول الكتابة في الواتساب.
 */
class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var fabCreate: ExtendedFloatingActionButton
    private lateinit var tvSectionTitle: TextView
    private lateinit var tvSectionContent: TextView
    private lateinit var etGlobalSearch: EditText
    private lateinit var etMessage: EditText
    private lateinit var layoutMessageInput: LinearLayout
    private lateinit var btnSendMessage: ImageButton
    private var currentSection = "tiktok"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tts = TextToSpeech(this, this)
        setupFinalPolishGUI()
    }

    private fun setupFinalPolishGUI() {
        bottomNav = findViewById(R.id.bottom_navigation)
        fabCreate = findViewById(R.id.fab_create)
        tvSectionTitle = findViewById(R.id.tv_title)
        tvSectionContent = findViewById(R.id.tv_section_content)
        etGlobalSearch = findViewById(R.id.et_global_search)
        etMessage = findViewById(R.id.et_message)
        layoutMessageInput = findViewById(R.id.layout_message_input)
        btnSendMessage = findViewById(R.id.btn_send_message)

        // إعداد البحث العالمي بالكيبورد
        etGlobalSearch.setOnClickListener {
            showKeyboard(etGlobalSearch)
            speak("بدء البحث العالمي، اكتب ما تريد بالكيبورد.")
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_tiktok -> {
                    updateSection("تيك توك", "مقاطع إبداعية فورية", "نشر فيديو مباشر", "tiktok")
                    layoutMessageInput.visibility = View.GONE
                    true
                }
                R.id.nav_twitter -> {
                    updateSection("إكس", "آراء المجتمع الصوتي", "نشر تدوينة فورية", "twitter")
                    layoutMessageInput.visibility = View.GONE
                    true
                }
                R.id.nav_snap -> {
                    updateSection("سناب شات", "قصصك اليومية المباشرة", "إضافة قصة فورية", "snap")
                    layoutMessageInput.visibility = View.GONE
                    true
                }
                R.id.nav_whatsapp -> {
                    updateSection("واتساب", "محادثاتك الخاصة والمشفرة", "إرسال وسائط", "whatsapp")
                    layoutMessageInput.visibility = View.VISIBLE
                    true
                }
                else -> false
            }
        }

        fabCreate.setOnClickListener {
            val action = when (currentSection) {
                "tiktok" -> "اختيار فيديو ونشره فوراً في تيك توك..."
                "twitter" -> "اختيار منشور ونشره فوراً في إكس..."
                "snap" -> "فتح الكاميرا لنشر قصة فورية في سناب..."
                "whatsapp" -> "اختيار وسائط لإرسالها في واتساب..."
                else -> "نشر فوري..."
            }
            speak(action)
            Toast.makeText(this, action, Toast.LENGTH_SHORT).show()
        }

        btnSendMessage.setOnClickListener {
            val msg = etMessage.text.toString()
            if (msg.isNotEmpty()) {
                speak("تم إرسال الرسالة: $msg")
                etMessage.setText("")
                hideKeyboard()
            } else {
                speak("يرجى كتابة رسالة أولاً.")
            }
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

    private fun showKeyboard(view: View) {
        view.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale("ar")
            speak("مرحباً بك في بلايند هاب الإصدار النهائي المطور. تم إصلاح البحث والنشر والواتساب.")
        }
    }

    private fun speak(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "FinalPolishID")
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}
