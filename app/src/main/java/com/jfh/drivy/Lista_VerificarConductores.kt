package com.jfh.drivy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.*
import android.widget.Button
import android.widget.ImageView

class Lista_VerificarConductores : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lista_verificar_conductores)

        // Ajustar los insets de la pantalla
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar la referencia a Firebase
        database = FirebaseDatabase.getInstance().reference

        // Configurar flecha de regreso
        findViewById<ImageView>(R.id.flecha_regreso).setOnClickListener {
            onBackPressed()
        }

        // Llamar al método para obtener los conductores
        obtenerConductores()
    }

    private fun obtenerConductores() {
        val listaConductores = findViewById<LinearLayout>(R.id.eleccion_conductor)

        val usuariosRef = database.child("Usuarios")
        usuariosRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaConductores.removeAllViews() // Limpia la lista antes de agregar elementos
                for (userSnapshot in snapshot.children) {
                    val rol = userSnapshot.child("Rol").value.toString()
                    val estado = userSnapshot.child("Estado").value.toString()
                    val nombre = userSnapshot.child("Nombre").value.toString()
                    val uid = userSnapshot.key // Obtén el UID del usuario

                    // Filtrar solo conductores con estado "sin verificar"
                    if (rol == "conductor" && estado == "sin verificar") {
                        // Crear un botón para el conductor
                        val button = Button(this@Lista_VerificarConductores).apply {
                            text = nombre
                            textSize = 16f
                            setPadding(16, 16, 16, 16)
                            setBackgroundResource(R.drawable.button_shape) // Personaliza el diseño del botón si es necesario
                            setTextColor(resources.getColor(R.color.white))
                            setOnClickListener {
                                val intent = Intent(this@Lista_VerificarConductores, Datos_ConductorVerificacion::class.java)
                                intent.putExtra("conductorUid", uid) // Pasa el UID del conductor
                                startActivity(intent)
                            }
                        }

                        // Agregar el botón al LinearLayout
                        listaConductores.addView(button)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ListaConductores", "Error al leer la base de datos: ${error.message}")
                Toast.makeText(
                    this@Lista_VerificarConductores,
                    "Error al cargar conductores: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
