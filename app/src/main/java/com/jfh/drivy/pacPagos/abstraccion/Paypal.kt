package com.jfh.drivy.pacPagos.abstraccion

class Paypal : InterfazMetodoPago {
    override fun completarPago() = "🔐 Transacción segura realizada con PayPal. ¡Gracias por su confianza!"
}
