package com.jfh.drivy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Lista_Vehiculos : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var linearLayoutVehiculos: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_vehiculos)

        // Inicializar referencia a Firebase Database y contenedor dinámico
        database = FirebaseDatabase.getInstance().reference
        linearLayoutVehiculos = findViewById(R.id.contenedor_vehiculo)

        // Configurar la flecha de regreso
        val flechaRegreso = findViewById<ImageView>(R.id.flecha_regreso)
        flechaRegreso.setOnClickListener { finish() }

        // Configurar el botón de agregar vehículo
        val botonAgregarVehiculo = findViewById<Button>(R.id.boton_agregarVehiculo)
        botonAgregarVehiculo.setOnClickListener {
            val intent = Intent(this, AgregarVehiculoActivity::class.java)
            startActivity(intent)
        }

        // Configurar el botón de cerrar sesión
        val botonCerrarSesion = findViewById<ImageView>(R.id.cerrar_sesion)
        botonCerrarSesion.setOnClickListener {
            FirebaseAuth.getInstance().signOut() // Cerrar sesión en Firebase
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Limpiar la pila de actividades
            startActivity(intent)
        }

        // Cargar los vehículos del usuario autenticado
        cargarVehiculos()
    }

    private fun cargarVehiculos() {
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            val uid = currentUser.uid

            database.child("Vehiculos").orderByChild("uidUsuario").equalTo(uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            // Limpia solo los botones dinámicos (sin afectar el contenido estático)
                            linearLayoutVehiculos.removeViews(2, linearLayoutVehiculos.childCount - 2)

                            // Agregar los vehículos dinámicamente
                            for (vehiculoSnapshot in snapshot.children) {
                                val marca = vehiculoSnapshot.child("marca").value.toString()
                                val vehiculoId = vehiculoSnapshot.key

                                if (marca.isNotEmpty() && vehiculoId != null) {
                                    // Crear un contenedor horizontal para el botón del vehículo y el botón de eliminar
                                    val container = LinearLayout(this@Lista_Vehiculos).apply {
                                        orientation = LinearLayout.HORIZONTAL
                                        layoutParams = LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                        ).apply {
                                            setMargins(16, 8, 16, 8)
                                        }
                                        gravity = android.view.Gravity.CENTER_VERTICAL
                                    }

                                    // Crear botón para el vehículo
                                    val buttonVehiculo = Button(this@Lista_Vehiculos).apply {
                                        text = marca
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
                                            // Navegar a DetalleVehiculoConductorActivity y pasar el ID del vehículo
                                            val intent = Intent(this@Lista_Vehiculos, DetalleVehiculoConductorActivity::class.java)
                                            intent.putExtra("vehiculoId", vehiculoId)
                                            startActivity(intent)
                                        }
                                    }

                                    // Crear botón para eliminar
                                    val buttonEliminar = Button(this@Lista_Vehiculos).apply {
                                        layoutParams = LinearLayout.LayoutParams(
                                            60, // Ancho del botón más pequeño
                                            60 // Alto del botón más pequeño
                                        ).apply {
                                            setMargins(8, 0, 8, 0)
                                        }
                                        setBackgroundResource(R.drawable.boton_eliminar)
                                        setOnClickListener {
                                            eliminarVehiculo(vehiculoId)
                                        }
                                    }

                                    // Agregar botones al contenedor
                                    container.addView(buttonVehiculo)
                                    container.addView(buttonEliminar)

                                    // Agregar contenedor al layout principal
                                    linearLayoutVehiculos.addView(container)
                                }
                            }
                        } else {
                            Toast.makeText(this@Lista_Vehiculos, "No tienes vehículos registrados", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@Lista_Vehiculos, "Error al cargar vehículos: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    private fun eliminarVehiculo(vehiculoId: String) {
        database.child("Vehiculos").child(vehiculoId).removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Vehículo eliminado correctamente", Toast.LENGTH_SHORT).show()
                cargarVehiculos() // Recargar la lista después de eliminar
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, "Error al eliminar el vehículo: ${error.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
