package com.example.mcmovilcare.repository

import com.example.mcmovilcare.model.AtencionDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.io.IOException

class AtencionRepositoryTest {

    private val api = mockk<McmovilApi>()

    @Test
    fun `obtiene atenciones del microservicio`() = runTest {
        coEvery { api.listarAtenciones() } returns listOf(
            AtencionDto(1001, 1, "En diagnóstico", "Falla de almacenamiento", 45000)
        )

        val atenciones = AtencionRepository(api).observarAtenciones().first()

        assertEquals(1001, atenciones[0].id)
        assertEquals("Falla de almacenamiento", atenciones[0].diagnostico)
    }

    @Test
    fun `obtiene una atencion por id`() = runTest {
        coEvery { api.obtenerAtencion(1002) } returns AtencionDto(
            1002, 2, "En reparación", "Cambio de pantalla", 85000
        )

        val atencion = AtencionRepository(api).observarAtencion(1002).first()

        assertEquals("Cambio de pantalla", atencion?.diagnostico)
    }

    @Test
    fun `usa respaldo si falla la consulta general`() = runTest {
        coEvery { api.listarAtenciones() } throws IOException("Sin conexión")

        val atenciones = AtencionRepository(api).observarAtenciones().first()

        assertEquals(3, atenciones.size)
    }

    @Test
    fun `usa respaldo si falla la consulta por id`() = runTest {
        coEvery { api.obtenerAtencion(99) } throws IOException("Sin conexión")

        assertNull(AtencionRepository(api).observarAtencion(99).first())
    }
}
