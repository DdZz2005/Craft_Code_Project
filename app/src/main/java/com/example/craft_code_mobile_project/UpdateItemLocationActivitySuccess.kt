package com.example.craft_code_mobile_project

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.craft_code_mobile_project.databinding.ActivityUpdateItemLocationSuccessBinding

class UpdateItemLocationActivitySuccess : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateItemLocationSuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateItemLocationSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Получаем новое местоположение из предыдущего окна
        val newLocation = intent.getStringExtra("NEW_LOCATION")
        val itemName = intent.getStringExtra("ITEM_NAME")

        // Устанавливаем новое местоположение и имя товара
        binding.newLocation.text = newLocation
        binding.confirmationMessage.text = "Расположение товара изменено, текущее место нахождения товара ‘$itemName’:"

        // Обрабатываем нажатие на ссылку
        binding.linkConfirm.setOnClickListener {
            // Здесь вы можете отправить запрос или выполнить другие действия
            Toast.makeText(this, "Местоположение обновлено", Toast.LENGTH_SHORT).show()

            // Переход на другое Activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}