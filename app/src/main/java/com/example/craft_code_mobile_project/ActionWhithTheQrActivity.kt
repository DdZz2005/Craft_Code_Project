package com.example.craft_code_mobile_project

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.craft_code_mobile_project.databinding.ActivityActionWhithTheQrBinding

class ActionWhithTheQrActivity : AppCompatActivity() {
    private lateinit var binding: ActivityActionWhithTheQrBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActionWhithTheQrBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Получение данных из Intent

        binding.qrResult.text = intent.getStringExtra("QR_CODE")
        val qrCodeData = intent.getStringExtra("QR_CODE")
        if (qrCodeData != null) {
            Toast.makeText(this, "QR Code: $qrCodeData", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "No QR Code Data", Toast.LENGTH_LONG).show()
        }

        // Обработчики кликов для кнопок
        binding.getInfo.setOnClickListener {
            getInformation()
        }

        binding.MarkMove.setOnClickListener {
            markMove()
        }
    }

    private fun markMove() {
        Toast.makeText(this, "Отметка перемещения", Toast.LENGTH_SHORT).show()
    }

    private fun getInformation() {
        Toast.makeText(this, "Получение информации", Toast.LENGTH_SHORT).show()
    }
}
