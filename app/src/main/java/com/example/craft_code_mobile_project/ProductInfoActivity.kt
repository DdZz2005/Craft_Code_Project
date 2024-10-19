package com.example.craft_code_mobile_project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.craft_code_mobile_project.databinding.ActivityProductInfoBinding

class ProductInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Получаем данные из Intent
        val itemName = intent.getStringExtra("ITEM_NAME")
        val itemDescription = intent.getStringExtra("ITEM_DESCRIPTION")
        val itemSerialNumber = intent.getStringExtra("ITEM_SERIAL_NUMBER")
        val itemWarehouse = intent.getStringExtra("ITEM_WAREHOUSE")
        val itemCompany = intent.getStringExtra("ITEM_COMPANY")


        // Устанавливаем значения в TextView
        binding.itemName.text = itemName
        binding.itemDescription.text = itemDescription
        binding.itemSerialNumber.text = itemSerialNumber
        binding.itemWarehouse.text = itemWarehouse
        binding.itemLocation.text = itemCompany

    }
}
