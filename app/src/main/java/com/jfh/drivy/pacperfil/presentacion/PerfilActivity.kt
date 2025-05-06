package com.example.pacperfil.presentacion

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.pacperfil.R
import com.example.pacperfil.abstraccion.PerfilConductorDecorator
import com.example.pacperfil.abstraccion.PerfilPasajeroDecorator
import com.example.pacperfil.control.ControladorPerfil

class PerfilActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val idUsuario = intent.getStringExtra("idUsuario") ?: ""
        val controlador = ControladorPerfil(this)
        val perfil = controlador.obtenerPerfil(idUsuario, this)

        when (perfil) {
            is PerfilConductorDecorator -> {
                setContentView(R.layout.activity_perfil_conductor)

                val txtNombre = findViewById<TextView>(R.id.txt_nombre)
                val txtUltimaParada = findViewById<TextView>(R.id.txt_ultima_parada)
                val contenedorVehiculos = findViewById<LinearLayout>(R.id.card_vehiculos)

                txtNombre.text = perfil.nombre
                txtUltimaParada.text = perfil.getUltimaParada()

                perfil.getListaVehiculos().forEach { vehiculo ->
                    val nuevoVehiculo = TextView(this).apply {
                        text = vehiculo
                        textSize = 14f
                        setPadding(0, 8, 0, 8)
                    }
                    contenedorVehiculos.addView(nuevoVehiculo)
                }
            }

            is PerfilPasajeroDecorator -> {
                setContentView(R.layout.activity_perfil_pasajero)

                val txtNombre = findViewById<TextView>(R.id.txt_nombre)
                val txtUltimaReserva = findViewById<TextView>(R.id.txt_ultima_reserva)
                val contenedorReservas = findViewById<LinearLayout>(R.id.lista_reservas)

                txtNombre.text = perfil.nombre
                txtUltimaReserva.text = perfil.getUltimaReserva()

                perfil.getListaReservas().forEach { reserva ->
                    val nuevaReserva = TextView(this).apply {
                        text = reserva
                        textSize = 14f
                        setPadding(0, 8, 0, 8)
                    }
                    contenedorReservas.addView(nuevaReserva)
                }
            }

            else -> {
                Toast.makeText(this, "Perfil no encontrado", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
