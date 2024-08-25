package com.example.craft_code_mobile_project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.craft_code_mobile_project.databinding.ActivityCompanyDetailsBinding



class CompanyDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCompanyDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCompanyDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Инициализация ToolBar
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.title = "User"
    }
}