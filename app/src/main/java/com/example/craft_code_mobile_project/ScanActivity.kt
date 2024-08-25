package com.example.craft_code_mobile_project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.journeyapps.barcodescanner.CompoundBarcodeView
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult

class ScanActivity : AppCompatActivity() {

    private lateinit var barcodeView: CompoundBarcodeView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)  // Убедитесь, что путь к вашему XML правильный

        // Инициализация CompoundBarcodeView
        barcodeView = findViewById(R.id.barcode_scanner)


        // Запуск непрерывного сканирования
        barcodeView.decodeContinuous(callback)
    }

    // Callback для обработки результатов сканирования
    private val callback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult?) {
            result?.let {
                // Обработка результата сканирования
                val qrCodeContent = result.text
                // Действия после сканирования, например, показать уведомление или перейти на другую активность
            }
        }

        override fun possibleResultPoints(resultPoints: List<com.google.zxing.ResultPoint>) {
            // Необязательная обработка возможных точек результата
        }
    }

    override fun onResume() {
        super.onResume()
        barcodeView.resume() // Возобновляем сканирование при возврате к активности
    }

    override fun onPause() {
        super.onPause()
        barcodeView.pause() // Приостанавливаем сканирование, когда активность на паузе
    }
}
