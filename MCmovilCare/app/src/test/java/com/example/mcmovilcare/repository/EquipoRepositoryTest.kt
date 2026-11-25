package com.example.mcmovilcare.repository

import com.example.mcmovilcare.model.EquipoDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.io.IOException

class EquipoRepositoryTest {

    private val api = mockk<McmovilApi>()

    @Test
    fun `obtiene equipos del microservicio`() = runTest {
        coEvery { api.listarEquipos() } returns listOf(
            EquipoDto(1, "Notebook", "Lenovo", "IdeaPad 3", "En diagnóstico")
        )

        val equipos = EquipoRepository(api).observarEquipos().first()

        assertEquals(1, equipos.size)
        assertEquals("Notebook", equipos[0].nombre)
        assertEquals("IdeaPad 3", equipos[0].modelo)
        coVerify { api.listarEquipos() }
    }

    @Test
    fun `usa datos de respaldo si falla el microservicio`() = runTest {
        coEvery { api.listarEquipos() } throws IOException("Sin conexión")

        val equipos = EquipoRepository(api).observarEquipos().first()

        assertEquals(3, equipos.size)
        assertEquals("Notebook personal", equipos[0].nombre)
    }

    @Test
    fun `obtiene un equipo por id`() = runTest {
        coEvery { api.obtenerEquipo(2) } returns EquipoDto(2, "Teléfono", "Samsung", "Galaxy A54", "En reparación")

        val equipo = EquipoRepository(api).observarEquipo(2).first()

        assertEquals("Galaxy A54", equipo?.modelo)
    }

    @Test
    fun `usa respaldo si falla la consulta por id`() = runTest {
        coEvery { api.obtenerEquipo(99) } throws IOException("Sin conexión")

        assertNull(EquipoRepository(api).observarEquipo(99).first())
    }
}
