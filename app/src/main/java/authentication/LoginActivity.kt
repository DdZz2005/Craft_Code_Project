package authentication
import ApiService
import UserLoginRequestForm
import UserLoginResponse
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.craft_code_mobile_project.MainActivity
import com.example.craft_code_mobile_project.R
import com.example.craft_code_mobile_project.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var pref: SharedPreferences
    private lateinit var binding: ActivityLoginBinding
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Инициализация Retrofit
        val retrofit = RetrofitClient.getClient()
        apiService = retrofit.create(ApiService::class.java)

        // Инициализация SharedPreferences
        pref = getSharedPreferences("auth", MODE_PRIVATE)

        // Проверка сохраненного состояния авторизации
        checkLoginState()

        // Клик по кнопке входа
        binding.btnLoginIn.setOnClickListener {
            val username = binding.etLogin.text.toString()
            val password = binding.etPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            loginInUser(username, password)
        }
    }

    private fun checkLoginState() {
        // Проверка сохраненного состояния авторизации
        val isLoggedIn = pref.getBoolean("isLoggedIn", true)
        if (isLoggedIn) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loginInUser(username: String, password: String) {
        // Создаем объект запроса с логином и паролем
        val loginRequest = UserLoginRequestForm(username, password)

        // Отправляем запрос на сервер через Retrofit
        apiService.loginUser(loginRequest).enqueue(object : Callback<UserLoginResponse> {
            override fun onResponse(call: Call<UserLoginResponse>, response: Response<UserLoginResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    // Log the successful response
                    Log.d("LOGIN_SUCCESS", response.body().toString())
                    // Save the login state
                    val editor = pref.edit()
                    editor.putBoolean("isLoggedIn", true)
                    editor.apply()

                    // Navigate to the main activity
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Log the error
                    Log.e("LOGIN_ERROR", "Response: ${response.errorBody()}")
                    Toast.makeText(this@LoginActivity, "Ошибка авторизации", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
                Log.e("LOGIN_FAILURE", t.message ?: "Unknown error")
                Toast.makeText(this@LoginActivity, "Ошибка подключения", Toast.LENGTH_SHORT).show()
            }
        })

    }
}
