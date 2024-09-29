package authentication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.craft_code_mobile_project.databinding.ActivityForgottenPasswordStartBinding


class ForgottenPasswordStartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgottenPasswordStartBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForgottenPasswordStartBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnNext.setOnClickListener {
            val login = binding.etLogin.text.toString()

            if (login.isEmpty()){
                binding.etLogin.error = "Введите email"
                return@setOnClickListener
            }
                // запрос на сервер

        }
    }
}