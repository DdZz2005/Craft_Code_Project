package com.example.craft_code_mobile_project

import ApiService
import ItemResponse
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.craft_code_mobile_project.databinding.ActivityActionWhithTheQrBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActionWhithTheQrActivity : AppCompatActivity() {
    private lateinit var binding: ActivityActionWhithTheQrBinding
    private lateinit var apiService: ApiService
    private var serialNumber: String? = null
    private var itemName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActionWhithTheQrBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Инициализация Retrofit
        apiService = RetrofitClient.getClient().create(ApiService::class.java)

        // Получаем данные из Intent
        itemName = intent.getStringExtra("ITEM_NAME")
        serialNumber = intent.getStringExtra("ITEM_SERIAL_NUMBER") // Получаем серийный номер



        binding.qrResult.text = itemName


        // Обработчики кликов для кнопок
        binding.getInfo.setOnClickListener {
            serialNumber?.let { getItemInformation(it) }
        }

        binding.MarkMove.setOnClickListener {
            // Переход на экран обновления местоположения
            moveToUpdateItemLocation()
        }
    }

    private fun moveToUpdateItemLocation() {
        if (itemName != null) {
            // Переход на UpdateItemLocationActivity с передачей названия товара
            val intent = Intent(this@ActionWhithTheQrActivity, UpdateItemLocationActivity::class.java).apply {
                putExtra("ITEM_NAME", itemName)  // Передаем название товара
                putExtra("ITEM_SERIAL_NUMBER", serialNumber)
            }
            startActivity(intent)
        } else {
            Toast.makeText(this, "Нет имени товара для перемещения", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getItemInformation(serialNumber: String) {
        // Запрос к серверу для получения информации по серийному номеру
        apiService.getItemBySerialNumber(serialNumber).enqueue(object : Callback<ItemResponse> {
            override fun onResponse(call: Call<ItemResponse>, response: Response<ItemResponse>) {
                if (response.isSuccessful) {
                    val item = response.body()
                    item?.let {
                        itemName = it.name  // Сохраняем имя товара для использования
                        binding.qrResult.text = itemName // Отображаем имя товара в интерфейсе

                        // Передаем все данные товара в следующую активность
                        val intent = Intent(this@ActionWhithTheQrActivity, ProductInfoActivity::class.java).apply {
                            putExtra("ITEM_NAME", it.name)
                            putExtra("ITEM_SERIAL_NUMBER", it.serial_number)
                            putExtra("ITEM_WAREHOUSE", it.warehouse)

                        }

                        startActivity(intent)

                    }
                } else {
                    // Обработка ошибки ответа
                    Toast.makeText(this@ActionWhithTheQrActivity, "Ошибка при получении данных", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<ItemResponse>, t: Throwable) {
                // Обработка ошибки сети
                Toast.makeText(this@ActionWhithTheQrActivity, "Ошибка подключения: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
