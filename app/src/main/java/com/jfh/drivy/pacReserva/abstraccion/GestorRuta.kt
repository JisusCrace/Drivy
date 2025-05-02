package com.jfh.drivy.gestores

import com.jfh.drivy.modelos.Ruta

class GestorRuta {

    fun obtenerRutaPorId(paradaId: String, callback: (Ruta.DatosRuta?) -> Unit) {
        val ruta = Ruta()
        ruta.obtenerDatosParada(paradaId) { datosRuta ->
            callback(datosRuta)
        }
    }
}
