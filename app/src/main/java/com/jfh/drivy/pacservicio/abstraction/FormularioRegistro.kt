package com.jfh.drivy.pacservicio.abstraction

/** Devuelve Map<String,Any> para admitir Boolean y String */
interface FormularioRegistro {
    fun obtenerDatos(): Map<String, Any>
}
