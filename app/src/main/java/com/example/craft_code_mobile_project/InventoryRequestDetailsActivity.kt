package com.example.craft_code_mobile_project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.craft_code_mobile_project.databinding.ActivityInventoryRequestDetailsBinding

class InventoryRequestDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInventoryRequestDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInventoryRequestDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Получение данных из Intent
        val employeeName = intent.getStringExtra("employee_name") ?: "Неизвестный сотрудник"
        val rooms = intent.getStringExtra("rooms") ?: "Нет информации о кабинетах"
        val deadline = intent.getStringExtra("deadline") ?: "Нет информации о дедлайне"
        val requestId = intent.getStringExtra("REQUEST_ID")

        if (requestId == null) {
            Toast.makeText(this, "Не удалось получить ID заявки", Toast.LENGTH_SHORT).show()
            Log.e("InventoryRequestDetails", "REQUEST_ID отсутствует")
            finish()
            return
        }

        // Заполнение интерфейса данными
        binding.inventoryRequestMessage.text = "Уважаемый $employeeName, ваш администратор отправил вам заявку на проведение инвентаризации в указанных кабинетах: $rooms."
        binding.inventoryRequestDeadline.text = "Отчёт предоставить до: $deadline"

        // Обработчик нажатия на кнопку начала инвентаризации
        binding.startInventoryButton.setOnClickListener {
            navigateToScanActivity(requestId)
        }
    }

    // Метод для перехода на ScanActivity2 с передачей REQUEST_ID
    private fun navigateToScanActivity(requestId: String) {
        try {
            val intent = Intent(this, ScanActivity2::class.java).apply {
                putExtra("REQUEST_ID", requestId)
            }
            Log.d("InventoryRequestDetails", "Переход на ScanActivity2 с REQUEST_ID: $requestId")
            startActivity(intent)
            finish()
        } catch (e: Exception) {
            Log.e("InventoryRequestDetails", "Ошибка при переходе на ScanActivity2: ${e.message}")
            Toast.makeText(this, "Не удалось начать инвентаризацию", Toast.LENGTH_SHORT).show()
        }
    }
}
