package com.jfh.drivy.datos

import com.google.firebase.database.*

class DatosConductor {

    data class Datos(
        val nombre: String,
        val numeroLicencia: String,
        val telefono: String
    )

    interface CallbackDatosConductor {
        fun onDatosObtenidos(datos: Datos)
        fun onError(error: String)
    }

    fun obtenerDatosConductorPorIdRuta(paradaId: String, callback: CallbackDatosConductor) {
        val database = FirebaseDatabase.getInstance().reference
        val paradaRef = database.child("Paradas").child(paradaId)

        paradaRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val uidUsuario = snapshot.child("uidUsuario").value.toString()
                val usuarioRef = database.child("Usuarios").child(uidUsuario)

                usuarioRef.get().addOnSuccessListener { userSnap ->
                    if (userSnap.exists()) {
                        val nombre = userSnap.child("Nombre").value.toString()
                        val numeroLicencia = userSnap.child("NumeroLicencia").value.toString()
                        val telefono = userSnap.child("Telefono").value.toString()

                        val datos = Datos(nombre, numeroLicencia, telefono)
                        callback.onDatosObtenidos(datos)
                    } else {
                        callback.onError("No se encontró el usuario.")
                    }
                }.addOnFailureListener {
                    callback.onError("Error al obtener datos del usuario: ${it.message}")
                }
            } else {
                callback.onError("No se encontró la parada.")
            }
        }.addOnFailureListener {
            callback.onError("Error al obtener parada: ${it.message}")
        }
    }
}
