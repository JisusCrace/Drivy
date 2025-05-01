package com.jfh.drivy.pacservicio.formulario

import android.widget.EditText
import com.jfh.drivy.pacservicio.abstraction.FormularioRegistro

class FormularioRegistroConductorAndroid(
    private val etMatricula:    EditText,
    private val etNombre:       EditText,
    private val etCorreo:       EditText,
    private val etPassword:     EditText,
    private val etTelefono:     EditText,
    private val etCredencial:   EditText,
    private val etDireccion:    EditText,
    private val etLicencia:     EditText,
    private val etFotoLicencia: EditText
) : FormularioRegistro {
    override fun obtenerDatos() = mapOf(
        "matricula"    to etMatricula.text.toString(),
        "nombre"       to etNombre.text.toString(),
        "correo"       to etCorreo.text.toString(),
        "password"     to etPassword.text.toString(),
        "telefono"     to etTelefono.text.toString(),
        "credencial"   to etCredencial.text.toString(),
        "direccion"    to etDireccion.text.toString(),
        "licencia"     to etLicencia.text.toString(),
        "fotoLicencia" to etFotoLicencia.text.toString()
    )
}
