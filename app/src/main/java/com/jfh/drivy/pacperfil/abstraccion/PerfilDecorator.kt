package com.example.pacperfil.abstraccion

abstract class PerfilDecorator(protected val perfil: Perfil) : Perfil {
    override val id: String
        get() = perfil.id

    override val nombre: String
        get() = perfil.nombre

    override val rol: String
        get() = perfil.rol
}




