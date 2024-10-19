package com.example.craft_code_mobile_project

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ScannedItemDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanned_item_details)

        val itemName = findViewById<TextView>(R.id.itemName)
        val itemLocation = findViewById<TextView>(R.id.itemLocation)
        val btnContinueScanning = findViewById<Button>(R.id.btnContinueScanning)
        val btnCompleteInventory = findViewById<Button>(R.id.btnCompleteInventory)

        // Получаем данные о товаре из Intent
        val name = intent.getStringExtra("ITEM_NAME") ?: "Неизвестный товар"
        val location = intent.getStringExtra("ITEM_LOCATION") ?: "Местоположение неизвестно"

        // Устанавливаем данные в TextView
        itemName.text = "Товар: $name"
        itemLocation.text = "Местоположение: $location"

        // Обработчик кнопки продолжения сканирования
        btnContinueScanning.setOnClickListener {
            finish() // Закрываем это окно и возвращаемся к сканеру
        }

        // Обработчик кнопки завершения инвентаризации
        btnCompleteInventory.setOnClickListener {
            val requestId = intent.getStringExtra("REQUEST_ID") ?: return@setOnClickListener
            completeInventory(requestId)
        }
    }

    // Метод завершения инвентаризации
    private fun completeInventory(requestId: String) {
        Toast.makeText(this, "Инвентаризация завершена", Toast.LENGTH_SHORT).show()
        // Здесь можно выполнить HTTP-запрос для завершения инвентаризации
        finish() // Возвращаемся к предыдущей активности
    }
}
