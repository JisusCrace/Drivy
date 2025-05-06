package com.example.pacperfil.control

import android.content.Context
import com.example.pacperfil.abstraccion.GestorPerfil
import com.example.pacperfil.abstraccion.Perfil

class ControladorPerfil(private val context: Context) {

    // Esta función se invoca desde PerfilActivity
    fun obtenerPerfil(idUsuario: String, context: Context): Perfil? {
        val gestor = GestorPerfil(context)
        return gestor.obtenerPerfil(idUsuario)
    }
}



