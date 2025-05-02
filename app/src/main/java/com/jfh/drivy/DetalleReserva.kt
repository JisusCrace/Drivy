package com.jfh.drivy

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class DetalleReserva : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var database: DatabaseReference
    private lateinit var map: GoogleMap
    private var latitud: Double? = null
    private var longitud: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_reserva)

        val reservaId = intent.getStringExtra("reservaId")
        if (reservaId == null) {
            Toast.makeText(this, "No se recibió ID de reserva", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        database = FirebaseDatabase.getInstance().reference

        val flechaRegreso = findViewById<ImageView>(R.id.flecha_regreso)
        flechaRegreso.setOnClickListener { finish() }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.fragmentMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
        checkPermissions()

        cargarReserva(reservaId)
    }

    private fun cargarReserva(reservaId: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val uid = currentUser.uid
        val reservaRef = database.child("Reservaciones").child(uid).child(reservaId)

        reservaRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val alcaldia = snapshot.child("alcaldia").value?.toString() ?: ""
                val dia = snapshot.child("dia").value?.toString() ?: ""
                val hora = snapshot.child("hora").value?.toString() ?: ""
                val nombreConductor = snapshot.child("nombreConductor").value?.toString() ?: ""
                val telefonoConductor = snapshot.child("telefonoConductor").value?.toString() ?: ""
                val numeroLicencia = snapshot.child("numeroLicencia").value?.toString() ?: ""
                val nombrePasajero = snapshot.child("nombrePasajero").value?.toString() ?: ""
                val telefonoPasajero = snapshot.child("telefonoPasajero").value?.toString() ?: ""
                val codigoVerificacion = snapshot.child("codigoVerificacion").value?.toString() ?: ""
                latitud = snapshot.child("latitud").getValue(Double::class.java)
                longitud = snapshot.child("longitud").getValue(Double::class.java)

                findViewById<TextView>(R.id.text_alcaldia).text = "Alcaldía: $alcaldia"
                findViewById<TextView>(R.id.text_dia).text = "Día: $dia"
                findViewById<TextView>(R.id.text_hora).text = "Hora: $hora"
                findViewById<TextView>(R.id.text_nombre_conductor).text = "Nombre del Conductor: $nombreConductor"
                findViewById<TextView>(R.id.text_telefono_conductor).text = "Teléfono Conductor: $telefonoConductor"
                findViewById<TextView>(R.id.text_numero_licencia).text = "Número de Licencia: $numeroLicencia"
                findViewById<TextView>(R.id.text_nombre_pasajero).text = "Nombre Pasajero: $nombrePasajero"
                findViewById<TextView>(R.id.text_telefono_pasajero).text = "Teléfono Pasajero: $telefonoPasajero"
                findViewById<TextView>(R.id.text_codigo).text = "Código de Verificación: $codigoVerificacion"

                mostrarUbicacionEnMapa()
            } else {
                Toast.makeText(this, "Reserva no encontrada", Toast.LENGTH_SHORT).show()
                finish()
            }
        }.addOnFailureListener { error ->
            Toast.makeText(this, "Error al cargar reserva: ${error.message}", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun mostrarUbicacionEnMapa() {
        if (latitud != null && longitud != null && ::map.isInitialized) {
            val ubicacion = LatLng(latitud!!, longitud!!)
            map.clear()
            map.addMarker(MarkerOptions().position(ubicacion).title("Ubicación reserva"))
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 15f))
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isMyLocationButtonEnabled = true

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.isMyLocationEnabled = true
        }
    }

    private fun checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
    }
}
