package com.jfh.drivy.pacPagos.presentacion

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jfh.drivy.R

class MercadoPagoSimActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mercadopago_sim)

        findViewById<Button>(R.id.btnConfirmarPago).setOnClickListener {
            Toast.makeText(this, "✅ Pago confirmado con MercadoPago", Toast.LENGTH_SHORT).show()
            setResult(Activity.RESULT_OK)
            finish()
        }

        findViewById<Button>(R.id.btnCancelar).setOnClickListener {
            Toast.makeText(this, "❌ Pago cancelado", Toast.LENGTH_SHORT).show()
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }
}
