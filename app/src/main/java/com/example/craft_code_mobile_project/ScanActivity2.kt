package com.example.craft_code_mobile_project

import ApiService
import ItemResponse
import RetrofitClient
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.craft_code_mobile_project.databinding.ActivityScan2Binding
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.CompoundBarcodeView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScanActivity2 : AppCompatActivity() {

    private lateinit var barcodeView: CompoundBarcodeView
    private lateinit var apiService: ApiService
    private var scannedItems: MutableSet<String> = mutableSetOf()
    private lateinit var textScan: TextView
    private lateinit var btnComplete: Button
    private lateinit var binding: ActivityScan2Binding
    private var itemsToScan: List<ItemResponse> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScan2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        barcodeView = binding.barcodeScanner
        textScan = binding.textScan
        btnComplete = binding.completeButton

        // Инициализация API клиента
        apiService = RetrofitClient.getClient().create(ApiService::class.java)

        // Получаем ID заявки как строку
        val requestId = intent.getStringExtra("REQUEST_ID")
        if (requestId == null) {
            Toast.makeText(this, "Ошибка: не удалось получить ID заявки", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        loadItemsFromRequest(requestId)

        barcodeView.decodeContinuous(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult?) {
                result?.let {
                    val serialNumber = it.text
                    if (scannedItems.add(serialNumber)) {
                        showScannedItemDetails(serialNumber)
                        updateScanCounter()
                    }
                }
            }
        })

        // Обработчик нажатия на кнопку завершения
        btnComplete.setOnClickListener {
            barcodeView.pause() // Останавливаем сканирование
            navigateToConfirmationScreen(requestId)
        }
    }

    // Показать детали отсканированного товара
    private fun showScannedItemDetails(serialNumber: String) {
        val item = itemsToScan.find { it.serial_number == serialNumber }
        if (item != null) {
            val itemInfo = "Товар: ${item.name}, Склад: ${item.warehouse}"
            Toast.makeText(this, itemInfo, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Товар не найден", Toast.LENGTH_SHORT).show()
        }
    }

    // Обновить счетчик сканированных товаров
    private fun updateScanCounter() {
        val totalItems = itemsToScan.size
        textScan.text = "Отсканировано: ${scannedItems.size} из $totalItems"
    }

    // Переход на экран подтверждения завершения
    private fun navigateToConfirmationScreen(requestId: String) {
        val intent = Intent(this, ConfirmCompletionActivity::class.java).apply {
            putExtra("REQUEST_ID", requestId)
            putStringArrayListExtra("SCANNED_ITEMS", ArrayList(scannedItems))
            Log.d("scannedItems", "REQUEST_ID: $requestId; SCANNED_ITEMS: ${ArrayList(scannedItems)}")
        }
        startActivity(intent)
    }

    // Загрузка товаров для заявки с сервера
    private fun loadItemsFromRequest(requestId: String) {
        apiService.getItemsForRequest(requestId.toInt()).enqueue(object : Callback<List<ItemResponse>> {
            override fun onResponse(call: Call<List<ItemResponse>>, response: Response<List<ItemResponse>>) {
                if (response.isSuccessful) {
                    itemsToScan = response.body() ?: listOf()
                    Log.d("ScanActivity2", "Загружены товары: $itemsToScan")
                    updateScanCounter() // Обновляем счетчик после загрузки
                } else {
                    Toast.makeText(this@ScanActivity2, "Ошибка получения товаров", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ItemResponse>>, t: Throwable) {
                Toast.makeText(this@ScanActivity2, "Ошибка подключения", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        Log.d("ScanActivity2", "Возобновляем сканирование")
        barcodeView.resume()
    }

    override fun onPause() {
        super.onPause()
        Log.d("ScanActivity2", "Сканирование приостановлено")
        barcodeView.pauseAndWait()
    }
}
