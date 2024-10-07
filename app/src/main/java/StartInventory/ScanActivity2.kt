package StartInventory

import com.example.craft_code_mobile_project.ActionWhithTheQrActivity
import com.example.craft_code_mobile_project.R


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

class ScanActivity2 : AppCompatActivity() {

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

                // Тут реализовать процесс проверки ед.инвентаря и прибавления счетчика
            }
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
