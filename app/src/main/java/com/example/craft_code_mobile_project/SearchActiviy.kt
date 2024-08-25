package com.example.craft_code_mobile_project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.craft_code_mobile_project.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Установка Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""

        // Установка макета меню в Toolbar
        binding.toolbar.inflateMenu(R.menu.main_menu)

        // Получение меню из Toolbar
        val menu = binding.toolbar.menu

        // Добавление MenuItem программно, используя равные веса (layout_weight)
        for (i in 0 until menu.size()) {
            val menuItem = menu.getItem(i)
            val itemView = menuItem.actionView ?: continue

            // Установка LayoutParams для равномерного распределения
            val params = Toolbar.LayoutParams(0, Toolbar.LayoutParams.MATCH_PARENT)
            itemView.layoutParams = params
        }
    }
}