package com.jfh.drivy

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream
import java.io.InputStream

class AgregarVehiculoActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var tarjetaImageView: ImageView
    private var tarjetaBitmap: Bitmap? = null
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_vehiculo)

        database = FirebaseDatabase.getInstance().reference

        // Inicializar el selector de imágenes
        pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val imageUri = result.data!!.data
                tarjetaBitmap = uriToBitmap(imageUri) // Convertir URI a Bitmap
                tarjetaImageView.setImageBitmap(tarjetaBitmap) // Mostrar la imagen seleccionada
            } else {
                Toast.makeText(this, "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show()
            }
        }

        // Referencias a los campos del formulario
        val editTextMarca = findViewById<EditText>(R.id.ingresar_marca)
        val editTextModelo = findViewById<EditText>(R.id.ingresar_modelo)
        val editTextPlaca = findViewById<EditText>(R.id.ingresar_placa)
        val editTextColor = findViewById<EditText>(R.id.ingresar_color)
        val editTextNumeroSerie = findViewById<EditText>(R.id.ingresar_numeroserie)
        val botonAgregarVehiculo = findViewById<Button>(R.id.boton_agregarVehiculo)
        val subirLicenciaButton = findViewById<Button>(R.id.subirLicenciaButton)
        tarjetaImageView = findViewById(R.id.TajetaImageView)

        val flechaRegreso = findViewById<ImageView>(R.id.flecha_regreso)
        flechaRegreso.setOnClickListener { finish() }

        // Manejar clic en "Subir tarjeta de circulación"
        subirLicenciaButton.setOnClickListener {
            abrirGaleria()
        }

        // Evento clic para registrar el vehículo
        botonAgregarVehiculo.setOnClickListener {
            val marca = editTextMarca.text.toString().trim()
            val modelo = editTextModelo.text.toString().trim()
            val placa = editTextPlaca.text.toString().trim()
            val color = editTextColor.text.toString().trim()
            val numeroSerie = editTextNumeroSerie.text.toString().trim()

            if (marca.isNotEmpty() && modelo.isNotEmpty() && placa.isNotEmpty() &&
                color.isNotEmpty() && numeroSerie.isNotEmpty() && tarjetaBitmap != null
            ) {
                registrarVehiculo(marca, modelo, placa, color, numeroSerie)
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos y selecciona una imagen", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun abrirGaleria() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        pickImageLauncher.launch(intent)
    }

    private fun uriToBitmap(uri: Uri?): Bitmap? {
        return try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri!!)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            Log.e("AgregarVehiculoActivity", "Error al convertir URI a Bitmap: ${e.message}")
            null
        }
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream) // Reducir calidad
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun registrarVehiculo(
        marca: String,
        modelo: String,
        placa: String,
        color: String,
        numeroSerie: String
    ) {
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            val uid = currentUser.uid
            val userRef = database.child("Usuarios").child(uid)

            // Obtener el nombre del usuario autenticado
            userRef.get().addOnSuccessListener { snapshot ->
                if (snapshot.exists() && snapshot.hasChild("Nombre")) {
                    val nombreUsuario = snapshot.child("Nombre").value.toString()
                    val vehiculoId = database.child("Vehiculos").push().key

                    if (vehiculoId != null) {
                        val tarjetaBase64 = bitmapToBase64(tarjetaBitmap!!)
                        val vehiculoData = mapOf(
                            "id" to vehiculoId,
                            "marca" to marca,
                            "modelo" to modelo,
                            "placa" to placa,
                            "color" to color,
                            "numeroSerie" to numeroSerie,
                            "uidUsuario" to uid,
                            "agregadoPor" to nombreUsuario,
                            "tarjetaBase64" to tarjetaBase64 // Guardar imagen en Base64
                        )

                        database.child("Vehiculos").child(vehiculoId).setValue(vehiculoData)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Vehículo registrado correctamente", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                            .addOnFailureListener { error ->
                                Toast.makeText(this, "Error al registrar el vehículo: ${error.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Toast.makeText(this, "No se pudo obtener el nombre del usuario", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { error ->
                Toast.makeText(this, "Error al obtener datos del usuario: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
        }
    }
}
