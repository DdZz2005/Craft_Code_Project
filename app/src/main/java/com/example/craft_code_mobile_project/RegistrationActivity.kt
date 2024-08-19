package com.example.craft_code_mobile_project

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.craft_code_mobile_project.databinding.ActivityRegistrationBinding


class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnRegister.setOnClickListener {
            signUpUser()
        }
    }

    private fun signUpUser() {
        val fields: Array<Pair<TextView, String>> = arrayOf(
            binding.etFirstName to "Поле не может быть пустым",
            binding.etLastName to "Поле не может быть пустым",
            binding.etMiddleName to "Поле не может быть пустым",
            binding.etEmail to "Поле не может быть пустым",
            binding.etPassword to "Поле не может быть пустым",
            binding.etConfirmPassword to "Поле не может быть пустым"
        )

        var allFieldsFilled = true

        for ((field, errorMsg) in fields) {
            if (field.text.toString().isEmpty()) {
                field.error = errorMsg
                allFieldsFilled = false
            }
        }

        if (!allFieldsFilled) {
            return
        }

        val firstName = binding.etFirstName.text.toString()
        val lastName = binding.etLastName.text.toString()
        val middleName = binding.etMiddleName.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etPasswordAccess.text.toString()

        if (password != confirmPassword) {
            Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show()
            return
        }

    }
}
