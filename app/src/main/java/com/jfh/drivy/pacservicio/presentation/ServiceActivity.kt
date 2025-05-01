package com.jfh.drivy.pacservicio.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jfh.drivy.databinding.ActivityServiceBinding
import com.jfh.drivy.pacservicio.control.FabricaConcretaConductorAndroid
import com.jfh.drivy.pacservicio.control.FabricaConcretaPasajeroAndroid
import com.jfh.drivy.pacservicio.control.ServiceController

class ServiceActivity : AppCompatActivity() {
    lateinit var binding: ActivityServiceBinding
        private set

    private lateinit var controller: ServiceController
    private lateinit var action: String
    private lateinit var userType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.flechaRegreso.setOnClickListener { finish() }

        userType = intent.getStringExtra("USER_TYPE")!!
        action   = intent.getStringExtra("ACTION")!!

        val factory = if (userType == "pasajero")
            FabricaConcretaPasajeroAndroid(this)
        else
            FabricaConcretaConductorAndroid(this)

        controller = ServiceController(factory, this, userType)

        when (action) {
            "register" -> setupRegistrationForm()
            "login"    -> setupLoginForm()
        }
    }

    private fun setupLoginForm() {
        binding.apply {
            etCorreo.visibility         = View.VISIBLE
            etTelefono.visibility       = View.VISIBLE
            etNumeroLicencia.visibility = if (userType == "conductor") View.VISIBLE else View.GONE

            // ocultar resto
            etPassword.visibility         = View.GONE
            etDireccion.visibility        = View.GONE
            etImagenCredencial.visibility = View.GONE
            etImagenLicencia.visibility   = View.GONE
            etNombre.visibility           = View.GONE

            btnSubmit.text = "Iniciar sesión"
            btnSubmit.setOnClickListener { controller.login() }
        }
    }

    private fun setupRegistrationForm() {
        binding.apply {
            etCorreo.visibility           = View.VISIBLE
            etTelefono.visibility         = View.VISIBLE
            etPassword.visibility         = View.VISIBLE
            etDireccion.visibility        = View.VISIBLE
            etImagenCredencial.visibility = View.VISIBLE
            etNombre.visibility           = View.VISIBLE

            if (userType == "conductor") {
                etImagenLicencia.visibility = View.VISIBLE
                etNumeroLicencia.visibility = View.VISIBLE
            } else {
                etImagenLicencia.visibility = View.GONE
                etNumeroLicencia.visibility = View.GONE
            }

            btnSubmit.text = "Registrarse"
            btnSubmit.setOnClickListener { controller.register() }
        }
    }

    fun showResult(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
