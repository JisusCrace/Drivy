package com.jfh.drivy

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.*

class AnalisisActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var conductoresBarChart: CustomBarChart
    private lateinit var pasajerosBarChart: CustomBarChart
    private lateinit var alcaldiasBarChart: CustomBarChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analisis)

        // Ajustar insets para compatibilidad visual
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar Firebase y gráficos
        database = FirebaseDatabase.getInstance().reference
        conductoresBarChart = findViewById(R.id.conductores_chart)
        pasajerosBarChart = findViewById(R.id.pasajeros_chart)
        alcaldiasBarChart = findViewById(R.id.alcaldias_chart)

        // Obtener datos
        obtenerDatosDeConductores()
        obtenerDatosDePasajeros()
        obtenerDatosDeParadas()

        findViewById<ImageView>(R.id.flecha_regreso).setOnClickListener {
            finish()
        }
    }

    private fun obtenerDatosDeConductores() {
        val usuariosRef = database.child("Usuarios")
        usuariosRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val diasRegistro = inicializarDiasDeLaSemana()

                for (usuarioSnapshot in snapshot.children) {
                    val rol = usuarioSnapshot.child("Rol").value.toString()
                    val fechaCreacion = usuarioSnapshot.child("fechaCreacion").value.toString()
                    if (rol == "conductor") {
                        val dia = obtenerDiaDeLaSemana(fechaCreacion)
                        if (dia != null) {
                            diasRegistro[dia] = diasRegistro[dia]!! + 1
                        }
                    }
                }

                val data = diasRegistro.map { it.key to it.value }.toList()
                conductoresBarChart.setData(data)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AnalisisActivity, "Error al cargar datos: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun obtenerDatosDePasajeros() {
        val usuariosRef = database.child("Usuarios")
        usuariosRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val diasRegistro = inicializarDiasDeLaSemana()

                for (usuarioSnapshot in snapshot.children) {
                    val rol = usuarioSnapshot.child("Rol").value.toString()
                    val fechaCreacion = usuarioSnapshot.child("fechaCreacion").value.toString()
                    if (rol == "pasajero") {
                        val dia = obtenerDiaDeLaSemana(fechaCreacion)
                        if (dia != null) {
                            diasRegistro[dia] = diasRegistro[dia]!! + 1
                        }
                    }
                }

                val data = diasRegistro.map { it.key to it.value }.toList()
                pasajerosBarChart.setData(data)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AnalisisActivity, "Error al cargar datos: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun obtenerDatosDeParadas() {
        val paradasRef = database.child("Paradas")
        paradasRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val alcaldiasRegistro = mutableMapOf<String, Int>()

                for (paradaSnapshot in snapshot.children) {
                    val alcaldia = paradaSnapshot.child("alcaldia").value.toString()
                    alcaldiasRegistro[alcaldia] = alcaldiasRegistro.getOrDefault(alcaldia, 0) + 1
                }

                val data = alcaldiasRegistro.map { it.key to it.value }.toList()
                alcaldiasBarChart.setData(data)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AnalisisActivity, "Error al cargar datos: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun inicializarDiasDeLaSemana(): MutableMap<String, Int> {
        val diasSemana = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
        return diasSemana.associateWith { 0 }.toMutableMap()
    }

    private fun obtenerDiaDeLaSemana(fecha: String): String? {
        // Implementa la lógica para convertir una fecha (en formato yyyy-MM-dd) a un día de la semana
        // Ejemplo: "2025-01-01" -> "Miércoles"
        return null // Reemplaza esto con la lógica real
    }

    // Clase personalizada para el gráfico de barras
    class CustomBarChart @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
    ) : View(context, attrs, defStyleAttr) {

        private val barPaint = Paint().apply {
            color = Color.BLUE
            style = Paint.Style.FILL
        }

        private val textPaint = Paint().apply {
            color = Color.BLACK
            textSize = 40f
            textAlign = Paint.Align.CENTER
        }

        private val data = mutableListOf<Pair<String, Int>>()

        fun setData(newData: List<Pair<String, Int>>) {
            data.clear()
            data.addAll(newData)
            invalidate()
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)

            if (data.isEmpty()) return

            val barWidth = (width / (data.size * 2)).toFloat()
            val maxBarHeight = (height * 0.7f)
            val maxValue = data.maxOf { it.second }.toFloat()

            data.forEachIndexed { index, pair ->
                val barHeight = (pair.second / maxValue) * maxBarHeight
                val left = ((index * 2 + 1) * barWidth)
                val top = (height - barHeight)
                val right = left + barWidth
                val bottom = height.toFloat()

                // Dibujar barra
                canvas.drawRect(left, top, right, bottom, barPaint)

                // Dibujar etiqueta
                canvas.drawText(pair.first, (left + right) / 2, bottom + 40f, textPaint)
            }
        }
    }
}
