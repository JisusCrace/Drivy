package com.jfh.drivy.pacpasajero.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jfh.drivy.databinding.ActivityPasajeroMainBinding

class MainPasajeroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPasajeroMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasajeroMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Solo mostramos un texto
        binding.textTitle.text = "Pantalla principal pasajero"
    }
}
