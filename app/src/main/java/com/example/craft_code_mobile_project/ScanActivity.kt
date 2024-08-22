package com.example.craft_code_mobile_project

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult

class ScanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Принудительная установка ориентации экрана в портретную
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT


        // Запуск сканера
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Scan a QR code")
        integrator.setCameraId(0)
        integrator.setBeepEnabled(true)
        integrator.setBarcodeImageEnabled(true)
        integrator.setOrientationLocked(true)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (scanResult.contents != null) {
            // Переход к следующей активности с результатом сканирования
            val intent = Intent(this, ActionWhithTheQrActivity::class.java).apply {
                putExtra("QR_CODE", scanResult.contents)
            }
            startActivity(intent)
        } else {
            // Если сканирование было отменено
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
        }

        finish() // Завершаем текущую активность
    }

}
