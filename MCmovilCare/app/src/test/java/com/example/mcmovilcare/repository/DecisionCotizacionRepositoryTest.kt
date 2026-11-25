package com.example.mcmovilcare.repository

import com.example.mcmovilcare.model.DecisionCotizacion
import com.example.mcmovilcare.model.DecisionCotizacionDao
import com.example.mcmovilcare.model.DecisionCotizacionEntity
import com.example.mcmovilcare.model.DecisionRespuestaDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException

class DecisionCotizacionRepositoryTest {

    private val dao = mockk<DecisionCotizacionDao>()
    private val api = mockk<McmovilApi>()

    @Test
    fun `observa decision pendiente cuando no hay registro local`() = runTest {
        coEvery { dao.observarDecision(1001) } returns flowOf(null)

        val decision = DecisionCotizacionRepository(dao, api).observarDecision(1001).first()

        assertEquals(DecisionCotizacion.PENDIENTE, decision)
    }

    @Test
    fun `observa decision guardada localmente`() = runTest {
        coEvery { dao.observarDecision(1001) } returns flowOf(
            DecisionCotizacionEntity(1001, "ACEPTADA")
        )

        val decision = DecisionCotizacionRepository(dao, api).observarDecision(1001).first()

        assertEquals(DecisionCotizacion.ACEPTADA, decision)
    }

    @Test
    fun `mapea decision desconocida a pendiente`() = runTest {
        coEvery { dao.observarDecision(1001) } returns flowOf(
            DecisionCotizacionEntity(1001, "DESCONOCIDA")
        )

        val decision = DecisionCotizacionRepository(dao, api).observarDecision(1001).first()

        assertEquals(DecisionCotizacion.PENDIENTE, decision)
    }

    @Test
    fun `sincroniza decision del microservicio`() = runTest {
        coEvery { api.obtenerDecision(1001) } returns DecisionRespuestaDto(1001, "RECHAZADA")
        coEvery { dao.guardarDecision(any()) } returns Unit

        DecisionCotizacionRepository(dao, api).sincronizar(1001)

        coVerify { dao.guardarDecision(match { it.decision == "RECHAZADA" }) }
    }

    @Test
    fun `sincronizacion fallida no escribe en Room`() = runTest {
        coEvery { api.obtenerDecision(1001) } throws IOException("Sin conexión")

        DecisionCotizacionRepository(dao, api).sincronizar(1001)

        coVerify(exactly = 0) { dao.guardarDecision(any()) }
    }

    @Test
    fun `guarda decision remota y local`() = runTest {
        coEvery { api.guardarDecision(1001, any()) } returns DecisionRespuestaDto(1001, "ACEPTADA")
        coEvery { dao.guardarDecision(any()) } returns Unit

        DecisionCotizacionRepository(dao, api).guardarDecision(1001, DecisionCotizacion.ACEPTADA)

        coVerify { api.guardarDecision(1001, any()) }
        coVerify { dao.guardarDecision(match { it.decision == "ACEPTADA" }) }
    }

    @Test
    fun `guarda localmente si falla el microservicio`() = runTest {
        coEvery { api.guardarDecision(1001, any()) } throws IOException("Sin conexión")
        coEvery { dao.guardarDecision(any()) } returns Unit

        DecisionCotizacionRepository(dao, api).guardarDecision(1001, DecisionCotizacion.RECHAZADA)

        coVerify { dao.guardarDecision(match { it.decision == "RECHAZADA" }) }
    }
}
