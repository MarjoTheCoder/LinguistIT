package com.example.linguistit.utils

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

object DateUtils {
    fun obtenerFechaFormateada(timestamp: Timestamp?): String {
        if (timestamp == null) return ""
        val milisegundos = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return sdf.format(milisegundos)
    }
}