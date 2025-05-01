package com.jfh.drivy.pacservicio.formulario

import android.widget.EditText
import com.jfh.drivy.pacservicio.abstraction.FormularioRegistro

class FormularioRegistroPasajeroAndroid(
    private val etMatricula: EditText,
    private val etNombre:    EditText,
    private val etCorreo:    EditText,
    private val etPassword:  EditText,
    private val etTelefono:  EditText
) : FormularioRegistro {
    override fun obtenerDatos() = mapOf(
        "matricula" to etMatricula.text.toString(),
        "nombre"    to etNombre.text.toString(),
        "correo"    to etCorreo.text.toString(),
        "password"  to etPassword.text.toString(),
        "telefono"  to etTelefono.text.toString()
    )
}
