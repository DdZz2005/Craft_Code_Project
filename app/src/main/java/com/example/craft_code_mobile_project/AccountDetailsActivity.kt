package com.example.craft_code_mobile_project

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.craft_code_mobile_project.databinding.ActivityAccountDetailsBinding
import utils.CameraFunctions


class AccountDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Инициализация ToolBar
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.title = "User"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.account_details_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.company_details -> {
                val intent = Intent(this, CompanyDetailsActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            R.id.action_document -> {
                // Действие при нажатии на иконку документа
                true
            }
            R.id.action_camera -> {
                // Проверка разрешений
                if (CameraFunctions.checkCameraPermission(this)) {
                    // Если разрешение уже предоставлено, запускаем ScanActivity
                    val intent = Intent(this, ScanActivity::class.java)
                    startActivity(intent)
                } else {
                    // Если разрешения нет, запрашиваем его
                    CameraFunctions.requestCameraPermission(this)
                }
                true
            }
            R.id.action_search -> {
                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
