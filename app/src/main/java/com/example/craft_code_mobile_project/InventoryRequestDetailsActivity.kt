package com.example.craft_code_mobile_project

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.craft_code_mobile_project.databinding.ActivityInventoryRequestDetailsBinding

class InventoryRequestDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInventoryRequestDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInventoryRequestDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Получение данных из Intent
        val employeeName = intent.getStringExtra("employee_name")
        val rooms = intent.getStringExtra("rooms")
        val deadline = intent.getStringExtra("deadline")

        // Заполнение данными элементов интерфейса
        binding.inventoryRequestMessage.text = "Уважаемый $employeeName, ваш администратор отправил вам заявку на проведение инвентаризации в указанных кабинетах/отделах:"
        binding.inventoryRequestRooms.text = rooms
        binding.inventoryRequestDeadline.text = deadline

        binding.startInventoryButton.setOnClickListener {
            // Логика для обработки нажатия на кнопку "Начать инвентаризацию"
            val intent = Intent(this, ScanActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
