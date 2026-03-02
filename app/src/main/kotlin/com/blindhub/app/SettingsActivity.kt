package com.blindhub.app

import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat

/**
 * شاشة الإعدادات - Settings Activity (Privacy & Chat Lock Edition)
 * توفر خيارات خصوصية وقفل الدردشة للمكفوفين.
 */
class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val sharedPref = getSharedPreferences("BlindHubPrivacy", Context.MODE_PRIVATE)

        val spinner = findViewById<Spinner>(R.id.profile_visibility_spinner)
        val switchChatLock = findViewById<SwitchCompat>(R.id.switch_chat_lock)
        val btnSave = findViewById<Button>(R.id.btn_save_settings)

        // إعداد خيارات رؤية الملف الشخصي
        val options = arrayOf("الجميع", "الأصدقاء فقط", "لا أحد")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // استرجاع الإعدادات الحالية
        spinner.setSelection(sharedPref.getInt("profile_visibility", 0))
        switchChatLock.isChecked = sharedPref.getBoolean("chat_lock_enabled", false)

        btnSave.setOnClickListener {
            with(sharedPref.edit()) {
                putInt("profile_visibility", spinner.selectedItemPosition)
                putBoolean("chat_lock_enabled", switchChatLock.isChecked)
                apply()
            }
            Toast.makeText(this, "تم حفظ إعدادات الخصوصية بنجاح!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
