package com.jfh.drivy.pacservicio.control

import com.jfh.drivy.pacservicio.abstraction.FabricaAbstractaUsuario
import com.jfh.drivy.pacservicio.abstraction.FormularioRegistro
import com.jfh.drivy.pacservicio.abstraction.FormularioAutenticacion
import com.jfh.drivy.pacservicio.formulario.FormularioRegistroPasajeroAndroid
import com.jfh.drivy.pacservicio.formulario.FormularioAutenticacionPasajeroAndroid
import com.jfh.drivy.pacservicio.presentation.ServiceActivity

class FabricaConcretaPasajeroAndroid(
    private val activity: ServiceActivity
) : FabricaAbstractaUsuario {

    override fun crearFormularioRegistro(): FormularioRegistro =
        FormularioRegistroPasajeroAndroid(
            activity.binding.etMatricula,
            activity.binding.etNombre,
            activity.binding.etCorreo,
            activity.binding.etPassword,
            activity.binding.etTelefono
        )

    override fun crearFormularioAutenticacion(): FormularioAutenticacion =
        FormularioAutenticacionPasajeroAndroid(
            activity.binding.etMatricula,
            activity.binding.etPassword
        )
}
