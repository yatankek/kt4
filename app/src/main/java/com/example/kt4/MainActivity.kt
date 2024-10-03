package com.example.kt4

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Загрузка фрагмента камеры по умолчанию
        loadFragment(CameraFragment())

        // Инициализация BottomNavigationView и установка слушателя
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_camera -> {
                    loadFragment(CameraFragment())
                    true
                }
                R.id.navigation_list -> {
                    loadFragment(ListFragment())
                    true
                }
                else -> false
            }
        }
    }

    // Метод для замены фрагментов
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}