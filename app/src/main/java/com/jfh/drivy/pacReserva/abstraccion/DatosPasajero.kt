package com.jfh.drivy.datos

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class DatosPasajero {

    data class Datos(
        val nombre: String,
        val telefono: String
    )

    interface CallbackDatosPasajero {
        fun onDatosObtenidos(datos: Datos)
        fun onError(error: String)
    }

    fun obtenerDatosPasajeroActual(callback: CallbackDatosPasajero) {
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            val uid = currentUser.uid
            val ref = FirebaseDatabase.getInstance().reference.child("Usuarios").child(uid)

            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val nombre = snapshot.child("Nombre").value?.toString() ?: ""
                        val telefono = snapshot.child("Telefono").value?.toString() ?: ""
                        callback.onDatosObtenidos(Datos(nombre, telefono))
                    } else {
                        callback.onError("El usuario no tiene datos registrados.")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    callback.onError("Error al acceder a la base de datos: ${error.message}")
                }
            })
        } else {
            callback.onError("No hay sesión activa.")
        }
    }
}
