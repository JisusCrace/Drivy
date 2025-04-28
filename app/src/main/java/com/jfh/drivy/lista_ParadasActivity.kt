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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class lista_ParadasActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var linearLayoutParadas: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lista_paradas)

        // Ajustar los insets de la pantalla
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Listener para la flecha de regreso
        val flechaRegreso = findViewById<ImageView>(R.id.flecha_regreso)
        flechaRegreso.setOnClickListener {
            val intent = Intent(this, HomeConductorActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Inicializar la referencia al contenedor dinámico
        linearLayoutParadas = findViewById(R.id.contenedor_paradas)

        // Inicializar la referencia a Firebase Database
        database = FirebaseDatabase.getInstance().reference

        // Cargar las paradas del usuario autenticado
        cargarParadas()

        // Botón para agregar paradas
        val botonAgregarParadas = findViewById<Button>(R.id.agregarParadas)
        botonAgregarParadas.setOnClickListener {
            startActivity(Intent(this, AgregarParadaActivity::class.java))
        }

        // Configurar el botón de cerrar sesión
        val botonCerrarSesion = findViewById<ImageView>(R.id.cerrar_sesion)
        botonCerrarSesion.setOnClickListener {
            FirebaseAuth.getInstance().signOut() // Cerrar sesión en Firebase
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Limpiar la pila de actividades
            startActivity(intent)
        }
    }

    private fun cargarParadas() {
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            val uid = currentUser.uid
            Log.d("UIDUsuario", "UID del usuario autenticado: $uid")

            val paradasRef = database.child("Paradas")

            // Consulta las paradas asociadas al UID del usuario autenticado
            paradasRef.orderByChild("uidUsuario").equalTo(uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            // Limpia solo los botones dinámicos (sin afectar el contenido estático)
                            linearLayoutParadas.removeViews(2, linearLayoutParadas.childCount - 2)

                            // Agregar las paradas dinámicamente
                            for (paradaSnapshot in snapshot.children) {
                                val calle = paradaSnapshot.child("calle").value.toString()
                                val paradaId = paradaSnapshot.key

                                if (calle.isNotEmpty() && paradaId != null) {
                                    // Crear un contenedor horizontal para el botón de la parada y el botón de eliminar
                                    val container = LinearLayout(this@lista_ParadasActivity).apply {
                                        orientation = LinearLayout.HORIZONTAL
                                        layoutParams = LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                        ).apply {
                                            setMargins(16, 8, 16, 8)
                                        }
                                        gravity = android.view.Gravity.CENTER_VERTICAL
                                    }

                                    // Crear botón para la parada
                                    val buttonParada = Button(this@lista_ParadasActivity).apply {
                                        text = calle
                                        layoutParams = LinearLayout.LayoutParams(
                                            0,
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            1f
                                        ).apply {
                                            setMargins(8, 0, 8, 0)
                                        }
                                        setBackgroundResource(R.drawable.button_shape)
                                        setTextColor(resources.getColor(R.color.white))
                                        textSize = 18f
                                        setOnClickListener {
                                            // Navegar a DetallesRuta y pasar el ID de la parada
                                            val intent = Intent(this@lista_ParadasActivity, DetalleRuta::class.java)
                                            intent.putExtra("paradaId", paradaId)
                                            intent.putExtra("calle", calle)
                                            startActivity(intent)
                                        }
                                    }

                                    // Crear botón para eliminar
                                    val buttonEliminar = Button(this@lista_ParadasActivity).apply {
                                        layoutParams = LinearLayout.LayoutParams(
                                            60, // Ancho del botón más pequeño
                                            60 // Alto del botón más pequeño
                                        ).apply {
                                            setMargins(8, 0, 8, 0)
                                        }
                                        setBackgroundResource(R.drawable.boton_eliminar)
                                        setOnClickListener {
                                            eliminarParada(paradaId)
                                        }
                                    }

                                    // Agregar botones al contenedor
                                    container.addView(buttonParada)
                                    container.addView(buttonEliminar)

                                    // Agregar contenedor al layout principal
                                    linearLayoutParadas.addView(container)
                                }
                            }
                        } else {
                            Toast.makeText(
                                this@lista_ParadasActivity,
                                "No tienes paradas registradas",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(
                            this@lista_ParadasActivity,
                            "Error al cargar las paradas: ${error.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("FirebaseError", error.message)
                    }
                })
        } else {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun eliminarParada(paradaId: String) {
        database.child("Paradas").child(paradaId).removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Parada eliminada correctamente", Toast.LENGTH_SHORT).show()
                cargarParadas() // Recargar la lista después de eliminar
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, "Error al eliminar la parada: ${error.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
