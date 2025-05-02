package com.jfh.drivy.gestores

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.jfh.drivy.datos.DatosConductor
import com.jfh.drivy.datos.DatosPasajero
import com.jfh.drivy.modelos.Reserva
import com.jfh.drivy.modelos.Ruta

class GestorReserva {

    fun realizarReserva(paradaId: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser == null) {
            Log.e("GestorReserva", "❌ Usuario no autenticado. No se puede registrar la reserva.")
            return
        }

        val gestorRuta = GestorRuta()
        val gestorDatos = GestorDatos()

        gestorRuta.obtenerRutaPorId(paradaId) { datosRuta ->

            if (datosRuta != null) {
                gestorDatos.obtenerDatosConductor(paradaId, object : GestorDatos.CallbackConductor {
                    override fun onDatosConductorRecibidos(nombre: String, licencia: String, telefono: String) {
                        val datosConductor = DatosConductor.Datos(nombre, licencia, telefono)

                        gestorDatos.obtenerDatosPasajero(object : GestorDatos.CallbackPasajero {
                            override fun onDatosPasajeroRecibidos(nombre: String, telefono: String) {
                                val datosPasajero = DatosPasajero.Datos(nombre, telefono)

                                gestorDatos.generarCodigoVerificacion(object : GestorDatos.CallbackCodigo {
                                    override fun onCodigoGenerado(codigo: String) {

                                        val reservaId = FirebaseDatabase.getInstance().reference
                                            .child("Reservas").push().key ?: return

                                        val reserva = Reserva(
                                            id = reservaId,
                                            alcaldia = datosRuta.alcaldia,
                                            dia = datosRuta.dias,
                                            hora = datosRuta.horario,
                                            latitud = datosRuta.latitud,
                                            longitud = datosRuta.longitud,
                                            nombreConductor = datosConductor.nombre,
                                            numeroLicencia = datosConductor.numeroLicencia,
                                            telefonoConductor = datosConductor.telefono,
                                            nombrePasajero = datosPasajero.nombre,
                                            telefonoPasajero = datosPasajero.telefono,
                                            codigoVerificacion = codigo
                                        )

                                        subirReservaAFirebase(reserva, currentUser.uid)
                                    }
                                })
                            }

                            override fun onErrorPasajero(mensaje: String) {
                                Log.e("GestorReserva", "❌ Error al obtener datos del pasajero: $mensaje")
                            }
                        })
                    }

                    override fun onErrorConductor(mensaje: String) {
                        Log.e("GestorReserva", "❌ Error al obtener datos del conductor: $mensaje")
                    }
                })
            } else {
                Log.e("GestorReserva", "❌ No se encontró la parada con ID: $paradaId")
            }
        }
    }

    private fun subirReservaAFirebase(reserva: Reserva, uid: String) {
        val database = FirebaseDatabase.getInstance().reference
        val ref = database.child("Reservaciones").child(uid).child(reserva.id)

        ref.setValue(reserva)
            .addOnSuccessListener {
                Log.d("GestorReserva", "✅ Reserva registrada correctamente en Firebase")
            }
            .addOnFailureListener { error ->
                Log.e("GestorReserva", "❌ Error al registrar reserva: ${error.message}")
            }
    }
}
