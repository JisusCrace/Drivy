package com.jfh.drivy

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeConductorActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_conductor)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        database = FirebaseDatabase.getInstance().reference

        mostrarNombreUsuario()

        val botonParadas = findViewById<CardView>(R.id.boton_Paradas)
        botonParadas.setOnClickListener {
            val intent = Intent(this, lista_ParadasActivity::class.java)
            startActivity(intent)
        }

        val botonVehiculos = findViewById<CardView>(R.id.boton_Vehiculos)
        botonVehiculos.setOnClickListener {
            val intent = Intent(this, Lista_Vehiculos::class.java)
            startActivity(intent)
        }

        val botonCerrarSesion = findViewById<ImageView>(R.id.cerrar_sesion)
        botonCerrarSesion.setOnClickListener {
            FirebaseAuth.getInstance().signOut() // Cerrar sesión en Firebase
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Limpiar la pila de actividades
            startActivity(intent)
        }
    }

    private fun mostrarNombreUsuario() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val textViewNombre = findViewById<TextView>(R.id.nombre_usuario)

        if (currentUser != null) {
            val uid = currentUser.uid
            val userRef = database.child("Usuarios").child(uid)


            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists() && snapshot.hasChild("Nombre")) {
                        val nombre = snapshot.child("Nombre").value.toString()
                        runOnUiThread { textViewNombre.text = "¡Hola, $nombre!" }
                    } else {
                        runOnUiThread { textViewNombre.text = "¡Hola, Usuario!" }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    runOnUiThread {
                        textViewNombre.text = "¡Hola, Usuario!"
                        Toast.makeText(
                            this@HomeConductorActivity,
                            "Error al obtener los datos: ${error.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
        } else {
            textViewNombre.text = "¡Hola, Usuario!"
        }
    }
}
