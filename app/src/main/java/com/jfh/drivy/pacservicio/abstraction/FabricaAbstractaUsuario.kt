package com.jfh.drivy.pacservicio.abstraction

interface FabricaAbstractaUsuario {
    fun crearFormularioRegistro(): FormularioRegistro
    fun crearFormularioAutenticacion(): FormularioAutenticacion
}
