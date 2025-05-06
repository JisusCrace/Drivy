package com.example.pacperfil.abstraccion

class PerfilBase(
    override val id: String,
    override val nombre: String,
    override val rol: String,
    val ultimaReserva: String = "",
    val reservas: List<String> = emptyList(),
    val ultimaParada: String = "",
    val vehiculos: List<String> = emptyList()
) : Perfil




