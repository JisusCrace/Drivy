package com.jfh.drivy.pacservicio.formulario

import android.widget.EditText
import com.jfh.drivy.pacservicio.abstraction.FormularioRegistro

class FormularioRegistroPasajeroAndroid(
    private val etCorreo:           EditText,
    private val etDireccion:        EditText,
    private val etImagenCredencial: EditText,
    private val etNombre:           EditText,
    private val etTelefono:         EditText
) : FormularioRegistro {
    override fun obtenerDatos(): Map<String, String> = mapOf(
        "correo"           to etCorreo.text.toString(),
        "direccion"        to etDireccion.text.toString(),
        "estado"           to "verificado",
        "imagenCredencial" to etImagenCredencial.text.toString(),
        "nombre"           to etNombre.text.toString(),
        "rol"              to "pasajero",
        "telefono"         to etTelefono.text.toString()
    )
}
