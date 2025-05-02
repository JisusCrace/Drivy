package com.jfh.drivy.pacservicio.formulario

import android.widget.EditText
import com.jfh.drivy.pacservicio.abstraction.FormularioAutenticacion

class FormularioAutenticacionPasajeroAndroid(
    private val etCorreo:   EditText,
    private val etPassword: EditText
) : FormularioAutenticacion {
    override fun obtenerDatos(): Map<String, String> = mapOf(
        "correo"   to etCorreo.text.toString(),
        "password" to etPassword.text.toString()
    )
}
