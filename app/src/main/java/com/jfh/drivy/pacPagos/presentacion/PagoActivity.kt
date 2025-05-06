package com.jfh.drivy.pacPagos.presentacion

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jfh.drivy.R
import com.jfh.drivy.gestores.GestorReserva
import com.jfh.drivy.pacPagos.abstraccion.*

class PagoActivity : AppCompatActivity() {

    private lateinit var tvResultado: TextView
    private var paradaId: String? = null
    private val requestCodePaypal = 1001
    private val requestCodeMP = 1002

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pago)

        paradaId = intent.getStringExtra("paradaId")
        tvResultado = findViewById(R.id.tvResultado)

        findViewById<ImageView>(R.id.flecha_regreso).setOnClickListener {
            finish()
        }

        findViewById<ImageView>(R.id.btnPaypal).setOnClickListener {
            startActivityForResult(Intent(this, PaypalSimActivity::class.java), requestCodePaypal)
        }

        findViewById<ImageView>(R.id.btnMercadoPago).setOnClickListener {
            startActivityForResult(Intent(this, MercadoPagoSimActivity::class.java), requestCodeMP)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && paradaId != null) {
            val cantidad = 100.0

            val metodo: InterfazMetodoPago = when (requestCode) {
                requestCodePaypal -> Paypal()
                requestCodeMP -> MercadoPago()
                else -> throw IllegalArgumentException("Método desconocido")
            }

            val pago: InterfazPago = Pago(metodo)
            val resultado = pago.realizarPago(cantidad)

            tvResultado.text = resultado
            tvResultado.visibility = TextView.VISIBLE

            GestorReserva().realizarReserva(paradaId!!)
        } else {
            Toast.makeText(this, "❌ Pago cancelado", Toast.LENGTH_SHORT).show()
        }
    }
}
