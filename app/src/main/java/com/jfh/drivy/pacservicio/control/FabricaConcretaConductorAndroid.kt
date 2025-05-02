package com.jfh.drivy.pacservicio.control

import com.jfh.drivy.pacservicio.abstraction.FabricaAbstractaUsuario
import com.jfh.drivy.pacservicio.abstraction.FormularioAutenticacion
import com.jfh.drivy.pacservicio.abstraction.FormularioRegistro
import com.jfh.drivy.pacservicio.formulario.FormularioAutenticacionConductorAndroid
import com.jfh.drivy.pacservicio.formulario.FormularioRegistroConductorAndroid
import com.jfh.drivy.pacservicio.presentation.ServiceActivity

class FabricaConcretaConductorAndroid(
    private val activity: ServiceActivity
) : FabricaAbstractaUsuario {
    override fun crearFormularioRegistro(): FormularioRegistro =
        FormularioRegistroConductorAndroid(
            activity.binding.etCorreo,
            activity.binding.etTelefono,
            activity.binding.etPassword,
            activity.binding.etDireccion,
            activity.binding.etImagenCredencial,
            activity.binding.etImagenLicencia,
            activity.binding.etNombre,
            activity.binding.etNumeroLicencia
        )

    override fun crearFormularioAutenticacion(): FormularioAutenticacion =
        FormularioAutenticacionConductorAndroid(
            activity.binding.etCorreo,
            activity.binding.etPassword,
            activity.binding.etTelefono
        )
}
