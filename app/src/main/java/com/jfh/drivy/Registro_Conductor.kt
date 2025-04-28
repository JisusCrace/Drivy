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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream
import java.io.InputStream

class Registro_Conductor : AppCompatActivity() {

    private lateinit var licenciaImageView: ImageView
    private lateinit var credencialImageView: ImageView
    private var licenciaBitmap: Bitmap? = null
    private var credencialBitmap: Bitmap? = null

    private lateinit var firebaseAuth: FirebaseAuth
    private val database = FirebaseDatabase.getInstance().reference.child("Usuarios") // Referencia corregida

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_conductor)

        firebaseAuth = FirebaseAuth.getInstance()

        // Ajustar el padding para systemBars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configurar botones e imágenes
        licenciaImageView = findViewById(R.id.licenciaImageView)
        credencialImageView = findViewById(R.id.credencialImageView)

        val subirLicenciaButton: Button = findViewById(R.id.subirLicenciaButton)
        val subirCredencialButton: Button = findViewById(R.id.subir_credencial)
        val botonRegistrarse: Button = findViewById(R.id.boton_registrarse)

        subirLicenciaButton.setOnClickListener { abrirGaleria(REQUEST_CODE_LICENCIA) }
        subirCredencialButton.setOnClickListener { abrirGaleria(REQUEST_CODE_CREDENCIAL) }
        botonRegistrarse.setOnClickListener { registrarUsuario("conductor") }
    }

    private fun abrirGaleria(requestCode: Int) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = data?.data

            when (requestCode) {
                REQUEST_CODE_LICENCIA -> {
                    licenciaBitmap = uriToBitmap(imageUri)
                    licenciaImageView.setImageBitmap(licenciaBitmap)
                }
                REQUEST_CODE_CREDENCIAL -> {
                    credencialBitmap = uriToBitmap(imageUri)
                    credencialImageView.setImageBitmap(credencialBitmap)
                }
            }
        }
    }

    private fun uriToBitmap(uri: Uri?): Bitmap? {
        return try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri!!)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            Log.e("RegistroConductor", "Error al convertir URI a Bitmap: ${e.message}")
            null
        }
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream) // Reducir calidad para ahorrar espacio
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun registrarUsuario(rol: String) {
        val nombre = findViewById<EditText>(R.id.ingresar_nombre).text.toString()
        val correo = findViewById<EditText>(R.id.ingresar_correo).text.toString()
        val contrasena = findViewById<EditText>(R.id.ingresar_contraseña).text.toString()
        val telefono = findViewById<EditText>(R.id.ingresar_telefono).text.toString()
        val direccion = findViewById<EditText>(R.id.ingresar_direccion).text.toString()
        val numeroLicencia = findViewById<EditText>(R.id.ingresar_númerodelicencia).text.toString()

        if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || telefono.isEmpty() || direccion.isEmpty() || numeroLicencia.isEmpty() || licenciaBitmap == null || credencialBitmap == null) {
            Toast.makeText(this, "Por favor, completa todos los campos y sube las imágenes.", Toast.LENGTH_SHORT).show()
            return
        }

        // Convertir Bitmap a Base64
        val imagenLicenciaBase64 = bitmapToBase64(licenciaBitmap!!)
        val imagenCredencialBase64 = bitmapToBase64(credencialBitmap!!)

        // Registrar usuario en Firebase Authentication
        firebaseAuth.createUserWithEmailAndPassword(correo, contrasena)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = firebaseAuth.currentUser?.uid ?: return@addOnCompleteListener

                    // Guardar datos en la base de datos
                    val usuario = mapOf(
                        "Nombre" to nombre,
                        "Correo" to correo,
                        "Telefono" to telefono,
                        "Direccion" to direccion,
                        "NumeroLicencia" to numeroLicencia,
                        "Rol" to rol,
                        "Estado" to "sin verificar", // Inicialmente sin verificar
                        "ImagenLicencia" to imagenLicenciaBase64,
                        "ImagenCredencial" to imagenCredencialBase64
                    )

                    database.child(uid).setValue(usuario) // Guardar directamente en el nodo Usuarios
                        .addOnCompleteListener { dbTask ->
                            if (dbTask.isSuccessful) {
                                Log.d("RegistroConductor", "Datos guardados en la base de datos con UID: $uid")
                                Toast.makeText(this, "Registro exitoso. Espera la verificación.", Toast.LENGTH_LONG).show()

                                // Redirigir al MainActivity
                                val intent = Intent(this, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            } else {
                                Log.e("RegistroConductor", "Error al guardar en la base de datos: ${dbTask.exception}")
                                Toast.makeText(this, "Error al guardar datos en la base de datos.", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.e("RegistroConductor", "Error en Realtime Database: ${e.message}")
                            Toast.makeText(this, "Error en Realtime Database: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Log.e("RegistroConductor", "Error al registrar usuario: ${task.exception?.message}")
                    Toast.makeText(this, "Error al registrar usuario: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    companion object {
        private const val REQUEST_CODE_LICENCIA = 1001
        private const val REQUEST_CODE_CREDENCIAL = 1002
    }
}
