package com.jfh.drivy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.*

class Lista_VerificarPasajeros : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lista_verificar_pasajeros)

        // Ajustar los insets de la pantalla
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configurar flecha de regreso
        findViewById<ImageView>(R.id.flecha_regreso).setOnClickListener {
            onBackPressed()
        }

        // Inicializar la referencia a Firebase
        database = FirebaseDatabase.getInstance().reference

        // Cargar pasajeros sin verificar
        cargarPasajeros()
    }

    private fun cargarPasajeros() {
        val listaPasajeros = findViewById<LinearLayout>(R.id.eleccion_pasajeros)

        val usuariosRef = database.child("Usuarios")
        usuariosRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaPasajeros.removeAllViews() // Limpia la lista antes de agregar elementos
                for (userSnapshot in snapshot.children) {
                    val uid = userSnapshot.key
                    val rol = userSnapshot.child("Rol").value.toString()
                    val estado = userSnapshot.child("Estado").value.toString()
                    val nombre = userSnapshot.child("Nombre").value.toString()

                    // Filtrar solo pasajeros con estado "sin verificar"
                    if (rol == "pasajero" && estado == "sin verificar") {
                        // Crear un botón para el pasajero
                        val button = Button(this@Lista_VerificarPasajeros).apply {
                            text = nombre
                            textSize = 16f
                            setPadding(16, 16, 16, 16)
                            setBackgroundResource(R.drawable.button_shape)
                            setTextColor(resources.getColor(R.color.white))
                            setOnClickListener {
                                // Navegar a Datos_PasajeroVerificacion
                                val intent = Intent(this@Lista_VerificarPasajeros, Datos_PasajeroVerificacion::class.java)
                                intent.putExtra("pasajeroUid", uid) // Pasar el UID del pasajero
                                startActivity(intent)
                            }
                        }

                        // Agregar el botón al LinearLayout
                        listaPasajeros.addView(button)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ListaPasajeros", "Error al leer la base de datos: ${error.message}")
                Toast.makeText(
                    this@Lista_VerificarPasajeros,
                    "Error al cargar pasajeros: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
