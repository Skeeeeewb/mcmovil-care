
package com.example.mcmovilcare

object ValidacionSolicitud {

    fun validarDescripcion(descripcion: String): String? {

        if (descripcion.isBlank()) {
            return "Debes describir el problema."
        }

        if (descripcion.trim().length < 10) {
            return "La descripción debe tener al menos 10 caracteres."
        }

        return null
    }
}
