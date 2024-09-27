package com.example.craft_code_mobile_project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragment_admin.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlankFragment_admin : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Установка Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""

        // Установка макета меню в Toolbar
        binding.toolbar.inflateMenu(R.menu.main_menu)

        // Получение меню из Toolbar
        val menu = binding.toolbar.menu

        for (i in 0 until menu.size()) {
            val menuItem = menu.getItem(i)
            val itemView = menuItem.actionView ?: continue

            // Установка LayoutParams для равномерного распределения
            val params = Toolbar.LayoutParams(0, Toolbar.LayoutParams.MATCH_PARENT)
            itemView.layoutParams = params
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank_admin1, container, false)


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment_admin.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BlankFragment_admin().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}