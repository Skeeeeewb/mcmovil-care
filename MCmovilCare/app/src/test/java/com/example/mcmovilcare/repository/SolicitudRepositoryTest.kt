package com.example.mcmovilcare.repository

import com.example.mcmovilcare.model.Solicitud
import com.example.mcmovilcare.model.SolicitudDao
import com.example.mcmovilcare.model.SolicitudDto
import com.example.mcmovilcare.model.SolicitudEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException

class SolicitudRepositoryTest {

    private val dao = mockk<SolicitudDao>()
    private val api = mockk<McmovilApi>()

    @Test
    fun `sincroniza solicitudes del microservicio con Room`() = runTest {
        coEvery { api.listarSolicitudes() } returns listOf(
            SolicitudDto(5, 1, "Revisión del equipo completo", "Pendiente")
        )
        coEvery { dao.sincronizar(any()) } returns Unit

        SolicitudRepository(dao, api).sincronizar()

        coVerify { dao.sincronizar(match { it.single().id == 5 }) }
    }

    @Test
    fun `guarda solicitud con el id devuelto por el microservicio`() = runTest {
        coEvery { api.crearSolicitud(any()) } returns SolicitudDto(
            8, 1, "Revisión del equipo completo", "Pendiente"
        )
        coEvery { dao.insertar(any()) } returns 8L
        val solicitud = Solicitud(0, 1, "Revisión del equipo completo")

        val id = SolicitudRepository(dao, api).guardar(solicitud)

        assertEquals(8L, id)
        coVerify { dao.insertar(match { it.id == 8 }) }
    }

    @Test
    fun `guarda localmente si falla el microservicio`() = runTest {
        coEvery { api.crearSolicitud(any()) } throws IOException("Sin conexión")
        coEvery { dao.insertar(any()) } returns 3L

        val id = SolicitudRepository(dao, api).guardar(Solicitud(0, 1, "Revisión del equipo"))

        assertEquals(3L, id)
        coVerify { dao.insertar(match { it.descripcion == "Revisión del equipo" }) }
    }

    @Test
    fun `sincronizacion fallida no modifica Room`() = runTest {
        coEvery { api.listarSolicitudes() } throws IOException("Sin conexión")

        SolicitudRepository(dao, api).sincronizar()

        coVerify(exactly = 0) { dao.sincronizar(any()) }
    }

    @Test
    fun `mapea entidades de Room a dominio`() = runTest {
        coEvery { dao.obtenerTodas() } returns flowOf(
            listOf(SolicitudEntity(4, 1, "Revisión del equipo", "Pendiente"))
        )

        val solicitudes = SolicitudRepository(dao, api).observarSolicitudes().first()

        assertEquals("Revisión del equipo", solicitudes[0].descripcion)
    }

    @Test
    fun `devuelve lista vacia cuando no hay solicitudes locales`() = runTest {
        coEvery { dao.obtenerTodas() } returns flowOf(emptyList())

        val solicitudes = SolicitudRepository(dao, api).observarSolicitudes().first()

        assertEquals(0, solicitudes.size)
    }
}
