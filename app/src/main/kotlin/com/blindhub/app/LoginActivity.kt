package com.blindhub.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * شاشة تسجيل الدخول - Login Activity
 * توفر خيارات تسجيل الدخول التقليدي والتسجيل السريع للمكفوفين.
 */
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // التحقق مما إذا كان المستخدم مسجلاً دخوله مسبقاً
        val sharedPref = getSharedPreferences("BlindHubAuth", Context.MODE_PRIVATE)
        if (sharedPref.getBoolean("isLoggedIn", false)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_login)

        val etUsername = findViewById<EditText>(R.id.et_username)
        val etPassword = findViewById<EditText>(R.id.et_password)
        val btnLogin = findViewById<Button>(R.id.btn_login)
        val btnQuickSignup = findViewById<Button>(R.id.btn_quick_signup)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                saveLoginSession(username)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "يرجى إدخال اسم المستخدم وكلمة المرور", Toast.LENGTH_SHORT).show()
            }
        }

        btnQuickSignup.setOnClickListener {
            // منطق التسجيل السريع: إنشاء حساب تلقائي للمكفوفين
            saveLoginSession("مستخدم_سريع")
            Toast.makeText(this, "تم التسجيل السريع بنجاح!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun saveLoginSession(username: String) {
        val sharedPref = getSharedPreferences("BlindHubAuth", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("isLoggedIn", true)
            putString("username", username)
            apply()
        }
    }
}
