package com.example.pacperfil.abstraccion

class PerfilConductorDecorator(
    private val perfil: Perfil,
    private val ultimaParada: String,
    private val listaVehiculos: List<String>
) : Perfil {

    override val id: String
        get() = perfil.id

    override val nombre: String
        get() = perfil.nombre

    override val rol: String
        get() = "Conductor"

    fun getUltimaParada(): String = ultimaParada

    fun getListaVehiculos(): List<String> = listaVehiculos
}
