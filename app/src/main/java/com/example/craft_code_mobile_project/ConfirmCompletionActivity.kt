package com.example.craft_code_mobile_project

import ApiService
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.craft_code_mobile_project.databinding.ActivityConfirmCompletionBinding

class ConfirmCompletionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmCompletionBinding
    private var requestId: String? = null
    private lateinit var scannedItems: List<String>
    private var name: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmCompletionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Получаем данные из Intent

        requestId = intent.getStringExtra("REQUEST_ID")
        scannedItems = intent.getStringArrayListExtra("SCANNED_ITEMS") ?: emptyList()

        // Кнопка "Подтвердить"
        binding.btnConfirm.setOnClickListener {
            requestId?.let {
                completeInventory(it, scannedItems)
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

        apiService.completeInventoryRequest(requestId, requestBody).enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: retrofit2.Call<Void>, response: retrofit2.Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ConfirmCompletionActivity, "Инвентаризация завершена", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@ConfirmCompletionActivity, "Ошибка: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<Void>, t: Throwable) {
                Toast.makeText(this@ConfirmCompletionActivity, "Ошибка подключения", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
