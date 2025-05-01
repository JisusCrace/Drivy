package com.jfh.drivy.pacservicio.formulario

import android.widget.EditText
import com.jfh.drivy.pacservicio.abstraction.FormularioAutenticacion

class FormularioAutenticacionConductorAndroid(
    private val etMatricula: EditText,
    private val etPassword:  EditText,
    private val etLicencia:  EditText
) : FormularioAutenticacion {
    override fun obtenerDatos() = mapOf(
        "matricula" to etMatricula.text.toString(),
        "password"  to etPassword.text.toString(),
        "licencia"  to etLicencia.text.toString()
    )
}
