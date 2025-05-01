package com.jfh.drivy.pacservicio.formulario

import android.widget.EditText
import com.jfh.drivy.pacservicio.abstraction.FormularioRegistro

class FormularioRegistroPasajeroAndroid(
    private val etCorreo:           EditText,
    private val etTelefono:         EditText,
    private val etPassword:         EditText,
    private val etDireccion:        EditText,
    private val etImagenCredencial: EditText,
    private val etNombre:           EditText
) : FormularioRegistro {
    override fun obtenerDatos(): Map<String, Any> = linkedMapOf(
        "correo"           to etCorreo.text.toString(),
        "telefono"         to etTelefono.text.toString(),
        "password"         to etPassword.text.toString(),
        "direccion"        to etDireccion.text.toString(),
        "estado"           to "verificado",
        "imagenCredencial" to etImagenCredencial.text.toString(),
        "nombre"           to etNombre.text.toString(),
        "rol"              to "pasajero"
    )
}
