package com.example.pacperfil.abstraccion

class PerfilPasajeroDecorator(
    private val perfil: Perfil,
    private val ultimaReserva: String,
    private val listaReservas: List<String>
) : Perfil {

    override val id: String
        get() = perfil.id

    override val nombre: String
        get() = perfil.nombre

    override val rol: String
        get() = "Pasajero"

    fun getUltimaReserva(): String = ultimaReserva

    fun getListaReservas(): List<String> = listaReservas
}
