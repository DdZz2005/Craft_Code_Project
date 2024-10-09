package com.example.craft_code_mobile_project

import ApiService
import CompanyDetailsResponse
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.craft_code_mobile_project.databinding.ActivityCompanyDetailsBinding
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import utils.CameraFunctions

class CompanyDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCompanyDetailsBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCompanyDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Initialize the toolbar
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.title = ""

        sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE)  // Используем то же имя, что и в LoginActivity

        // Инициализация API с учетом токена
        val token = sharedPreferences.getString("access_token", null)
        if (token != null) {
            val client = OkHttpClient.Builder()
                .addInterceptor(Interceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                    Log.d("CompanyDetailsActivity", "Токен передан: $token") // Логирование токена
                    chain.proceed(request)
                }).build()

            apiService = Retrofit.Builder()
                .baseUrl("http://192.168.0.100:8080")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)

            // Load company details
            fetchCompanyDetails()

        } else {
            Log.e("CompanyDetailsActivity", "Токен не найден в SharedPreferences") // Логирование, если токен не найден
            Toast.makeText(this, "Токен не найден, требуется авторизация", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchCompanyDetails() {
        apiService.getCompanyDetails().enqueue(object : Callback<CompanyDetailsResponse> {
            override fun onResponse(call: Call<CompanyDetailsResponse>, response: Response<CompanyDetailsResponse>) {
                if (response.isSuccessful) {
                    Log.d("CompanyDetailsActivity", "Full response: ${response.body()?.toString()}")
                    val companyDetails = response.body()
                    if (companyDetails != null) {
                        updateUIWithCompanyDetails(companyDetails)
                    } else {
                        Log.e("CompanyDetailsActivity", "companyDetails is null: ${response.errorBody()?.string()}")
                        Toast.makeText(this@CompanyDetailsActivity, "Ошибка получения данных компании", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("CompanyDetailsActivity", "Ошибка в ответе: ${response.errorBody()?.string()}")
                    Toast.makeText(this@CompanyDetailsActivity, "Не удалось получить данные компании", Toast.LENGTH_SHORT).show()
                }
            }


            override fun onFailure(call: Call<CompanyDetailsResponse>, t: Throwable) {
                Log.e("CompanyDetailsActivity", "Ошибка подключения: ${t.message}")
                Toast.makeText(this@CompanyDetailsActivity, "Ошибка подключения", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateUIWithCompanyDetails(companyDetails: CompanyDetailsResponse) {
        binding.companyNameInput.setText(companyDetails.name)
        binding.familyInput.setText(companyDetails.admin_surname)
        binding.nameInput.setText(companyDetails.admin_name)
        binding.patronymicInput.setText(companyDetails.admin_patronymic)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.company_details_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.account_details -> {
                val intent = Intent(this, AccountDetailsActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            R.id.action_document -> {
                // Handle document action
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
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
