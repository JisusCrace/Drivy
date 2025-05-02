package com.jfh.drivy.gestores

import com.jfh.drivy.datos.DatosConductor
import com.jfh.drivy.datos.DatosPasajero
import com.jfh.drivy.utilidades.CodigoVerificacion

class GestorDatos {

    interface CallbackConductor {
        fun onDatosConductorRecibidos(nombre: String, licencia: String, telefono: String)
        fun onErrorConductor(mensaje: String)
    }

    interface CallbackPasajero {
        fun onDatosPasajeroRecibidos(nombre: String, telefono: String)
        fun onErrorPasajero(mensaje: String)
    }

    interface CallbackCodigo {
        fun onCodigoGenerado(codigo: String)
    }

    fun obtenerDatosConductor(paradaId: String, callback: CallbackConductor) {
        val datosConductor = DatosConductor()
        datosConductor.obtenerDatosConductorPorIdRuta(paradaId, object : DatosConductor.CallbackDatosConductor {
            override fun onDatosObtenidos(datos: DatosConductor.Datos) {
                callback.onDatosConductorRecibidos(datos.nombre, datos.numeroLicencia, datos.telefono)
            }

            override fun onError(error: String) {
                callback.onErrorConductor(error)
            }
        })
    }

    fun obtenerDatosPasajero(callback: CallbackPasajero) {
        val datosPasajero = DatosPasajero()
        datosPasajero.obtenerDatosPasajeroActual(object : DatosPasajero.CallbackDatosPasajero {
            override fun onDatosObtenidos(datos: DatosPasajero.Datos) {
                callback.onDatosPasajeroRecibidos(datos.nombre, datos.telefono)
            }

            override fun onError(error: String) {
                callback.onErrorPasajero(error)
            }
        })
    }

    fun generarCodigoVerificacion(callback: CallbackCodigo) {
        val generador = CodigoVerificacion()
        val codigo = generador.generarCodigo()
        callback.onCodigoGenerado(codigo)
    }
}
