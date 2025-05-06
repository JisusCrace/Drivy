package com.jfh.drivy.pacPagos.control

import com.jfh.drivy.pacPagos.abstraccion.InterfazPago

class GestorReserva {
    fun procesarPago(pago: InterfazPago, cantidad: Double): String {
        return pago.realizarPago(cantidad)
    }
}
