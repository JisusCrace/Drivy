package com.jfh.drivy.pacservicio.presentation

import android.app.Activity
import android.content.Intent
import android.net.Uri
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
    private lateinit var action:     String
    private lateinit var userType:   String

    private val REQ_CRED = 1001
    private val REQ_LIC  = 1002

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

            ivImagenCredencial.visibility = View.VISIBLE
            etImagenCredencial.visibility = View.VISIBLE
            ivImagenCredencial.setOnClickListener { pickImage(REQ_CRED) }

            if (userType == "conductor") {
                ivImagenLicencia.visibility = View.VISIBLE
                etImagenLicencia.visibility = View.VISIBLE
                ivImagenLicencia.setOnClickListener { pickImage(REQ_LIC) }

                etNumeroLicencia.visibility = View.VISIBLE
            } else {
                ivImagenLicencia.visibility   = View.GONE
                etImagenLicencia.visibility   = View.GONE
                etNumeroLicencia.visibility   = View.GONE
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
            ivImagenCredencial.visibility = View.GONE
            etImagenCredencial.visibility = View.GONE
            ivImagenLicencia.visibility   = View.GONE
            etImagenLicencia.visibility   = View.GONE
            etNumeroLicencia.visibility   = View.GONE

            btnSubmit.text = "Iniciar sesión"
            btnSubmit.setOnClickListener { controller.login() }
        }
    }

    private fun pickImage(requestCode: Int) {
        Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
            startActivityForResult(this, requestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK || data?.data == null) return

        val uri: Uri = data.data!!
        when (requestCode) {
            REQ_CRED -> {
                binding.ivImagenCredencial.setImageURI(uri)
                binding.etImagenCredencial.setText(uri.toString())
            }
            REQ_LIC  -> {
                binding.ivImagenLicencia.setImageURI(uri)
                binding.etImagenLicencia.setText(uri.toString())
            }
        }
    }

    fun showResult(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
