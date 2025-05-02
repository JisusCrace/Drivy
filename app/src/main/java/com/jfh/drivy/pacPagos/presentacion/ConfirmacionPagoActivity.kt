package com.jfh.drivy.pacPagos.presentacion

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.jfh.drivy.R
import com.jfh.drivy.HomePasajeroActivity
import com.jfh.drivy.pacReserva.abstraccion.GestorOrdenPago // ✅ Importa correctamente el PAC de Reserva

class ConfirmacionPagoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmacion_pago)

        val paradaId = intent.getStringExtra("paradaId")

        val btnContinuar = findViewById<Button>(R.id.btnContinuar)
        btnContinuar.setOnClickListener {
            if (paradaId != null) {
                // ✅ Usa el PAC de Reserva de forma modular
                val gestorOrdenPago = GestorOrdenPago()
                gestorOrdenPago.confirmarPagoYCrearReserva(paradaId)

                // ✅ Regresa a pantalla principal del pasajero
                val intent = Intent(this, HomePasajeroActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Log.e("ConfirmacionPago", "❌ paradaId es null")
            }
        }
    }
}
