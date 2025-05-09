package com.jfh.drivy.pacservicio.control

import com.jfh.drivy.pacservicio.abstraction.FabricaAbstractaUsuario
import com.jfh.drivy.pacservicio.abstraction.FormularioAutenticacion
import com.jfh.drivy.pacservicio.abstraction.FormularioRegistro
import com.jfh.drivy.pacservicio.formulario.FormularioAutenticacionPasajeroAndroid
import com.jfh.drivy.pacservicio.formulario.FormularioRegistroPasajeroAndroid
import com.jfh.drivy.pacservicio.presentation.ServiceActivity

class FabricaConcretaPasajeroAndroid(
    private val activity: ServiceActivity
) : FabricaAbstractaUsuario {
    override fun crearFormularioRegistro(): FormularioRegistro =
        FormularioRegistroPasajeroAndroid(
            activity.binding.etCorreo,
            activity.binding.etTelefono,
            activity.binding.etPassword,
            activity.binding.etDireccion,
            activity.binding.etImagenCredencial,
            activity.binding.etNombre
        )

    override fun crearFormularioAutenticacion(): FormularioAutenticacion =
        FormularioAutenticacionPasajeroAndroid(
            activity.binding.etCorreo,
            activity.binding.etPassword
        )
}
