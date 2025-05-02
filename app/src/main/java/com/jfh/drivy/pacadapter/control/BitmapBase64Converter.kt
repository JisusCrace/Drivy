package com.jfh.drivy.pacadapter.control

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream

class BitmapBase64Converter(private val context: Context) {

    fun toBase64(uriString: String): String {
        val uri = Uri.parse(uriString)
        context.contentResolver.openInputStream(uri).use { input ->
            val bmp: Bitmap = BitmapFactory.decodeStream(input)
            val baos = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos)
            return Base64.encodeToString(baos.toByteArray(), Base64.NO_WRAP)
        }
    }
}
