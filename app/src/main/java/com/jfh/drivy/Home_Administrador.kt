package com.jfh.drivy

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import androidx.cardview.widget.CardView

class Home_Administrador : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_administrador)

        // Ajustar los insets de la pantalla
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar referencia a Firebase Database
        database = FirebaseDatabase.getInstance().reference

        // Mostrar el nombre del usuario autenticado
        mostrarNombreUsuario()

        // Configurar el botón para navegar a Lista_VerificarConductores
        val botonVerificacionConductor = findViewById<CardView>(R.id.boton_verificaciónConductor)
        botonVerificacionConductor.setOnClickListener {
            val intent = Intent(this, Lista_VerificarConductores::class.java)
            startActivity(intent)
        }

        // Configurar el botón para navegar a Lista_VerificarPasajeros
        val botonVerificacionPasajero = findViewById<CardView>(R.id.boton_verificaciónPasajero)
        botonVerificacionPasajero.setOnClickListener {
            val intent = Intent(this, Lista_VerificarPasajeros::class.java)
            startActivity(intent)
        }

        val botonGraficas = findViewById<CardView>(R.id.boton_graficas)
        botonGraficas.setOnClickListener {
            val intent = Intent(this, AnalisisActivity::class.java)
            startActivity(intent)
        }

    }

    private fun mostrarNombreUsuario() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val textViewNombre = findViewById<TextView>(R.id.nombre_usuario_administrador)

        if (currentUser != null) {
            val uid = currentUser.uid
            val userRef = database.child("Usuarios").child(uid)

            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists() && snapshot.hasChild("Nombre")) {
                        val nombre = snapshot.child("Nombre").value.toString()
                        textViewNombre.text = "¡Bienvenido, $nombre!"
                    } else {
                        textViewNombre.text = "¡Bienvenido, Administrador!"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    textViewNombre.text = "¡Bienvenido, Administrador!"
                    Toast.makeText(
                        this@Home_Administrador,
                        "Error al obtener el nombre del usuario: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        } else {
            textViewNombre.text = "¡Bienvenido, Administrador!"
        }
    }
}
