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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.SharedPreferences
import android.util.Log
import authentication.LoginActivity
import okhttp3.Interceptor
import okhttp3.OkHttpClient

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var inventoryRequestAdapter: InventoryRequestAdapter
    private lateinit var apiService: ApiService
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Установка Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""

        // Настройка RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE)  // Используй то же имя, что и в LoginActivity
        val token = sharedPreferences.getString("access_token", null)
        if (token != null) {
            Log.d("MainActivity", "Токен найден: $token")
        } else {
            Log.d("MainActivity", "Токен не найден, требуется авторизация")
        }



        if (token != null) {
            val client = OkHttpClient.Builder()
                .addInterceptor(Interceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                    Log.d("MainActivity", "Запрос с токеном отправлен")
                    chain.proceed(request)
                }).build()


            apiService = RetrofitClient.getClient().create(ApiService::class.java)

            // Получение заявок с сервера
            fetchInventoryRequests()
        }

        else {
            Toast.makeText(this, "Необходима авторизация", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnRefresh.setOnClickListener {
            // Обновление заявок при нажатии кнопки
            fetchInventoryRequests()
        }
    }

    private fun fetchInventoryRequests() {
        // Запрос к API для получения заявок
        apiService.getPendingRequests().enqueue(object : Callback<List<InventoryRequest>> {
            override fun onResponse(call: Call<List<InventoryRequest>>, response: Response<List<InventoryRequest>>) {
                if (response.isSuccessful) {
                    val requestList = response.body() ?: emptyList()
                    inventoryRequestAdapter = InventoryRequestAdapter(requestList) { selectedRequest ->
                        val intent = Intent(this@MainActivity, InventoryRequestDetailsActivity::class.java).apply {
                            putExtra("employee_name", selectedRequest.employee)
                            putExtra("rooms", selectedRequest.warehouses.joinToString("\n") { "- $it" })
                            putExtra("deadline", selectedRequest.deadline)
                        }
                        startActivity(intent)
                    }
                    binding.recyclerView.adapter = inventoryRequestAdapter
                } else {
                    Toast.makeText(this@MainActivity, "Ошибка: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }


            override fun onFailure(call: Call<List<InventoryRequest>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Ошибка подключения", Toast.LENGTH_SHORT).show()
            }
        })
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
