package com.example.mcmovilcare.model

object ValidacionSolicitud {

    fun validarDescripcion(descripcion: String): String? {
        val texto = descripcion.trim()

        return when {
            texto.isEmpty() -> "Debes describir el problema"
            texto.length < 10 -> "La descripción debe tener al menos 10 caracteres"
            else -> null
        }
    }
}
