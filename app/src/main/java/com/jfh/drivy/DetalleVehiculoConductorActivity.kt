package com.jfh.drivy

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DetalleVehiculoConductorActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_vehiculo_conductor)

        // Inicializar referencia de la base de datos
        database = FirebaseDatabase.getInstance().reference

        // Obtener el ID del vehículo desde el intent
        val vehiculoId = intent.getStringExtra("vehiculoId")

        if (vehiculoId != null) {
            cargarDetallesVehiculo(vehiculoId)
        } else {
            Toast.makeText(this, "ID del vehículo no proporcionado", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Listener para la flecha de regreso
        findViewById<ImageView>(R.id.flecha_regreso).setOnClickListener {
            finish()
        }
    }

    private fun cargarDetallesVehiculo(vehiculoId: String) {
        database.child("Vehiculos").child(vehiculoId).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val marca = snapshot.child("marca").value.toString()
                    val modelo = snapshot.child("modelo").value.toString()
                    val color = snapshot.child("color").value.toString()
                    val numeroSerie = snapshot.child("numeroSerie").value.toString()
                    val placa = snapshot.child("placa").value.toString()
                    val tarjetaBase64 = snapshot.child("tarjetaBase64").value.toString()

                    // Asignar datos a TextViews
                    findViewById<TextView>(R.id.text_marca).text = "Marca: $marca"
                    findViewById<TextView>(R.id.text_modelo).text = "Modelo: $modelo"
                    findViewById<TextView>(R.id.text_color).text = "Color: $color"
                    findViewById<TextView>(R.id.text_numero_serie).text = "Número de Serie: $numeroSerie"
                    findViewById<TextView>(R.id.text_placa).text = "Placa: $placa"

                    // Convertir Base64 a Bitmap y mostrar en ImageView
                    if (tarjetaBase64.isNotEmpty()) {
                        val tarjetaBitmap = base64ToBitmap(tarjetaBase64)
                        if (tarjetaBitmap != null) {
                            findViewById<ImageView>(R.id.tarjeta_image_view).setImageBitmap(tarjetaBitmap)
                        } else {
                            Toast.makeText(this, "No se pudo cargar la imagen de la tarjeta", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "No hay imagen de la tarjeta disponible", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "El vehículo no existe en la base de datos", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, "Error al cargar los detalles del vehículo: ${error.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Función para convertir Base64 a Bitmap
    private fun base64ToBitmap(base64String: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            Log.e("DetalleVehiculo", "Error al convertir Base64 a Bitmap: ${e.message}")
            null
        }
    }
}
