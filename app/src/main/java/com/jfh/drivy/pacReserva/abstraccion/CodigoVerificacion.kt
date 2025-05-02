package com.jfh.drivy.utilidades

import kotlin.random.Random

class CodigoVerificacion {

    fun generarCodigo(): String {
        val codigo = StringBuilder()
        repeat(3) {
            val digito = Random.nextInt(0, 10) // Dígitos del 0 al 9
            codigo.append(digito)
        }
        return codigo.toString()
    }
}
