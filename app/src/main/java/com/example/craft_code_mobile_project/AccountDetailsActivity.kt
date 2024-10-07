package com.example.craft_code_mobile_project

import ApiService
import UserProfileModel
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.craft_code_mobile_project.databinding.ActivityAccountDetailsBinding
import utils.CameraFunctions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        // Загрузка данных из SharedPreferences
        loadAccountDetailsFromPreferences()

        // Получаем данные об учетной записи через API и сохраняем их
        fetchAccountDetails()
    }

    fun fetchAccountDetails() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getUserDetails().enqueue(object : Callback<UserProfileModel> {
            override fun onResponse(call: Call<UserProfileModel>, response: Response<UserProfileModel>) {
                if (response.isSuccessful) {
                    val userDetails = response.body()
                    // Сохраняем данные в SharedPreferences
                    if (userDetails != null) {
                        saveAccountDetailsToPreferences(userDetails)
                        // Устанавливаем данные в поля
                        binding.inputFamilyName.setText(userDetails.surname)
                        binding.inputFirstName.setText(userDetails.name)
                        binding.inputPatronymic.setText(userDetails.patronymic)
                        binding.inputEmail.setText(userDetails.email)
                        binding.inputRole.setText(userDetails.role)
                    }
                }
            }

            override fun onFailure(call: Call<UserProfileModel>, t: Throwable) {
                Toast.makeText(this@AccountDetailsActivity, "Ошибка загрузки данных", Toast.LENGTH_SHORT).show()
            }
        })
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

    fun loadAccountDetailsFromPreferences() {
        val sharedPreferences = getSharedPreferences("account_prefs", MODE_PRIVATE)
        val surname = sharedPreferences.getString("surname", "")
        val name = sharedPreferences.getString("name", "")
        val patronymic = sharedPreferences.getString("patronymic", "")
        val email = sharedPreferences.getString("email", "")
        val role = sharedPreferences.getString("role", "")

        // Устанавливаем данные в поля
        binding.inputFamilyName.setText(surname)
        binding.inputFirstName.setText(name)
        binding.inputPatronymic.setText(patronymic)
        binding.inputEmail.setText(email)
        binding.inputRole.setText(role)
    }

    fun saveAccountDetailsToPreferences(userDetails: UserProfileModel) {
        val sharedPreferences = getSharedPreferences("account_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("surname", userDetails.surname)
        editor.putString("name", userDetails.name)
        editor.putString("patronymic", userDetails.patronymic)
        editor.putString("email", userDetails.email)
        editor.putString("role", userDetails.role)
        editor.apply()  // Сохраняем изменения
    }
}
