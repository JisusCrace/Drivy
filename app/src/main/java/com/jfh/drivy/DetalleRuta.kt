package com.jfh.drivy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.*

class DetalleRuta : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_ruta)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val flechaRegreso = findViewById<ImageView>(R.id.flecha_regreso)
        flechaRegreso.setOnClickListener {
            finish()
        }

        database = FirebaseDatabase.getInstance().reference

        val origenPantalla = intent.getStringExtra("origenPantalla") ?: ""
        val paradaId = intent.getStringExtra("paradaId")

        if (paradaId != null) {
            cargarDetallesParada(paradaId)
            configurarBotonMapa(paradaId)
            if (origenPantalla == "VerRutasPasajero") {
                // Lógica adicional específica para VerRutasPasajero
                Toast.makeText(this, "Cargando detalles de parada", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "No se pudo obtener la información de la parada", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun cargarDetallesParada(paradaId: String) {
        val paradaRef = database.child("Paradas").child(paradaId)
        paradaRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val calle = snapshot.child("calle").value?.toString() ?: "Desconocida"
                val horario = snapshot.child("horario").value?.toString() ?: "No especificado"
                val referencia = snapshot.child("referencia").value?.toString() ?: "No disponible"
                val agregadoPor = snapshot.child("agregadoPor").value?.toString() ?: "Anónimo"
                val vehiculoSeleccionado = snapshot.child("vehiculo").value?.toString() ?: "Sin vehículo"
                val uidUsuario = snapshot.child("uidUsuario").value?.toString() ?: ""

                findViewById<TextView>(R.id.text_calle).text = "Calle: $calle"
                findViewById<TextView>(R.id.text_horario).text = "Horario: $horario"
                findViewById<TextView>(R.id.text_referencia).text = "Referencia: $referencia"
                findViewById<TextView>(R.id.text_conductor).text = "Conductor: $agregadoPor"

                cargarDetallesVehiculo(uidUsuario, vehiculoSeleccionado)
            } else {
                Toast.makeText(this, "La parada no existe", Toast.LENGTH_SHORT).show()
                finish()
            }
        }.addOnFailureListener { error ->
            Toast.makeText(this, "Error al cargar los datos: ${error.message}", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun cargarDetallesVehiculo(uidUsuario: String, vehiculoSeleccionado: String) {
        val vehiculosRef = database.child("Vehiculos")
        vehiculosRef.orderByChild("uidUsuario").equalTo(uidUsuario)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (vehiculoSnapshot in snapshot.children) {
                            val marca = vehiculoSnapshot.child("marca").value?.toString() ?: ""
                            if (marca == vehiculoSeleccionado) {
                                val modelo = vehiculoSnapshot.child("modelo").value?.toString() ?: "Desconocido"
                                val color = vehiculoSnapshot.child("color").value?.toString() ?: "Desconocido"
                                val placa = vehiculoSnapshot.child("placa").value?.toString() ?: "Desconocida"

                                findViewById<TextView>(R.id.text_marca_auto).text = "Marca de auto: $marca"
                                findViewById<TextView>(R.id.text_modelo_auto).text = "Modelo de auto: $modelo"
                                findViewById<TextView>(R.id.text_color).text = "Color: $color"
                                findViewById<TextView>(R.id.text_placa).text = "Placa: $placa"
                                break
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@DetalleRuta, "Error al cargar datos del vehículo: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun configurarBotonMapa(paradaId: String) {
        val botonMapa = findViewById<ImageView>(R.id.btn_open_maps)
        val paradaRef = database.child("Paradas").child(paradaId)

        botonMapa.setOnClickListener {
            paradaRef.child("linkMaps").get().addOnSuccessListener { snapshot ->
                val linkMaps = snapshot.value?.toString()
                if (!linkMaps.isNullOrEmpty()) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkMaps))
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "No hay un enlace de mapa disponible", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Error al obtener el enlace del mapa", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
