package com.jfh.drivy.pacconductor.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jfh.drivy.databinding.ActivityConductorMainBinding

class MainConductorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConductorMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConductorMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Solo mostramos un texto
        binding.textTitle.text = "Pantalla principal conductor"
    }
}
