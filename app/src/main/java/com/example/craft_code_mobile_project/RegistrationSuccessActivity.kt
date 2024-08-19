package com.example.craft_code_mobile_project

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.craft_code_mobile_project.databinding.ActivityRegistrationSuccessBinding
class RegistrationSuccessActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationSuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationSuccessBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val button = binding.btnContinue
        button.setOnClickListener {
            // Перейти в основное Activity или другое нужное Activity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

}
