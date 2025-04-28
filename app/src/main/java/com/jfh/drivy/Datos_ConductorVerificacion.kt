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

class Datos_ConductorVerificacion : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var conductorUid: String
    private var conductorCorreo: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_datos_conductor_verificacion)

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

        // Obtener UID del conductor desde el Intent
        conductorUid = intent.getStringExtra("conductorUid") ?: ""
        if (conductorUid.isEmpty()) {
            Toast.makeText(this, "UID del conductor no encontrado", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Inicializar Firebase
        database = FirebaseDatabase.getInstance().reference

        // Cargar datos del conductor
        cargarDatosConductor()

        // Configurar botones
        findViewById<Button>(R.id.boton_aceptarVerificación).setOnClickListener {
            aceptarVerificacion()
        }

        findViewById<Button>(R.id.boton_rechazarVerificación).setOnClickListener {
            rechazarVerificacion()
        }
    }

    private fun cargarDatosConductor() {
        val nombreField = findViewById<TextView>(R.id.mostrar_nombre)
        val correoField = findViewById<TextView>(R.id.mostrar_correo)
        val numeroLicenciaField = findViewById<TextView>(R.id.mostrar_númerodelicencia)
        val licenciaImage = findViewById<ImageView>(R.id.subir_licencia)
        val credencialImage = findViewById<ImageView>(R.id.subir_credencial)

        database.child("Usuarios").child(conductorUid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val nombre = snapshot.child("Nombre").value.toString()
                        val correo = snapshot.child("Correo").value.toString()
                        val numeroLicencia = snapshot.child("NumeroLicencia").value.toString()
                        val imagenLicenciaBase64 = snapshot.child("ImagenLicencia").value.toString()
                        val imagenCredencialBase64 = snapshot.child("ImagenCredencial").value.toString()

                        conductorCorreo = correo

                        nombreField.text = nombre
                        correoField.text = correo
                        numeroLicenciaField.text = numeroLicencia

                        if (imagenLicenciaBase64.isNotEmpty()) {
                            val licenciaBitmap = decodeBase64ToBitmap(imagenLicenciaBase64)
                            licenciaImage.setImageBitmap(licenciaBitmap)
                        }

                        if (imagenCredencialBase64.isNotEmpty()) {
                            val credencialBitmap = decodeBase64ToBitmap(imagenCredencialBase64)
                            credencialImage.setImageBitmap(credencialBitmap)
                        }
                    } else {
                        Toast.makeText(this@Datos_ConductorVerificacion, "Conductor no encontrado", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@Datos_ConductorVerificacion, "Error al cargar datos: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun aceptarVerificacion() {
        database.child("Usuarios").child(conductorUid).child("Estado").setValue("verificado")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    enviarCorreo(
                        conductorCorreo,
                        "Verificación exitosa en Drivy",
                        "Hola Conductor. Hemos verificado tu perfil en Drivy. Ahora puedes ofrecer tus viajes. ¡Disfruta!"
                    )
                    Toast.makeText(this, "Verificación aceptada", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Error al actualizar estado", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun rechazarVerificacion() {
        database.child("Usuarios").child(conductorUid).removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    enviarCorreo(
                        conductorCorreo,
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
