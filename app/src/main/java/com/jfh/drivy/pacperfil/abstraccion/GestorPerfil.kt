package com.example.pacperfil.abstraccion

import android.content.Context

class GestorPerfil(private val context: Context) {

    fun obtenerPerfil(idUsuario: String): Perfil? {
        return when (idUsuario) {
            "1" -> {
                // Base
                val perfilBase = PerfilBase("1", "Alejandra López", "Pasajero")
                // Datos simulados para pasajero
                val ultimaReserva = "Reserva a Santa Fe"
                val listaReservas = listOf("Reserva a Santa Fe", "Reserva a Polanco", "Reserva a Coyoacán")
                // Decorar
                PerfilPasajeroDecorator(perfilBase, ultimaReserva, listaReservas)
            }

            "2" -> {
                val perfilBase = PerfilBase("2", "Jesus Ramirez", "Conductor")
                // Datos simulados para conductor
                val ultimaParada = "Parada en Cuajimalpa"
                val listaVehiculos = listOf("Dodge", "Chevrolet Aveo")
                PerfilConductorDecorator(perfilBase, ultimaParada, listaVehiculos)
            }

            else -> {
                // Retorna un perfil base vacío o lanza una excepción
                PerfilBase(
                    id = idUsuario,
                    nombre = "Desconocido",
                    rol = "desconocido"
                )
            }
        }
    }
}

