package com.example.craft_code_mobile_project

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.craft_code_mobile_project.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Установка Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title=""

        // Установка макета меню в Toolbar
        binding.toolbar.inflateMenu(R.menu.main_menu)

        // Получение меню из Toolbar
        val menu = binding.toolbar.menu


        for (i in 0 until menu.size()) {
            val menuItem = menu.getItem(i)
            val itemView = menuItem.actionView ?: continue

            // Установка LayoutParams для равномерного распределения
            val params = Toolbar.LayoutParams(0, Toolbar.LayoutParams.MATCH_PARENT)
            itemView.layoutParams = params
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.account_details -> {
                val intent = Intent(this, AccountDetailsActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.company_details -> {
                val intent = Intent(this, CompanyDetailsActivity::class.java)
                startActivity(intent)
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
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
