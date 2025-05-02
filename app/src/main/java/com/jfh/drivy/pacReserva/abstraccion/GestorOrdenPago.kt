package com.jfh.drivy.pacReserva.abstraccion

import com.jfh.drivy.gestores.GestorReserva

class GestorOrdenPago {

    fun confirmarPagoYCrearReserva(paradaId: String) {
        // Puedes registrar la orden si lo deseas (a Firebase, por ejemplo)
        val orden = OrdenPago(paradaId, true, System.currentTimeMillis())

        // Lógica para generar la reserva
        val gestorReserva = GestorReserva()
        gestorReserva.realizarReserva(paradaId)
    }
}
