package com.jfh.drivy.modelos

class Reserva() {

    var id: String = ""
    var alcaldia: String = ""
    var dia: String = ""
    var hora: String = ""
    var latitud: Double = 0.0
    var longitud: Double = 0.0
    var nombreConductor: String = ""
    var numeroLicencia: String = ""
    var telefonoConductor: String = ""
    var nombrePasajero: String = ""
    var telefonoPasajero: String = ""
    var codigoVerificacion: String = ""

    constructor(
        id: String,
        alcaldia: String,
        dia: String,
        hora: String,
        latitud: Double,
        longitud: Double,
        nombreConductor: String,
        numeroLicencia: String,
        telefonoConductor: String,
        nombrePasajero: String,
        telefonoPasajero: String,
        codigoVerificacion: String
    ) : this() {
        this.id = id
        this.alcaldia = alcaldia
        this.dia = dia
        this.hora = hora
        this.latitud = latitud
        this.longitud = longitud
        this.nombreConductor = nombreConductor
        this.numeroLicencia = numeroLicencia
        this.telefonoConductor = telefonoConductor
        this.nombrePasajero = nombrePasajero
        this.telefonoPasajero = telefonoPasajero
        this.codigoVerificacion = codigoVerificacion
    }
}
