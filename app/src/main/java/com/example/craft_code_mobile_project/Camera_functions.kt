package com.example.craft_code_mobile_project

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object CameraFunctions {

    const val CAMERA_PERMISSION_CODE = 1001

    // Проверка разрешения на использование камеры
    fun checkCameraPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    // Запрос разрешения на использование камеры
    fun requestCameraPermission(activity: AppCompatActivity) {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
    }
}
