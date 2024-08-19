package com.example.craft_code_mobile_project

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.example.craft_code_mobile_project.databinding.ActivityLoginBinding
import com.example.myapplication.RegistrationActivity


class LoginActivity : AppCompatActivity() {

    private lateinit var pref: SharedPreferences
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Инициализация SharedPreferences
        pref = getSharedPreferences("startMain", MODE_PRIVATE)

        // Проверка, есть ли сохраненное состояние авторизации
        // Поменять на false, чтобы НЕ заходить с окон аутентификации!!!
        val isLoggedIn = pref.getBoolean("isLoggedIn", true)
        if (isLoggedIn) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        // Setup click listener for registration link
        val registrationTextView: TextView = findViewById(R.id.Registration)
        registrationTextView.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

        // Setup click listener for forgot password link
        val forgotPasswordTextView: TextView = findViewById(R.id.tvForgotPassword)
        forgotPasswordTextView.setOnClickListener {
            val intent = Intent(this, ForgottenPasswordStartActivity::class.java)
            startActivity(intent)
        }

        // Setup click listener for sign in button
        binding.btnSignIn.setOnClickListener {
            val email = binding.etLogin.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
                // Реализация аутентификации
        }
    }
}
