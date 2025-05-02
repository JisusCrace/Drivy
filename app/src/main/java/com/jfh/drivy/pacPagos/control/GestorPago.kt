package com.jfh.drivy.pacPagos.control

import com.jfh.drivy.pacPagos.abstraccion.InterfazPago

class GestorPago {
    fun procesarPago(pago: InterfazPago): String {
        val resultado = pago.realizarPago()
        return "🚌 Reserva confirmada.\n$resultado"
    }
}
