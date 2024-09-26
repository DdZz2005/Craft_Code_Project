package authentication

import android.os.Bundle
import android.content.Intent
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.craft_code_mobile_project.databinding.ActivityRegistrationBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Импортируем необходимые классы
import UserProfileModel
import RetrofitClient
import ApiService
import android.util.Log


class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Инициализация Retrofit
        val retrofit = RetrofitClient.getClient("http://10.0.2.2:8080")
        apiService = retrofit.create(ApiService::class.java)

        // Кнопка для регистрации
        binding.btnRegister.setOnClickListener {
            signUpUser()
        }
    }

    private fun signUpUser() {

        val fields: Array<Pair<TextView, String>> = arrayOf(
            binding.etFirstName to "Поле не может быть пустым",
            binding.etLastName to "Поле не может быть пустым",
            binding.etMiddleName to "Поле не может быть пустым",
            binding.etEmail to "Поле не может быть пустым",
            binding.etPassword to "Поле не может быть пустым",
            binding.etConfirmPassword to "Поле не может быть пустым"
        )

        var allFieldsFilled = true

        for ((field, errorMsg) in fields) {
            if (field.text.toString().isEmpty()) {
                field.error = errorMsg
                allFieldsFilled = false
            }
        }

        if (!allFieldsFilled) {
            return
        }

        val firstName = binding.etFirstName.text.toString()
        val lastName = binding.etLastName.text.toString()
        val middleName = binding.etMiddleName.text.toString()
        val email = binding.etEmail.text.toString()
        val phoneNumber = "Не указано"
        val password = binding.etPassword.text.toString()
        val role = binding.spinnerRole.selectedItem.toString()



        // Создание объекта MyDataModel с введенными данными
        val newUser = UserProfileModel(
            first_name = firstName,
            last_name = lastName,
            middle_name = middleName,
            email = email,
            phone_number = phoneNumber,
            password = password,
            role = role
        )

        // Отправка данных на сервер с помощью Retrofit
        apiService.postUser(newUser).enqueue(object : Callback<UserProfileModel> {
            override fun onResponse(call: Call<UserProfileModel>, response: Response<UserProfileModel>) {
                if (response.isSuccessful) {
                    val responseData = response.body()
                    Log.d("ServerResponse", "Ответ сервера: $responseData")
                    val intent = Intent(this@RegistrationActivity, RegistrationSuccessActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("ServerError", "Ошибка ответа: $errorBody")
                    Toast.makeText(this@RegistrationActivity, "Ошибка регистрации: $errorBody", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<UserProfileModel>, t: Throwable) {
                Toast.makeText(this@RegistrationActivity, "Ошибка подключения: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
