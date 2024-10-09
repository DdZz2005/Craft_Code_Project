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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.craft_code_mobile_project.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import authentication.LoginActivity
import android.content.SharedPreferences
import android.util.Log

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var inventoryRequestAdapter: InventoryRequestAdapter
    private lateinit var apiService: ApiService
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("access_token", null)

        if (token != null) {
            initializeApiService(token)  // Инициализируем API с токеном
            fetchInventoryRequests()     // Загружаем заявки
        } else {
            redirectToLogin()            // Перенаправляем на логин
        }

        binding.btnRefresh.setOnClickListener {
            fetchInventoryRequests()     // Обновляем заявки при нажатии
        }
    }

    // Метод для инициализации Retrofit с токеном
    private fun initializeApiService(token: String) {
        apiService = RetrofitClient.getClient(token).create(ApiService::class.java)
    }

    // Метод для загрузки заявок
    private fun fetchInventoryRequests() {
        apiService.getPendingRequests().enqueue(object : Callback<List<InventoryRequest>> {
            override fun onResponse(
                call: Call<List<InventoryRequest>>,
                response: Response<List<InventoryRequest>>
            ) {
                if (response.isSuccessful) {
                    val requestList = response.body() ?: emptyList()
                    setupRecyclerView(requestList)  // Настраиваем RecyclerView
                } else {
                    Toast.makeText(this@MainActivity, "Ошибка: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<InventoryRequest>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Ошибка подключения", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Метод для настройки RecyclerView
    private fun setupRecyclerView(requestList: List<InventoryRequest>) {
        inventoryRequestAdapter = InventoryRequestAdapter(requestList) { selectedRequest ->
            openInventoryRequestDetails(selectedRequest)  // Открываем детали заявки
        }
        binding.recyclerView.adapter = inventoryRequestAdapter
    }

    // Метод для перехода к деталям заявки
    private fun openInventoryRequestDetails(request: InventoryRequest) {
        val intent = Intent(this, InventoryRequestDetailsActivity::class.java).apply {
            putExtra("employee_name", request.employee)

            // Преобразуем список складов в строку
            val roomsString = request.warehouses.joinToString(", ")
            putExtra("rooms", roomsString)

            putExtra("deadline", request.deadline)
            putExtra("REQUEST_ID", request.id.toString())
        }
        startActivity(intent)
    }

    // Метод для перенаправления на логин
    private fun redirectToLogin() {
        Toast.makeText(this, "Необходима авторизация", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
