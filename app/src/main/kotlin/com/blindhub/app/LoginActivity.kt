package com.blindhub.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * شاشة تسجيل الدخول - Login Activity (Quick Social Edition)
 * توفر خيار تسجيل دخول سريع ومباشر للرسائل والمحادثات.
 */
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // التحقق من حالة تسجيل الدخول المسبقة
        val sharedPref = getSharedPreferences("BlindHubAuth", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            // إذا كان مسجلاً دخوله، ننتقل فوراً للمنصة الشاملة
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_login)

        val btnQuickStart = findViewById<Button>(R.id.btn_quick_start)
        val etInput = findViewById<EditText>(R.id.et_phone_or_name)
        val btnLogin = findViewById<Button>(R.id.btn_login)

        btnQuickStart.setOnClickListener {
            // تسجيل سريع باسم عشوائي أو مؤقت
            savePersistentSession("مستخدم_سريع_${(100..999).random()}")
            Toast.makeText(this, "تم التسجيل السريع! أهلاً بك في عالم بلايند هاب.", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        btnLogin.setOnClickListener {
            val input = etInput.text.toString()
            if (input.isNotEmpty()) {
                savePersistentSession(input)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "يرجى إدخال اسمك أو رقم هاتفك", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun savePersistentSession(identifier: String) {
        val sharedPref = getSharedPreferences("BlindHubAuth", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("isLoggedIn", true)
            putString("user_identifier", identifier)
            putLong("login_time", System.currentTimeMillis())
            apply()
        }
    }
}
