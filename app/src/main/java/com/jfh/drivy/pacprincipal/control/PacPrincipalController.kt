package com.jfh.drivy.pacprincipal.control

import android.content.Context
import android.content.Intent
import com.jfh.drivy.pacservicio.presentation.ServiceActivity

class PacPrincipalController(private val context: Context) {
    private lateinit var userType: String

    fun onPasajeroClicked() {
        userType = "pasajero"
    }

    fun onConductorClicked() {
        userType = "conductor"
    }

    fun onRegistrarClicked() {
        navigate("register")
    }

    fun onLoginClicked() {
        navigate("login")
    }

    private fun navigate(action: String) {
        Intent(context, ServiceActivity::class.java).apply {
            putExtra("USER_TYPE", userType)
            putExtra("ACTION", action)
            context.startActivity(this)
        }
    }
}
