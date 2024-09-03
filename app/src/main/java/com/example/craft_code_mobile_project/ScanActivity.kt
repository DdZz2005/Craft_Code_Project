package com.example.craft_code_mobile_project

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.journeyapps.barcodescanner.CompoundBarcodeView
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult

class ScanActivity : AppCompatActivity() {

    private lateinit var barcodeView: CompoundBarcodeView

    // Callback для обработки результатов сканирования
    private val callback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult?) {
            result?.let {
                val qrCodeContent = result.text
                // Переход к следующей активности с результатом сканирования
                val intent = Intent(this@ScanActivity, ActionWhithTheQrActivity::class.java).apply {
                    putExtra("QR_CODE", qrCodeContent)
                }
                startActivity(intent)
                finish() // Завершаем текущую активность после сканирования
            }
        }

        override fun possibleResultPoints(resultPoints: List<com.google.zxing.ResultPoint>) {
            // Необязательная обработка возможных точек результата
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan) // Убедитесь, что путь к вашему XML правильный

        // Принудительная установка ориентации экрана в портретную
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // Инициализация CompoundBarcodeView
        barcodeView = findViewById(R.id.barcode_scanner)

        // Запуск непрерывного сканирования
        barcodeView.decodeContinuous(callback)
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
