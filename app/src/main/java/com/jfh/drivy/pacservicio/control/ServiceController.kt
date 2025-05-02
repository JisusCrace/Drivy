package com.jfh.drivy.pacservicio.control

import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.jfh.drivy.pacconductor.presentation.MainConductorActivity
import com.jfh.drivy.pacpasajero.presentation.MainPasajeroActivity
import com.jfh.drivy.pacservicio.abstraction.FabricaAbstractaUsuario
import com.jfh.drivy.pacservicio.presentation.ServiceActivity

class ServiceController(
    private val factory: FabricaAbstractaUsuario,
    private val view: ServiceActivity,
    private val userType: String
) {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference.child("Usuarios")

    /** Registra igual que antes… (se omite aquí para centrar en el login) */
    fun register() {
        // 1) Obtenemos todos los datos (incluyendo password)
        val datos = factory.crearFormularioRegistro().obtenerDatos()
        val correo   = datos["correo"]    as String
        val password = datos["password"]  as String

        // 2) Creamos usuario en FirebaseAuth
        auth.createUserWithEmailAndPassword(correo, password)
            .addOnSuccessListener {
                val uid = auth.currentUser!!.uid
                // 3) Guardamos el map completo en /Usuarios/$uid
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

    /** Login con validación de campos y estado/verificación */
    fun login() {
        val datos = factory.crearFormularioAutenticacion().obtenerDatos()

        // 1) Validación de campos requeridos
        val correo   = datos["correo"]   as? String
        val password = datos["password"] as? String
        val telefono = datos["telefono"] as? String

        if (correo.isNullOrBlank() || password.isNullOrBlank() ||
            (userType == "conductor" && telefono.isNullOrBlank())
        ) {
            view.showResult("Por favor completa todos los campos")
            return
        }

        // 2) Autenticamos con FirebaseAuth
        auth.signInWithEmailAndPassword(correo, password)
            .addOnSuccessListener {
                val uid = auth.currentUser!!.uid

                // 3) Verificamos estado en Realtime DB
                database.child(uid)
                    .get()
                    .addOnSuccessListener { snap ->
                        val estado = snap.child("estado").value as? String
                        if (estado != "verificado") {
                            view.showResult("Usuario no verificado")
                            return@addOnSuccessListener
                        }

                        // 4) Si es conductor, comprobamos teléfono
                        if (userType == "conductor") {
                            val dbTel = snap.child("telefono").value as? String
                            if (dbTel != telefono) {
                                view.showResult("Teléfono no coincide")
                                return@addOnSuccessListener
                            }
                        }

                        // 5) Todo OK → navegar a la pantalla principal correspondiente
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

    /** Lanza la Activity de pasajero o conductor y cierra la actual */
    private fun navigateToHome() {
        val ctx = view
        val intent = Intent(
            ctx,
            if (userType == "pasajero")
                MainPasajeroActivity::class.java
            else
                MainConductorActivity::class.java
        )
        ctx.startActivity(intent)
        ctx.finish()
    }
}
