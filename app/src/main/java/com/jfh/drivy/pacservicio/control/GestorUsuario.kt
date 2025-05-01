package com.jfh.drivy.pacservicio.control

class GestorUsuario {
    fun registrarUsuario(datos: Map<String, String>): Boolean {
        println("Registrando: $datos")
        return true
    }

    fun autenticarUsuario(datos: Map<String, String>): Boolean {
        println("Autenticando: $datos")
        return true
    }
}
