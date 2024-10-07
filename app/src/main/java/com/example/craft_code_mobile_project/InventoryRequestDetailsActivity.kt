package com.example.craft_code_mobile_project

import StartInventory.ScanActivity2
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
        binding.inventoryRequestMessage.text = "Уважаемая $employeeName, ваш администратор отправил вам заявку на проведение инвентаризации в указанных ниже кабинетах/отделах:"
        binding.inventoryRequestRooms.text = rooms
        binding.inventoryRequestDeadline.text = deadline


        binding.startInventoryButton.setOnClickListener{
            val intent = Intent(this, ScanActivity2::class.java)
            startActivity(intent)
            finish()
        }
    }
}
