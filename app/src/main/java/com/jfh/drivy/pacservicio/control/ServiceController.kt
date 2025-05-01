package com.jfh.drivy.pacservicio.control

import com.jfh.drivy.pacservicio.abstraction.FabricaAbstractaUsuario
import com.jfh.drivy.pacservicio.presentation.ServiceActivity

class ServiceController(
    private val factory: FabricaAbstractaUsuario,
    private val view: ServiceActivity
) {
    /** Lanza el registro usando la fábrica para obtener el formulario apropiado */
    fun register() {
        val datos   = factory.crearFormularioRegistro().obtenerDatos()
        val success = GestorUsuario().registrarUsuario(datos)
        view.showResult(if (success) "Registro exitoso" else "Error al registrar")
    }

    /** Lanza la autenticación usando la fábrica para obtener el formulario apropiado */
    fun login() {
        val datos   = factory.crearFormularioAutenticacion().obtenerDatos()
        val success = GestorUsuario().autenticarUsuario(datos)
        view.showResult(if (success) "Login exitoso" else "Credenciales inválidas")
    }
}
