package com.jfh.drivy.pacadapter.control

import android.content.Context
import com.jfh.drivy.pacadapter.abstraction.ImageAdapter

class Base64ImageAdapter(context: Context) : ImageAdapter {
    private val converter = BitmapBase64Converter(context)

    override fun convert(uriString: String): String {
        return converter.toBase64(uriString)
    }
}
