package com.jfh.drivy.pacservicio.control

import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.jfh.drivy.HomeConductorActivity
import com.jfh.drivy.HomePasajeroActivity
import com.jfh.drivy.pacadapter.control.Base64ImageAdapter
import com.jfh.drivy.pacservicio.abstraction.FabricaAbstractaUsuario
import com.jfh.drivy.pacservicio.presentation.ServiceActivity

class ServiceController(
    private val factory:  FabricaAbstractaUsuario,
    private val view:     ServiceActivity,
    private val userType: String
) {
    private val auth     = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
        .reference
        .child("Usuarios")

    fun register() {
        val original: Map<String, Any> = factory.crearFormularioRegistro().obtenerDatos()
        val datos = original.toMutableMap()

        val adapter = Base64ImageAdapter(view)
        (datos["imagenCredencial"] as? String)
            ?.takeIf { it.isNotBlank() }
            ?.let { datos["imagenCredencial"] = adapter.convert(it) }
        if (userType == "conductor") {
            (datos["imagenLicencia"] as? String)
                ?.takeIf { it.isNotBlank() }
                ?.let { datos["imagenLicencia"] = adapter.convert(it) }
        }

        val correo   = datos["correo"]   as String
        val password = datos["password"] as String

        auth.createUserWithEmailAndPassword(correo, password)
            .addOnSuccessListener {
                val uid = auth.currentUser!!.uid
                database.child(uid)
                    .setValue(datos)
                    .addOnSuccessListener { view.showResult("Registro exitoso") }
                    .addOnFailureListener { e ->
                        view.showResult("Error al guardar datos: ${e.message}")
                    }
            }
            .addOnFailureListener { e ->
                view.showResult("Error de Auth: ${e.message}")
            }
    }

    fun login() {
        val datos   = factory.crearFormularioAutenticacion().obtenerDatos()
        val correo   = datos["correo"]   as String
        val password = datos["password"] as String
        val telefono = datos["telefono"] as? String

        if (correo.isBlank() || password.isBlank() ||
            (userType == "conductor" && telefono.isNullOrBlank())
        ) {
            view.showResult("Por favor completa todos los campos")
            return
        }

        auth.signInWithEmailAndPassword(correo, password)
            .addOnSuccessListener {
                val uid = auth.currentUser!!.uid
                database.child(uid)
                    .get()
                    .addOnSuccessListener { snap ->
                        val estado = snap.child("estado").value as? String
                        if (estado != "verificado") {
                            view.showResult("Usuario no verificado")
                            return@addOnSuccessListener
                        }
                        if (userType == "conductor") {
                            val dbTel = snap.child("telefono").value as? String
                            if (dbTel != telefono) {
                                view.showResult("Teléfono no coincide")
                                return@addOnSuccessListener
                            }
                        }
                        navigateToHome()
                    }
                    .addOnFailureListener { e ->
                        view.showResult("Error al leer datos: ${e.message}")
                    }
            }
            .addOnFailureListener { e ->
                view.showResult("Credenciales inválidas: ${e.message}")
            }
    }

    private fun navigateToHome() {
        val ctx    = view
        val intent = Intent(
            ctx,
            if (userType == "pasajero")
                HomePasajeroActivity::class.java
            else
                HomeConductorActivity::class.java
        )
        ctx.startActivity(intent)
        ctx.finish()
    }
}
