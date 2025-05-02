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

    private fun setupRegistrationForm() {
        binding.apply {
            etNombre.visibility           = View.VISIBLE
            etCorreo.visibility           = View.VISIBLE
            etPassword.visibility         = View.VISIBLE
            etTelefono.visibility         = View.VISIBLE
            etDireccion.visibility        = View.VISIBLE
            etImagenCredencial.visibility = View.VISIBLE

            if (userType == "conductor") {
                etNumeroLicencia.visibility = View.VISIBLE
                etImagenLicencia.visibility = View.VISIBLE
            } else {
                etNumeroLicencia.visibility = View.GONE
                etImagenLicencia.visibility = View.GONE
            }

            btnSubmit.text = "Registrarse"
            btnSubmit.setOnClickListener { controller.register() }
        }
    }

    private fun setupLoginForm() {
        binding.apply {
            etNombre.visibility           = View.GONE
            etCorreo.visibility           = View.VISIBLE
            etPassword.visibility         = View.VISIBLE
            etTelefono.visibility         =
                if (userType == "conductor") View.VISIBLE else View.GONE
            etDireccion.visibility        = View.GONE
            etImagenCredencial.visibility = View.GONE
            etNumeroLicencia.visibility   = View.GONE
            etImagenLicencia.visibility   = View.GONE

            btnSubmit.text = "Iniciar sesión"
            btnSubmit.setOnClickListener { controller.login() }
        }
    }

    fun showResult(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
