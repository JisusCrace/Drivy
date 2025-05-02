package com.jfh.drivy

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.jfh.drivy.pacPagos.presentacion.PagoActivity

class VerRutasPasajeroActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var paradasLayout: LinearLayout
    private lateinit var tituloParadas: TextView
    private lateinit var contenedorSinParadas: LinearLayout
    private lateinit var textoSinParadas: TextView
    private lateinit var imagenSinParadas: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_rutas_pasajero)

        database = FirebaseDatabase.getInstance().reference
        paradasLayout = findViewById(R.id.contenedor_paradas)
        tituloParadas = findViewById(R.id.titulo_paradas)
        contenedorSinParadas = findViewById(R.id.contenedor_sin_paradas)
        textoSinParadas = findViewById(R.id.texto_sin_paradas)
        imagenSinParadas = findViewById(R.id.imagen_sin_paradas)

        val flechaRegreso = findViewById<ImageView>(R.id.flecha_regreso)
        flechaRegreso.setOnClickListener { finish() }

        val alcaldiaSeleccionada = intent.getStringExtra("ALCALDIA_SELECCIONADA")
        if (alcaldiaSeleccionada != null) {
            tituloParadas.text = "Paradas disponibles en $alcaldiaSeleccionada"
            cargarParadasPorAlcaldia(alcaldiaSeleccionada)
        } else {
            Toast.makeText(this, "No se recibió la alcaldía seleccionada.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cargarParadasPorAlcaldia(alcaldia: String) {
        val paradasRef = database.child("Paradas")
        paradasRef.orderByChild("alcaldia").equalTo(alcaldia)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    paradasLayout.removeAllViews()
                    if (snapshot.exists()) {
                        contenedorSinParadas.visibility = View.GONE

                        for (paradaSnapshot in snapshot.children) {
                            val calle = paradaSnapshot.child("calle").value.toString()
                            val paradaId = paradaSnapshot.key ?: ""

                            val button = Button(this@VerRutasPasajeroActivity).apply {
                                text = "Reservar: $calle"
                                setBackgroundResource(R.drawable.button_shape)
                                setPadding(20, 20, 20, 20)
                                setTextColor(resources.getColor(R.color.white))
                                gravity = Gravity.CENTER
                                layoutParams = LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                ).apply {
                                    setMargins(20, 10, 20, 10)
                                }

                                setOnClickListener {
                                    val intent = Intent(
                                        this@VerRutasPasajeroActivity,
                                        PagoActivity::class.java
                                    )
                                    intent.putExtra("paradaId", paradaId)
                                    startActivity(intent)
                                }
                            }

                            paradasLayout.addView(button)
                        }
                    } else {
                        contenedorSinParadas.visibility = View.VISIBLE
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@VerRutasPasajeroActivity,
                        "Error al cargar las paradas: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}
