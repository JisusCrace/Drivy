package com.jfh.drivy.modelos

import com.google.firebase.database.*

class Ruta {

    data class DatosRuta(
        val alcaldia: String,
        val dias: String,
        val horario: String,
        val latitud: Double,
        val longitud: Double
    )

    fun obtenerDatosParada(paradaId: String, callback: (DatosRuta?) -> Unit) {
        val ref = FirebaseDatabase.getInstance().getReference("Paradas").child(paradaId)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val alcaldia = snapshot.child("alcaldia").getValue(String::class.java) ?: return callback(null)
                    val dias = snapshot.child("dias").getValue(String::class.java) ?: return callback(null)
                    val horario = snapshot.child("horario").getValue(String::class.java) ?: return callback(null)
                    val latitud = snapshot.child("latitud").getValue(Double::class.java) ?: return callback(null)
                    val longitud = snapshot.child("longitud").getValue(Double::class.java) ?: return callback(null)

                    callback(DatosRuta(alcaldia, dias, horario, latitud, longitud))
                } else {
                    callback(null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null)
            }
        })
    }
}
