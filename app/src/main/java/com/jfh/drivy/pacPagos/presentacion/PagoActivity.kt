package com.jfh.drivy.pacPagos.presentacion

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jfh.drivy.R
import com.jfh.drivy.pacPagos.abstraccion.MercadoPago
import com.jfh.drivy.pacPagos.abstraccion.Pago
import com.jfh.drivy.pacPagos.abstraccion.Paypal
import com.jfh.drivy.pacPagos.control.GestorPago

class PagoActivity : AppCompatActivity() {

    private lateinit var tvResultado: TextView
    private val requestCode = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pago)

        tvResultado = findViewById(R.id.tvResultado)

        findViewById<Button>(R.id.btnPaypal).setOnClickListener {
            val intent = Intent(this, PaypalSimActivity::class.java)
            startActivityForResult(intent, requestCode)
        }

        findViewById<Button>(R.id.btnMercadoPago).setOnClickListener {
            val intent = Intent(this, MercadoPagoSimActivity::class.java)
            startActivityForResult(intent, requestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == this.requestCode && resultCode == RESULT_OK) {
            val metodo = data?.getStringExtra("metodo")
            val resultado = when (metodo) {
                "paypal" -> GestorPago().procesarPago(Pago(Paypal()))
                "mercadopago" -> GestorPago().procesarPago(Pago(MercadoPago()))
                else -> "❌ Método de pago inválido."
            }
            tvResultado.text = resultado
        }
    }
}
