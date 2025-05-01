package com.jfh.drivy.pacservicio.formulario

import android.widget.EditText
import com.jfh.drivy.pacservicio.abstraction.FormularioAutenticacion

class FormularioAutenticacionPasajeroAndroid(
    private val etMatricula: EditText,
    private val etPassword:  EditText
) : FormularioAutenticacion {
    override fun obtenerDatos() = mapOf(
        "matricula" to etMatricula.text.toString(),
        "password"  to etPassword.text.toString()
    )
}
