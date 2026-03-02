package com.blindhub.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.blindhub.app.auth.VoiceAuthManager
import com.blindhub.app.services.SocialMediaManager
import android.speech.tts.TextToSpeech
import java.util.Locale

/**
 * النشاط الرئيسي - BlindHub GUI Social Edition (WhatsApp + Twitter)
 * واجهة رسومية تدعم قارئات الشاشة (TalkBack) بالكامل وتلغي الأوامر الصوتية.
 */
class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var voiceAuthManager: VoiceAuthManager
    private lateinit var socialMediaManager: SocialMediaManager
    private lateinit var tts: TextToSpeech
    private val PERMISSIONS_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tts = TextToSpeech(this, this)
        socialMediaManager = SocialMediaManager(this, tts)

        setupGUI()

        if (checkPermissions()) {
            startBlindAuth()
        } else {
            requestPermissions()
        }
    }

    private fun setupGUI() {
        val btnTimeline = findViewById<Button>(R.id.btn_timeline)
        val btnMessages = findViewById<Button>(R.id.btn_messages)
        val placeholderText = findViewById<TextView>(R.id.placeholder_text)

        btnTimeline.setOnClickListener {
            socialMediaManager.openTimeline()
            placeholderText.text = "أنت الآن في الخط الزمني (تويتر صوتي)"
        }

        btnMessages.setOnClickListener {
            socialMediaManager.openDirectMessages()
            placeholderText.text = "أنت الآن في الرسائل الخاصة (واتساب صوتي)"
        }
    }

    private fun checkPermissions(): Boolean {
        val permissions = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        return permissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSIONS_REQUEST_CODE
        )
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale("ar")
        }
    }

    private fun startBlindAuth() {
        voiceAuthManager = VoiceAuthManager(this) { isVerified ->
            if (isVerified) {
                tts.speak("تم التحقق بنجاح. المس الشاشة للتنقل بين الخط الزمني والرسائل.", TextToSpeech.QUEUE_FLUSH, null, "AuthSuccess")
            } else {
                tts.speak("فشل التحقق. هذا التطبيق للمكفوفين فقط.", TextToSpeech.QUEUE_FLUSH, null, "AuthFail")
            }
        }
        voiceAuthManager.startAuthentication()
    }

    override fun onDestroy() {
        if (::voiceAuthManager.isInitialized) {
            voiceAuthManager.destroy()
        }
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}
