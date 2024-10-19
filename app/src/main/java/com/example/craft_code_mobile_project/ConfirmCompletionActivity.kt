package com.example.craft_code_mobile_project

import ApiService
import CompleteInventoryResponse
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.craft_code_mobile_project.databinding.ActivityConfirmCompletionBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConfirmCompletionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmCompletionBinding
    private var requestId: String? = null
    private lateinit var scannedItems: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmCompletionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Получаем данные из Intent
        requestId = intent.getStringExtra("REQUEST_ID")
        scannedItems = intent.getStringArrayListExtra("SCANNED_ITEMS") ?: emptyList()

        Log.d("scannedItems", "Отсканированные товары: $scannedItems")

        if (requestId == null) {
            Log.e("ConfirmCompletionActivity", "Не удалось получить ID заявки.")
            finish()
            return
        }

        // Кнопка "Подтвердить"
        binding.btnConfirm.setOnClickListener {
            requestId?.let {
                if (scannedItems.isNotEmpty()) {
                    completeInventory(it, scannedItems) // Используем requestId как строку
                } else {
                    Log.e("ConfirmCompletionActivity", "Список отсканированных товаров пуст.")
                    Toast.makeText(this, "Список отсканированных товаров пуст", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Кнопка "Назад"
        binding.btnCancel.setOnClickListener {
            finish() // Вернуться к предыдущему окну
        }
    }

    private fun completeInventory(requestId: String, scannedItems: List<String>) {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        val requestBody = mapOf("scanned_items" to scannedItems)

        Log.d("scannedItems", "requestBody: $requestBody")

        apiService.completeInventoryRequest(requestId, requestBody).enqueue(object : Callback<CompleteInventoryResponse> {
            override fun onResponse(
                call: Call<CompleteInventoryResponse>,
                response: Response<CompleteInventoryResponse>
            ) {
                if (response.isSuccessful) {
                    val completeResponse = response.body()
                    completeResponse?.let {
                        Log.d("CompleteInventory", "Сообщение: ${it.message}")
                        Log.d("CompleteInventory", "Отсутствующие товары: ${it.missing_items}")
                        Log.d("CompleteInventory", "Лишние товары: ${it.extra_items}")

                        // Переход на следующую активность после успешного завершения
                        navigateToNextActivity()
                    }
                } else {
                    Log.e("CompleteInventory", "Ошибка завершения: ${response.message()}")
                    Toast.makeText(this@ConfirmCompletionActivity, "Ошибка завершения инвентаризации", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CompleteInventoryResponse>, t: Throwable) {
                Log.e("CompleteInventory", "Ошибка подключения: ${t.message}")
                Toast.makeText(this@ConfirmCompletionActivity, "Ошибка подключения", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navigateToNextActivity() {
        val intent = Intent(this, MainActivity::class.java) // Укажите свою следующую активность
        startActivity(intent)
        finish() // Закрываем текущую активность
    }
}
