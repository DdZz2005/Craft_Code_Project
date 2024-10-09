package com.example.craft_code_mobile_project

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.craft_code_mobile_project.databinding.ActivityScan2Binding
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.CompoundBarcodeView

class ScanActivity2 : AppCompatActivity() {

    private lateinit var barcodeView: CompoundBarcodeView
    private lateinit var binding: ActivityScan2Binding
    private var isScanned = false // Переменная для предотвращения повторного сканирования

    // Callback для обработки результатов сканирования
    private val callback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult?) {
            if (result != null && !isScanned) {
                isScanned = true
                val serialNumber = result.text
                Toast.makeText(this@ScanActivity2, "Товар успешно отсканирован: $serialNumber", Toast.LENGTH_SHORT).show()

                // Останавливаем сканирование
                barcodeView.pause()
                isScanned = false
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScan2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Инициализация сканера
        barcodeView = binding.barcodeScanner
        barcodeView.decodeContinuous(callback)
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
