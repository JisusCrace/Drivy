package com.jfh.drivy.pacPagos.abstraccion

class MercadoPago : InterfazMetodoPago {
    override fun procesarPago(cantidad: Double): String {
        return "✅ Pago de $$cantidad confirmado con MercadoPago"
    }
}
