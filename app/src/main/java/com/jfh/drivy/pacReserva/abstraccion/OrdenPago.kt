package com.jfh.drivy.pacReserva.abstraccion

data class OrdenPago(
    val paradaId: String,
    val pagado: Boolean,
    val timestamp: Long
)
