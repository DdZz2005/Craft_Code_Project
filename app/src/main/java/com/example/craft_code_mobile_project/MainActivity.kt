package com.example.craft_code_mobile_project

import API_service.InventoryRequestAdapter
import ApiService
import InventoryRequest
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.craft_code_mobile_project.databinding.ActivityMainBinding
import utils.CameraFunctions
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var inventoryRequestAdapter: InventoryRequestAdapter
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Установка Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""

        // Настройка RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // Инициализация Retrofit
        apiService = RetrofitClient.getClient().create(ApiService::class.java)

        loadRequestsFromPreferences()

        // Получение заявок с сервера
        binding.btnRefresh.setOnClickListener {
            fetchInventoryRequests()
        }

        // Загрузка заявок из SharedPreferences
        val requestList = loadRequestsFromPreferences()

        // Установка адаптера для RecyclerView с обработчиком кликов
        inventoryRequestAdapter = InventoryRequestAdapter(requestList) { selectedRequest ->
            val intent = Intent(this, InventoryRequestDetailsActivity::class.java).apply {
                putExtra("employee_name", selectedRequest.employee)
                putExtra("rooms", selectedRequest.warehouses.joinToString("\n") { "- $it" })  // Теперь здесь будут названия складов
                putExtra("deadline", selectedRequest.deadline)
            }
            startActivity(intent)
        }
        binding.recyclerView.adapter = inventoryRequestAdapter
    }





    private fun fetchInventoryRequests() {
        // Запрос к API для получения заявок с не завершённым статусом
        apiService.getPendingRequests().enqueue(object : Callback<List<InventoryRequest>> {
            override fun onResponse(
                call: Call<List<InventoryRequest>>,
                response: Response<List<InventoryRequest>>
            ) {
                if (response.isSuccessful) {
                    val requestList = response.body() ?: emptyList()

                    // Сохранение полученных заявок в SharedPreferences
                    saveRequestsToPreferences(requestList)

                    // Настройка адаптера для RecyclerView с обработчиком кликов
                    inventoryRequestAdapter = InventoryRequestAdapter(requestList) { selectedRequest ->
                        val intent = Intent(this@MainActivity, InventoryRequestDetailsActivity::class.java).apply {
                            putExtra("employee_name", selectedRequest.employee)
                            putExtra("rooms", selectedRequest.warehouses.joinToString("\n") { "- $it" })
                            putExtra("deadline", selectedRequest.deadline)
                        }
                        startActivity(intent)
                    }

                    binding.recyclerView.adapter = inventoryRequestAdapter
                }
            }

            override fun onFailure(call: Call<List<InventoryRequest>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Ошибка загрузки заявок", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveRequestsToPreferences(requestList: List<InventoryRequest>) {
        val sharedPreferences = getSharedPreferences("requests_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Преобразование списка заявок в JSON
        val gson = Gson()
        val json = gson.toJson(requestList)

        // Сохранение JSON строки в SharedPreferences
        editor.putString("inventory_requests", json)
        editor.apply()
    }

    private fun loadRequestsFromPreferences(): List<InventoryRequest> {
        val sharedPreferences = getSharedPreferences("requests_prefs", MODE_PRIVATE)
        val json = sharedPreferences.getString("inventory_requests", null)

        return if (json != null) {
            val gson = Gson()
            val type = object : TypeToken<List<InventoryRequest>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
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
                if (CameraFunctions.checkCameraPermission(this)) {
                    val intent = Intent(this, ScanActivity::class.java)
                    startActivity(intent)
                } else {
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
