package com.jfh.drivy.pacservicio.formulario

import android.widget.EditText
import com.jfh.drivy.pacservicio.abstraction.FormularioRegistro

class FormularioRegistroConductorAndroid(
    private val etCorreo:            EditText,
    private val etDireccion:         EditText,
    private val etImagenCredencial:  EditText,
    private val etImagenLicencia:    EditText,
    private val etNombre:            EditText,
    private val etNumeroLicencia:    EditText,
    private val etTelefono:          EditText
) : FormularioRegistro {
    override fun obtenerDatos(): Map<String, String> = mapOf(
        "correo"            to etCorreo.text.toString(),
        "direccion"         to etDireccion.text.toString(),
        "estado"            to "verificado",
        "imagenCredencial"  to etImagenCredencial.text.toString(),
        "imagenLicencia"    to etImagenLicencia.text.toString(),
        "nombre"            to etNombre.text.toString(),
        "numeroLicencia"    to etNumeroLicencia.text.toString(),
        "rol"               to "conductor",
        "telefono"          to etTelefono.text.toString(),
        "activo"            to "true",
        "tieneAdvertencia"  to "false"
    )
}
