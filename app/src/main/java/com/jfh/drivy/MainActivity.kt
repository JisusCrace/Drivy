package com.jfh.drivy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    /*private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var usuariosRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseAuth = FirebaseAuth.getInstance()
        usuariosRef = FirebaseDatabase.getInstance().getReference("Usuarios") // Nodo principal único

        val botonIniciarSesion = findViewById<Button>(R.id.boton_iniciarSesión)
        botonIniciarSesion.setOnClickListener {
            val correo = findViewById<EditText>(R.id.editText_correo).text.toString().trim()
            val contraseña = findViewById<EditText>(R.id.editText_contraseña).text.toString().trim()

            if (correo.isEmpty() || contraseña.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            iniciarSesion(correo, contraseña)
        }

        val botonRegistrarse = findViewById<Button>(R.id.boton_registro)
        botonRegistrarse.setOnClickListener {
            val intent = Intent(this, eleccionRegistroActivity::class.java)
            startActivity(intent)
        }
    }

    private fun iniciarSesion(correo: String, contraseña: String) {
        firebaseAuth.signInWithEmailAndPassword(correo, contraseña)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = firebaseAuth.currentUser?.uid
                    if (uid != null) {
                        verificarEstadoYRol(uid)
                    } else {
                        Toast.makeText(this, "Error al obtener los datos del usuario.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("MainActivity", "Error de inicio de sesión: ${task.exception}")
                    Toast.makeText(this, "Correo o contraseña incorrectos.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Log.e("MainActivity", "Fallo al iniciar sesión: ${exception.message}")
                Toast.makeText(this, "Error al iniciar sesión: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun verificarEstadoYRol(uid: String) {
        usuariosRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val estado = snapshot.child("Estado").getValue(String::class.java)
                    val rol = snapshot.child("Rol").getValue(String::class.java)

                    if (estado == null || rol == null) {
                        Toast.makeText(
                            this@MainActivity,
                            "Error: Datos incompletos del usuario.",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }

                    if (estado.lowercase() == "verificado") {
                        when (rol.lowercase()) {
                            "conductor" -> irAHomeConductor()
                            "pasajero" -> irAHomePasajero()
                            "administrador" -> irAHomeAdministrador()
                            else -> Toast.makeText(
                                this@MainActivity,
                                "Rol no reconocido. Contacta al soporte.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        mostrarMensajeNoVerificado()
                    }
                } else {
                    Log.e("MainActivity", "No se encontró el usuario con UID: $uid")
                    Toast.makeText(this@MainActivity, "Usuario no encontrado.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MainActivity", "Error al consultar datos: ${error.message}")
                Toast.makeText(
                    this@MainActivity,
                    "Error al consultar datos: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun mostrarMensajeNoVerificado() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Cuenta no verificada")
        builder.setMessage("Tu cuenta aún no ha sido verificada. Por favor, espera el correo de confirmación.")
        builder.setPositiveButton("Aceptar") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

    private fun irAHomeConductor() {
        val intent = Intent(this, HomeConductorActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun irAHomePasajero() {
        val intent = Intent(this, HomePasajeroActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun irAHomeAdministrador() {
        val intent = Intent(this, Home_Administrador::class.java)
        startActivity(intent)
        finish()
    }*/
}
