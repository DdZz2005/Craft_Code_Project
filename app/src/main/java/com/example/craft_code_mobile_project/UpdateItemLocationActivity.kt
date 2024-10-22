package com.example.craft_code_mobile_project

import ApiService
import UpdateLocationRequest
import WarehouseResponse
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.craft_code_mobile_project.databinding.ActivityUpdateItemLocationBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateItemLocationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateItemLocationBinding
    private lateinit var apiService: ApiService
    private var selectedLocation: String? = null
    private var serialNumber: String? = null  // Используем серийный номер
    private var itemName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateItemLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Инициализация Retrofit
        apiService = RetrofitClient.getClient().create(ApiService::class.java)

        // Получаем данные из Intent (серийный номер)
        serialNumber = intent.getStringExtra("ITEM_SERIAL_NUMBER")
        itemName = intent.getStringExtra("ITEM_NAME")

        binding.itemName.text = itemName  // Отображаем имя товара в заголовке

        // Загружаем список складов и настраиваем Spinner
        loadWarehouses()

        // Обработчик для кнопки отправки
        binding.buttonSend.setOnClickListener {
            if (selectedLocation != null && serialNumber != null) {
                updateItemLocation(serialNumber!!, selectedLocation!!, itemName!!)
            } else {
                Toast.makeText(this, "Выберите местоположение или серийный номер не найден", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Метод для отправки нового местоположения на сервер
    private fun updateItemLocation(serialNumber: String, location: String, name: String) {
        val updateRequest = UpdateLocationRequest(location) // Запрос обновления местоположения
        apiService.updateItemLocation(serialNumber, updateRequest).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // Переход на экран успеха
                    val intent = Intent(this@UpdateItemLocationActivity, UpdateItemLocationActivitySuccess::class.java).apply {
                        putExtra("NEW_LOCATION", location)
                    }
                    startActivity(intent)
                    finish() // Завершаем текущую активность
                } else {
                    Toast.makeText(this@UpdateItemLocationActivity, "Ошибка при обновлении местоположения", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@UpdateItemLocationActivity, "Ошибка сети: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Метод для загрузки складов с сервера
    private fun loadWarehouses() {
        apiService.getWarehouses().enqueue(object : Callback<List<WarehouseResponse>> {
            override fun onResponse(call: Call<List<WarehouseResponse>>, response: Response<List<WarehouseResponse>>) {
                if (response.isSuccessful) {
                    val warehouses = response.body() ?: emptyList()
                    setupLocationSpinner(warehouses) // Настраиваем Spinner с полученными складами
                } else {
                    Toast.makeText(this@UpdateItemLocationActivity, "Ошибка при загрузке складов", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<WarehouseResponse>>, t: Throwable) {
                Toast.makeText(this@UpdateItemLocationActivity, "Ошибка сети: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Настройка Spinner для выбора локации
    private fun setupLocationSpinner(warehouses: List<WarehouseResponse>) {
        val warehouseNames = warehouses.map { it.name } // Получаем список названий складов

        // Создаем адаптер для Spinner
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            warehouseNames
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerLocation.adapter = adapter

        // Устанавливаем слушатель для выбора склада
        binding.spinnerLocation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedLocation = warehouses[position].name  // Сохраняем выбранное имя склада
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Не выбрано местоположение
            }
        }
    }
}
