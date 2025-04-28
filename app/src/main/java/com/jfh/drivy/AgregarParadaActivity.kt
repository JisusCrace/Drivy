package com.jfh.drivy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AgregarParadaActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private var diasSeleccionados: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_parada)

        // Inicializar la referencia a Firebase Realtime Database
        database = FirebaseDatabase.getInstance().reference

        // Referencias a los campos del formulario
        val editTextCalle = findViewById<EditText>(R.id.ingresar_calle)
        val spinnerHorario = findViewById<Spinner>(R.id.spinner_horario)
        val textViewDias = findViewById<TextView>(R.id.text_dias_actividad)
        val editTextReferencia = findViewById<EditText>(R.id.ingresar_referencia)
        val spinnerTolerancia = findViewById<Spinner>(R.id.spinner_tolerancia)
        val spinnerAlcaldia = findViewById<Spinner>(R.id.spinner_alcaldia)
        val spinnerVehiculo = findViewById<Spinner>(R.id.spinner_vehiculo)
        val botonRegistrarParada = findViewById<Button>(R.id.registrar_parada)
        val editTextLinkMaps = findViewById<EditText>(R.id.link_maps)
        val botonAbrirMaps = findViewById<Button>(R.id.boton_maps)

        // Configurar la flecha de regreso
        val flechaRegreso = findViewById<ImageView>(R.id.flecha_regreso)
        flechaRegreso.setOnClickListener { finish() }

        // Configurar opciones para el Spinner de Alcaldías
        val alcaldias = listOf(
            "Seleccione una alcaldía",
            "Álvaro Obregón", "Azcapotzalco", "Benito Juárez", "Coyoacán",
            "Cuajimalpa de Morelos", "Cuauhtémoc", "Gustavo A. Madero",
            "Iztacalco", "Iztapalapa", "Magdalena Contreras", "Miguel Hidalgo",
            "Milpa Alta", "Tláhuac", "Tlalpan", "Venustiano Carranza",
            "Xochimilco", "Otra"
        )
        val adapterAlcaldias = ArrayAdapter(this, android.R.layout.simple_spinner_item, alcaldias)
        adapterAlcaldias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerAlcaldia.adapter = adapterAlcaldias

        // Configurar opciones para el Spinner de Vehículos
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val uid = currentUser.uid
            val vehiculosRef = database.child("Vehiculos")

            vehiculosRef.get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val vehiculos = mutableListOf("Seleccione un vehículo")
                    snapshot.children.forEach { vehiculoSnapshot ->
                        val uidUsuario = vehiculoSnapshot.child("uidUsuario").value.toString()
                        if (uidUsuario == uid) {
                            val marca = vehiculoSnapshot.child("marca").value.toString()
                            vehiculos.add("$marca")
                        }
                    }
                    val adapterVehiculos = ArrayAdapter(this, android.R.layout.simple_spinner_item, vehiculos)
                    adapterVehiculos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerVehiculo.adapter = adapterVehiculos
                } else {
                    Toast.makeText(this, "No tienes vehículos registrados", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { error ->
                Toast.makeText(this, "Error al obtener los vehículos: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }

        // Configurar opciones para el Spinner de Horarios
        val horarios = (0..23).map { hora -> String.format("%02d:00", hora) }
        val adapterHorarios = ArrayAdapter(this, android.R.layout.simple_spinner_item, horarios)
        adapterHorarios.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerHorario.adapter = adapterHorarios

        // Configurar opciones para el Spinner de Tolerancia
        val tolerancias = listOf("5 minutos", "10 minutos", "15 minutos")
        val adapterTolerancias = ArrayAdapter(this, android.R.layout.simple_spinner_item, tolerancias)
        adapterTolerancias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTolerancia.adapter = adapterTolerancias

        // Configurar selección múltiple para días de actividad
        val diasSemana = arrayOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
        val diasSeleccionadosIndices = BooleanArray(diasSemana.size)
        textViewDias.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Selecciona los días de actividad")
            builder.setMultiChoiceItems(diasSemana, diasSeleccionadosIndices) { _, which, isChecked ->
                if (isChecked) {
                    diasSeleccionados.add(diasSemana[which])
                } else {
                    diasSeleccionados.remove(diasSemana[which])
                }
            }
            builder.setPositiveButton("OK") { _, _ ->
                textViewDias.text = diasSeleccionados.joinToString(", ")
            }
            builder.setNegativeButton("Cancelar", null)
            builder.show()
        }

        // Manejo del clic en el botón para abrir en Maps
        botonAbrirMaps.setOnClickListener {
            val linkMaps = editTextLinkMaps.text.toString().trim()
            val uri = if (linkMaps.isNotEmpty()) {
                Uri.parse(linkMaps)
            } else {
                Uri.parse("https://www.google.com/maps")
            }
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        // Manejo del clic en el botón para registrar la parada
        botonRegistrarParada.setOnClickListener {
            val calle = editTextCalle.text.toString().trim()
            val horario = spinnerHorario.selectedItem.toString()
            val dias = diasSeleccionados.joinToString(", ")
            val referencia = editTextReferencia.text.toString().trim()
            val tolerancia = spinnerTolerancia.selectedItem.toString()
            val alcaldia = spinnerAlcaldia.selectedItem.toString()
            val vehiculo = spinnerVehiculo.selectedItem?.toString()
            val linkMaps = editTextLinkMaps.text.toString().trim()

            if (calle.isNotEmpty() && horario.isNotEmpty() && dias.isNotEmpty() &&
                referencia.isNotEmpty() && tolerancia.isNotEmpty() &&
                alcaldia != "Seleccione una alcaldía" && vehiculo != "Seleccione un vehículo" &&
                linkMaps.isNotEmpty()
            ) {
                registrarParada(calle, horario, dias, referencia, tolerancia, alcaldia, vehiculo!!, linkMaps)
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos y selecciona opciones válidas", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registrarParada(
        calle: String,
        horario: String,
        dias: String,
        referencia: String,
        tolerancia: String,
        alcaldia: String,
        vehiculo: String,
        linkMaps: String
    ) {
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            val uid = currentUser.uid
            val userRef = database.child("Usuarios").child(uid)

            userRef.get().addOnSuccessListener { snapshot ->
                if (snapshot.exists() && snapshot.hasChild("Nombre")) {
                    val nombreUsuario = snapshot.child("Nombre").value.toString()

                    val paradaId = database.child("Paradas").push().key
                    if (paradaId != null) {
                        val paradaData = mapOf(
                            "id" to paradaId,
                            "calle" to calle,
                            "horario" to horario,
                            "dias" to dias,
                            "referencia" to referencia,
                            "tolerancia" to tolerancia,
                            "alcaldia" to alcaldia,
                            "vehiculo" to vehiculo,
                            "linkMaps" to linkMaps,
                            "agregadoPor" to nombreUsuario,
                            "uidUsuario" to uid
                        )

                        database.child("Paradas").child(paradaId).setValue(paradaData)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Parada registrada correctamente", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                            .addOnFailureListener { error ->
                                Toast.makeText(this, "Error al registrar la parada: ${error.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Toast.makeText(this, "No se pudo obtener el nombre del usuario", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { error ->
                Toast.makeText(this, "Error al obtener los datos del usuario: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
        }
    }
}