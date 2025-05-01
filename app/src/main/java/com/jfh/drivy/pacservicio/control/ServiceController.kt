package com.jfh.drivy.pacservicio.control

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.jfh.drivy.pacservicio.abstraction.FabricaAbstractaUsuario
import com.jfh.drivy.pacservicio.presentation.ServiceActivity

class ServiceController(
    private val factory: FabricaAbstractaUsuario,
    private val view: ServiceActivity,
    private val userType: String
) {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    fun register() {
        // 1) Sacar datos, incluidos correo/password
        val datos = factory.crearFormularioRegistro().obtenerDatos()
        val correo   = datos["correo"]   as String
        val password = datos["password"] as String

        // 2) Crear usuario en Auth
        auth.createUserWithEmailAndPassword(correo, password)
            .addOnSuccessListener {
                val uid = auth.currentUser!!.uid
                // 3) Escribir en DB bajo /Usuarios/$uid
                database.child("Usuarios").child(uid)
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
        val datos = factory.crearFormularioAutenticacion().obtenerDatos()
        val correo = datos["correo"]   as String
        val password = datos["password"] as? String // pasajero no tiene license/password?
        val telefono = datos["telefono"] as String

        // 1) Login en Auth para generar token
        auth.signInWithEmailAndPassword(correo, password ?: "")
            .addOnSuccessListener {
                // 2) Verificar en DB (opcional, dado que Auth ya autorizó)
                view.showResult("Login exitoso")
            }
            .addOnFailureListener {
                view.showResult("Credenciales inválidas")
            }
    }
}
