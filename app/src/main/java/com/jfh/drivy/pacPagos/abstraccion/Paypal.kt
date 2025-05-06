package com.jfh.drivy.pacPagos.abstraccion

class Paypal : InterfazMetodoPago {
    override fun procesarPago(cantidad: Double): String {
        return "✅ Pago de $$cantidad confirmado con PayPal"
    }
}
