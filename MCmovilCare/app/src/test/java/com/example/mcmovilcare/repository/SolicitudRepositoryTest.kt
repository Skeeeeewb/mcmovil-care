package com.example.mcmovilcare.repository

import com.example.mcmovilcare.model.Solicitud
import com.example.mcmovilcare.model.SolicitudDao
import com.example.mcmovilcare.model.SolicitudDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

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
}
