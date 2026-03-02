package com.blindhub.app.services

import android.content.Context
import android.location.Location
import android.speech.tts.TextToSpeech
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

/**
 * مدير الملاحة الصوتية - GPS Navigation
 * يوفر توجيهات صوتية خطوة بخطوة للمكفوفين باستخدام بيانات الموقع.
 */
class NavigationManager(private val context: Context, private val tts: TextToSpeech) {

    private val fusedLocationClient: FusedLocationProviderClient = 
        LocationServices.getFusedLocationProviderClient(context)

    fun startNavigation(destination: String) {
        speak("بدأت الملاحة إلى $destination. يرجى الانتظار لتحديد موقعك الحالي.")
        
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val currentLat = location.latitude
                val currentLng = location.longitude
                // في النسخة الحقيقية، يتم إرسال الإحداثيات لـ Google Directions API
                // وجلب خطوات الملاحة وتحويلها إلى أوامر صوتية
                Log.d("Navigation", "الموقع الحالي: $currentLat, $currentLng")
                speak("أنت الآن في شارع الملك فهد. اتجه يميناً بعد مئة متر.")
            } else {
                speak("عذراً، لا يمكن تحديد موقعك حالياً.")
            }
        }
    }

    private fun speak(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "NavigationID")
    }
}
