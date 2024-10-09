package com.example.craft_code_mobile_project

import ApiService
import UserDetailsResponse
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.craft_code_mobile_project.databinding.ActivityAccountDetailsBinding
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import utils.CameraFunctions

class AccountDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountDetailsBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Установка Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""

        // Инициализация SharedPreferences
        sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE)  // Используем то же имя, что и в LoginActivity

        // Инициализация API с учетом токена
        val token = sharedPreferences.getString("access_token", null)
        if (token != null) {
            val client = OkHttpClient.Builder()
                .addInterceptor(Interceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                    Log.d("AccountDetails", "Токен передан: $token") // Логирование токена
                    chain.proceed(request)
                }).build()

            apiService = Retrofit.Builder()
                .baseUrl("http://192.168.0.100:8080")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        } else {
            Toast.makeText(this, "Токен не найден, требуется авторизация", Toast.LENGTH_SHORT).show()
        }

        // По нажатию кнопки загружаем и отображаем данные пользователя
        binding.buttonSubmit.setOnClickListener {
            fetchUserDetails()
        }
    }

    private fun fetchUserDetails() {
        apiService.getUserDetails().enqueue(object : Callback<UserDetailsResponse> {
            override fun onResponse(call: Call<UserDetailsResponse>, response: Response<UserDetailsResponse>) {
                if (response.isSuccessful) {
                    val userDetails = response.body()
                    if (userDetails != null) {
                        saveUserDetailsToPreferences(userDetails)
                        updateUIWithUserDetails(userDetails)
                    } else {
                        Log.d("AccountDetails", "User details are null")
                    }
                } else {
                    Log.d("AccountDetails", "Error in response: ${response.errorBody()?.string()}")
                    Toast.makeText(this@AccountDetailsActivity, "Не удалось получить данные пользователя", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserDetailsResponse>, t: Throwable) {
                Log.d("AccountDetails", "Error fetching user details: ${t.message}")
                Toast.makeText(this@AccountDetailsActivity, "Ошибка подключения", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveUserDetailsToPreferences(userDetails: UserDetailsResponse) {
        val editor = sharedPreferences.edit()
        editor.putString("user_name", userDetails.name)
        editor.putString("user_email", userDetails.email)
        editor.putString("user_role", if (userDetails.is_employee == true) "Сотрудник" else "Администратор")
        editor.apply()

        Log.d("AccountDetails", "User details saved: ${userDetails.name}, ${userDetails.email}")
    }

    private fun updateUIWithUserDetails(userDetails: UserDetailsResponse) {
        val role = if (userDetails.is_employee == true) "Сотрудник" else "Администратор"

        // Устанавливаем значения в полях
        binding.inputFirstName.setText(userDetails.name ?: "Не указано")
        binding.inputPatronymic.setText(userDetails.patronymic ?: "Не указано")
        binding.inputFamilyName.setText(userDetails.surname ?: "Не указано")
        binding.inputRole.setText(role)
        binding.inputEmail.setText(userDetails.email ?: "Не указано")

        Log.d("AccountDetails", "UI updated: Name: ${userDetails.name}, Role: $role")
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
