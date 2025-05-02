package com.jfh.drivy.pacPagos.presentacion

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.jfh.drivy.R

class MercadoPagoSimActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mercadopago_sim)

        findViewById<Button>(R.id.btnConfirmar).setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("metodo", "mercadopago")
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}
