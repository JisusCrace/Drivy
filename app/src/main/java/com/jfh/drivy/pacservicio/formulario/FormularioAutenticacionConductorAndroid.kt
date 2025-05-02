package com.jfh.drivy.pacservicio.formulario

import android.widget.EditText
import com.jfh.drivy.pacservicio.abstraction.FormularioAutenticacion

class FormularioAutenticacionConductorAndroid(
    private val etCorreo:   EditText,
    private val etPassword: EditText,
    private val etTelefono: EditText
) : FormularioAutenticacion {
    override fun obtenerDatos(): Map<String, String> = mapOf(
        "correo"   to etCorreo.text.toString(),
        "password" to etPassword.text.toString(),
        "telefono" to etTelefono.text.toString()
    )
}
