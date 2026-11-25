package com.example.mcmovilcare.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ValidacionSolicitudTest {

    @Test
    fun `descripcion corta muestra error original`() {
        assertEquals(
            "La descripción debe tener al menos 10 caracteres",
            ValidacionSolicitud.validarDescripcion("Muy corta")
        )
    }

    @Test
    fun `descripcion valida no muestra error`() {
        assertNull(ValidacionSolicitud.validarDescripcion("Descripción válida"))
    }
}
