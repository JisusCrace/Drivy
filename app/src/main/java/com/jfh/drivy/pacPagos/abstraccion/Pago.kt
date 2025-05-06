package com.jfh.drivy.pacPagos.abstraccion

class Pago(private val metodo: InterfazMetodoPago) : InterfazPago {
    override fun realizarPago(cantidad: Double): String {
        return metodo.procesarPago(cantidad)
    }
}
