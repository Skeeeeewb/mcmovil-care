package com.example.mcmovilcare.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File

class FotoRepository(context: Context) {

    private val contexto = context.applicationContext
    private val carpeta by lazy {
        File(contexto.filesDir, "evidencias").apply { mkdirs() }
    }

    fun guardar(bitmap: Bitmap): String? = runCatching {
        val archivo = File(carpeta, "solicitud_${System.currentTimeMillis()}.jpg")
        archivo.outputStream().use { bitmap.compress(Bitmap.CompressFormat.JPEG, 85, it) }
        archivo.absolutePath
    }.getOrNull()

    fun guardar(uri: String): String? = runCatching {
        val archivo = File(carpeta, "evidencia_${System.currentTimeMillis()}.jpg")
        val entrada = contexto.contentResolver.openInputStream(Uri.parse(uri)) ?: return null
        entrada.use { origen ->
            archivo.outputStream().use { destino -> origen.copyTo(destino) }
        }
        archivo.absolutePath
    }.getOrNull()

    fun cargar(ruta: String?): Bitmap? = ruta?.let {
        runCatching { BitmapFactory.decodeFile(it) }.getOrNull()
    }

    fun eliminar(ruta: String?) {
        ruta?.let { runCatching { File(it).delete() } }
    }
}
