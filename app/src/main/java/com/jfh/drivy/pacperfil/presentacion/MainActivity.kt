package com.example.pacperfil.presentacion

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.pacperfil.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inputId = findViewById<EditText>(R.id.input_id)
        val btnEntrar = findViewById<Button>(R.id.btn_entrar)

        btnEntrar.setOnClickListener {
            val idUsuario = inputId.text.toString()
            val intent = Intent(this, PerfilActivity::class.java)
            intent.putExtra("userId", idUsuario)
            startActivity(intent)
        }
    }
}

