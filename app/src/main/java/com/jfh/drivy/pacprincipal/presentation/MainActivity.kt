package com.jfh.drivy.pacprincipal.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jfh.drivy.databinding.ActivityMainBinding
import com.jfh.drivy.pacprincipal.control.PacPrincipalController

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var controller: PacPrincipalController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        controller = PacPrincipalController(this)

        binding.btnPasajero.setOnClickListener {
            controller.onPasajeroClicked()
            binding.layoutUserType.visibility = View.GONE
            binding.layoutAction.visibility   = View.VISIBLE
        }
        binding.btnConductor.setOnClickListener {
            controller.onConductorClicked()
            binding.layoutUserType.visibility = View.GONE
            binding.layoutAction.visibility   = View.VISIBLE
        }

        binding.flechaMain.setOnClickListener {
            if (binding.layoutUserType.visibility == View.VISIBLE) {
                finishAffinity()
            } else {
                binding.layoutAction.visibility   = View.GONE
                binding.layoutUserType.visibility = View.VISIBLE
            }
        }

        binding.btnRegistrar.setOnClickListener {
            controller.onRegistrarClicked()
        }
        binding.btnLogin.setOnClickListener {
            controller.onLoginClicked()
        }
    }
}
