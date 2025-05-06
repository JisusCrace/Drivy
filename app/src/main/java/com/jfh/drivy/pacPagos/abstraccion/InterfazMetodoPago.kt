package com.jfh.drivy.pacPagos.abstraccion

interface InterfazMetodoPago {
    fun procesarPago(cantidad: Double): String
}
