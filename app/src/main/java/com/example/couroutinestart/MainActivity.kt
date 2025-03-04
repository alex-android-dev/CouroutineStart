package com.example.couroutinestart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.couroutinestart.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.buttonLoad.setOnClickListener {
            binding.progress.isVisible = true
            binding.buttonLoad.isEnabled = false


            // launch - это корутин билдер
            val jobCity = lifecycleScope.launch {
                val city = loadCity()
                binding.tvLocation.text = city
            } // корутина запустилась

            val jobTemperature = lifecycleScope.launch {
                val temperature = loadTemperature()
                binding.tvTemperature.text = temperature.toString()
            } // корутина запустилась


            // мы это запустили в другой корутине, чтобы она отслеживала завершение двух других корутин
            lifecycleScope.launch {
                jobCity.join()
                // ждем завершения корутины

                jobTemperature.join()
                // ждем завершения корутины

                // метод останавливает корутину до тех пор пока работае не будет выполнена.
                // Когда она выполнится, то мы перейдем к следующему объекту (температура)
                // Данный метод позволяет только ДОЖДАТЬСЯ когда корутина выполнится. Он её НЕ запускает

                binding.progress.isVisible = false
                binding.buttonLoad.isEnabled = true
            }
        }
    }

    private suspend fun loadCity(): String {
        delay(5000)
        val city = "Moscow"
        return city
    }

    private suspend fun loadTemperature(): Int {
        delay(5000)
        return 17
    }
}