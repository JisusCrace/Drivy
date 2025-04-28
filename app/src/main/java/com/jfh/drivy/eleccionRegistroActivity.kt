
package com.jfh.drivy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class eleccionRegistroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_eleccion_registro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        findViewById<ImageView>(R.id.flecha_regreso).setOnClickListener {
            finish() // Termina la actividad actual y regresa a la anterior
        }
        findViewById<CardView>(R.id.boton_registroConductor).setOnClickListener {
            val intent = Intent(this, Registro_Conductor::class.java)
            startActivity(intent)
        }
        findViewById<CardView>(R.id.boton_registroPasajero).setOnClickListener {
            val intent = Intent(this, registro_Pasajero::class.java)
            startActivity(intent)
        }
    }
}