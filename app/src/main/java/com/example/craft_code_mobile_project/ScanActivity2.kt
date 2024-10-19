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
import androidx.appcompat.app.AlertDialog
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

    // Показать детали отсканированного товара в диалоговом окне
    private fun showScannedItemDetails(serialNumber: String) {
        val item = itemsToScan.find { it.serial_number == serialNumber }

        if (item != null) {
            // Товар найден в списке на сканирование
            val itemInfo = "Товар: ${item.name}\nМестоположение: ${item.warehouse}"
            showDialog("Детали товара", itemInfo, null)
        } else {
            // Товар не найден в списке
            showMissingItemDialog(serialNumber)
        }
    }

    // Метод для отображения диалогового окна с информацией о товаре
    private fun showDialog(title: String, message: String, onPositiveAction: (() -> Unit)?) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("ОК") { dialog, _ ->
            onPositiveAction?.invoke()
            dialog.dismiss()
        }
        builder.show()
    }

    // Метод для отображения диалога с вариантами действий, если товар не найден в списке на сканирование
    private fun showMissingItemDialog(serialNumber: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Товар не найден")
        builder.setMessage("Товар с серийным номером $serialNumber не найден в списке на сканирование. Что вы хотите сделать?")

        // Вариант оставить товар на месте
        builder.setPositiveButton("Оставить на месте") { dialog, _ ->
            Toast.makeText(this, "Товар оставлен на месте", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        // Вариант изменить местоположение товара
        builder.setNegativeButton("Изменить местоположение") { dialog, _ ->
            // Логика для изменения местоположения (например, можно открыть новую Activity для выбора местоположения)
            Toast.makeText(this, "Переходим к изменению местоположения", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
            navigateToChangeLocation(serialNumber) // Метод для изменения местоположения
        }

        builder.show()
    }

    // Метод для перехода на экран изменения местоположения товара
    private fun navigateToChangeLocation(serialNumber: String) {
        val intent = Intent(this, UpdateItemLocationActivity::class.java)
        intent.putExtra("SERIAL_NUMBER", serialNumber)
        startActivity(intent)
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
        apiService.getItems(requestId).enqueue(object : Callback<List<ItemResponse>> {
            override fun onResponse(call: Call<List<ItemResponse>>, response: Response<List<ItemResponse>>) {
                if (response.isSuccessful) {
                    val items = response.body() ?: listOf()
                    // Сохраняем загруженные товары в itemsToScan
                    itemsToScan = items
                    Log.d("Items", "Получено товаров: $itemsToScan")
                } else {
                    Log.e("Error", "Ошибка получения товаров: ${response.message()}")
                }
            }


            override fun onFailure(call: Call<List<ItemResponse>>, t: Throwable) {
                Log.e("Error", "Ошибка сети: ${t.message}")
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
