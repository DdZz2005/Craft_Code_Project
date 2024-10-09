package authentication

import ApiService
import LoginRequest
import TokenResponse
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.craft_code_mobile_project.MainActivity
import com.example.craft_code_mobile_project.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLoginIn.setOnClickListener {
            val email = binding.etLogin.text.toString()
            val password = binding.etPassword.text.toString()

            login(email, password)
        }
    }

    private fun login(email: String, password: String) {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        val loginRequest = LoginRequest(email, password)

        apiService.login(loginRequest).enqueue(object : Callback<TokenResponse> {
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                if (response.isSuccessful) {
                    val tokenResponse = response.body()
                    if (tokenResponse != null) {
                        // Сохраняем токен доступа в SharedPreferences
                        saveTokenToPreferences(tokenResponse.access)
                        // Переходим на экран с данными пользователя
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }

                else {
                    Toast.makeText(this@LoginActivity, "Неверные данные", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Ошибка подключения", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveTokenToPreferences(token: String) {
        val sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE)  // Убедись, что имя совпадает
        val editor = sharedPreferences.edit()
        editor.putString("access_token", token)
        editor.apply()
        Log.d("LoginActivity", "Токен сохранен: $token")
    }


}
