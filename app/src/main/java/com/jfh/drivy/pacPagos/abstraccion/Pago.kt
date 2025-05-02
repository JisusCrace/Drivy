package com.jfh.drivy.pacPagos.abstraccion

class Pago(private val metodo: InterfazMetodoPago) : InterfazPago {
    override fun realizarPago(): String = metodo.completarPago()
}
