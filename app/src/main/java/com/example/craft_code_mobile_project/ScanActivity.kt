package com.example.craft_code_mobile_project

import ApiService
import ItemResponse
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.journeyapps.barcodescanner.CompoundBarcodeView
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScanActivity : AppCompatActivity() {

    private lateinit var barcodeView: CompoundBarcodeView
    private lateinit var apiService: ApiService
    private var isScanned = false // Переменная для предотвращения многократного открытия окон

    // Callback для обработки результатов сканирования
    private val callback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult?) {
            if (result != null && !isScanned) {
                isScanned = true // Предотвращаем дальнейшие сканирования
                val serialNumber = result.text

                // Останавливаем дальнейшее сканирование
                barcodeView.pause()

                // Отправляем запрос на сервер для получения информации о товаре
                getItemInformation(serialNumber)
            }
        }

        override fun possibleResultPoints(resultPoints: List<com.google.zxing.ResultPoint>) {
            // Необязательная обработка возможных точек результата
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        // Принудительная установка ориентации экрана в портретную
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // Инициализация CompoundBarcodeView
        barcodeView = findViewById(R.id.barcode_scanner)

        // Запуск непрерывного сканирования
        barcodeView.decodeContinuous(callback)

        // Инициализация Retrofit
        val retrofit = RetrofitClient.getClient()
        apiService = retrofit.create(ApiService::class.java)
    }

    private fun getItemInformation(serialNumber: String) {
        // Запрос к серверу для получения информации по серийному номеру
        apiService.getItemBySerialNumber(serialNumber).enqueue(object : Callback<ItemResponse> {
            override fun onResponse(call: Call<ItemResponse>, response: Response<ItemResponse>) {
                if (response.isSuccessful) {
                    val item = response.body()
                    item?.let {
                        // Передаем все данные товара в следующую активность
                        val intent = Intent(this@ScanActivity, ActionWhithTheQrActivity::class.java).apply {
                            putExtra("ITEM_NAME", item.name)

                            putExtra("ITEM_SERIAL_NUMBER", item.serial_number)
                            putExtra("ITEM_WAREHOUSE", item.warehouse)

                        }
                        startActivity(intent)
                        finish() // Завершаем текущую активность после сканирования
                    }
                } else {
                    // Обработка ошибки ответа
                    Toast.makeText(this@ScanActivity, "Ошибка при получении данных", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ItemResponse>, t: Throwable) {
                // Обработка ошибки сети
                Toast.makeText(this@ScanActivity, "Ошибка подключения: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        isScanned = false // Сбрасываем состояние сканирования при возвращении
        barcodeView.resume() // Возобновляем сканирование при возврате к активности
    }

    override fun onPause() {
        super.onPause()
        barcodeView.pause() // Приостанавливаем сканирование, когда активность на паузе
    }
}
