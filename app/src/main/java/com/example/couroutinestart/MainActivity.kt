package com.example.couroutinestart

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.couroutinestart.databinding.ActivityMainBinding
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.buttonLoad.setOnClickListener {
            loadData()
        }
    }


    private fun loadData() {
        binding.progress.isVisible = true
        binding.buttonLoad.isEnabled = false

        loadCity { city ->
            binding.tvLocation.text = city
            loadTemperature(city) {
                loadTemperature(city) { temperature ->
                    binding.tvTemperature.text = temperature.toString()
                    binding.progress.isVisible = false
                    binding.buttonLoad.isEnabled = true
                }
            }
        }
    }

    private fun loadCity(callback: (String) -> Unit) {
        thread {
            val city = "Moscow"
            Thread.sleep(5000)
            callback.invoke(city)
        }
    }

    private fun loadTemperature(city: String, callback: (Int) -> Unit) {
        thread {
            Toast.makeText(
                this,
                getString(R.string.loading_temperature_toast, city),
                Toast.LENGTH_SHORT
            ).show()
            Thread.sleep(5000)
            callback.invoke(17)
        }
    }
}