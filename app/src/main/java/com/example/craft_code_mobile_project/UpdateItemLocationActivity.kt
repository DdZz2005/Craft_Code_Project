package com.example.craft_code_mobile_project

import ApiService
import UpdateLocationRequest
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

        // Настройка Spinner для выбора локации
        setupLocationSpinner()

        // Обработчик для кнопки отправки
        binding.buttonSend.setOnClickListener {
            if (selectedLocation != null && serialNumber != null) {
                // Вызываем метод для отправки нового местоположения
                updateItemLocation(serialNumber!!, selectedLocation!!, itemName!!)
            } else {
                Toast.makeText(this, "Выберите местоположение или серийный номер не найден", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Настройка Spinner для выбора локации
    private fun setupLocationSpinner() {
        val spinner: Spinner = binding.spinnerLocation

        // Создание адаптера для Spinner с данными из ресурса locations.xml
        ArrayAdapter.createFromResource(
            this,
            R.array.location_list, // Массив из locations.xml
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        // Установка слушателя для выбора элемента из Spinner
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedLocation = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Не выбрано никакое местоположение
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
}
