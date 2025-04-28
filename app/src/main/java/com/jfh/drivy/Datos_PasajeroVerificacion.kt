package com.jfh.drivy

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.*
import java.util.*
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class Datos_PasajeroVerificacion : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var pasajeroUid: String
    private var pasajeroCorreo: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_datos_pasajero_verificacion)

        // Ajustar los insets de la pantalla
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configurar flecha de regreso
        findViewById<ImageView>(R.id.flecha_regreso).setOnClickListener {
            onBackPressed()
        }

        // Obtener UID del pasajero desde el Intent
        pasajeroUid = intent.getStringExtra("pasajeroUid") ?: ""
        if (pasajeroUid.isEmpty()) {
            Toast.makeText(this, "UID del pasajero no encontrado", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Inicializar Firebase
        database = FirebaseDatabase.getInstance().reference

        // Cargar datos del pasajero
        cargarDatosPasajero()

        // Configurar botones
        findViewById<Button>(R.id.boton_aceptarVerificación).setOnClickListener {
            aceptarVerificacion()
        }

        findViewById<Button>(R.id.boton_rechazarVerificación).setOnClickListener {
            rechazarVerificacion()
        }
    }

    private fun cargarDatosPasajero() {
        val nombreField = findViewById<TextView>(R.id.mostrar_nombre)
        val correoField = findViewById<TextView>(R.id.mostrar_correo)
        val credencialImage = findViewById<ImageView>(R.id.subir_credencial)

        database.child("Usuarios").child(pasajeroUid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val nombre = snapshot.child("Nombre").value.toString()
                        val correo = snapshot.child("Correo").value.toString()
                        val imagenCredencialBase64 = snapshot.child("ImagenCredencial").value.toString()

                        pasajeroCorreo = correo

                        nombreField.text = nombre
                        correoField.text = correo

                        if (imagenCredencialBase64.isNotEmpty()) {
                            val credencialBitmap = decodeBase64ToBitmap(imagenCredencialBase64)
                            credencialImage.setImageBitmap(credencialBitmap)
                        }
                    } else {
                        Toast.makeText(this@Datos_PasajeroVerificacion, "Pasajero no encontrado", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@Datos_PasajeroVerificacion, "Error al cargar datos: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun aceptarVerificacion() {
        database.child("Usuarios").child(pasajeroUid).child("Estado").setValue("verificado")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    enviarCorreo(
                        pasajeroCorreo,
                        "Verificación exitosa en Drivy",
                        "Hemos verificado tu perfil en Drivy. Ahora puedes utilizar todos nuestros servicios. ¡Bienvenido!"
                    )
                    Toast.makeText(this, "Verificación aceptada", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Error al actualizar estado", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun rechazarVerificacion() {
        database.child("Usuarios").child(pasajeroUid).removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    enviarCorreo(
                        pasajeroCorreo,
                        "Verificación rechazada en Drivy",
                        "Lo sentimos mucho, hemos rechazado tu verificación en Drivy. Intenta registrarte de nuevo."
                    )
                    Toast.makeText(this, "Verificación rechazada y datos eliminados", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Error al eliminar datos", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun enviarCorreo(destinatario: String?, asunto: String, mensaje: String) {
        if (destinatario.isNullOrEmpty()) {
            Toast.makeText(this, "Correo no válido", Toast.LENGTH_SHORT).show()
            return
        }

        val email = "drivyappspp@gmail.com" // Reemplaza con tu correo
        val password = "xvav figu nrbh lzzq" // Reemplaza con la contraseña de aplicación

        val props = Properties().apply {
            put("mail.smtp.host", "smtp.gmail.com")
            put("mail.smtp.socketFactory.port", "465")
            put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
            put("mail.smtp.auth", "true")
            put("mail.smtp.port", "465")
        }

        Thread {
            try {
                val session = Session.getInstance(props, object : javax.mail.Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication(email, password)
                    }
                })

                val message = MimeMessage(session).apply {
                    setFrom(InternetAddress(email))
                    setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario))
                    subject = asunto
                    setText(mensaje)
                }

                Transport.send(message)

                runOnUiThread {
                    Toast.makeText(this, "Correo enviado correctamente", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this, "Error al enviar correo: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }.start()
    }

    private fun decodeBase64ToBitmap(base64Str: String): android.graphics.Bitmap {
        val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }
}
