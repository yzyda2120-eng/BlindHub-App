package com.blindhub.app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.blindhub.app.auth.VoiceAuthManager
import com.blindhub.app.services.VoiceControlService

/**
 * النشاط الرئيسي - BlindHub Social Hub (WhatsApp + Twitter Edition)
 * يركز حصرياً على التواصل الاجتماعي المباشر للمكفوفين.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var voiceAuthManager: VoiceAuthManager
    private val PERMISSIONS_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (checkPermissions()) {
            startBlindAuth()
        } else {
            requestPermissions()
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            startBlindAuth()
        }
    }

    private fun startBlindAuth() {
        voiceAuthManager = VoiceAuthManager(this) { isVerified ->
            if (isVerified) {
                Log.d("MainActivity", "تم التحقق من المستخدم بنجاح.")
                startVoiceControlService()
            } else {
                Log.e("MainActivity", "فشل التحقق. الوصول مرفوض.")
            }
        }
        voiceAuthManager.startAuthentication()
    }

    private fun startVoiceControlService() {
        val intent = Intent(this, VoiceControlService::class.java)
        startService(intent)
    }

    override fun onDestroy() {
        if (::voiceAuthManager.isInitialized) {
            voiceAuthManager.destroy()
        }
        super.onDestroy()
    }
}
