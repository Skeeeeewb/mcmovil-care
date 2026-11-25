package com.example.mcmovilcare.viewmodel

import android.app.Application
import com.example.mcmovilcare.model.Atencion
import com.example.mcmovilcare.model.DecisionCotizacion
import com.example.mcmovilcare.repository.AtencionRepository
import com.example.mcmovilcare.repository.DecisionCotizacionRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetalleAtencionViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private val application = mockk<Application>(relaxed = true)
    private val atencionRepository = mockk<AtencionRepository>()
    private val decisionRepository = mockk<DecisionCotizacionRepository>()

    @Before
    fun preparar() {
        Dispatchers.setMain(dispatcher)
        every { atencionRepository.observarAtencion(1001) } returns flowOf(
            Atencion(1001, 1, "En diagnóstico", "Falla de almacenamiento", 45000)
        )
        every { decisionRepository.observarDecision(1001) } returns flowOf(DecisionCotizacion.PENDIENTE)
        coEvery { decisionRepository.sincronizar(1001) } returns Unit
        coEvery { decisionRepository.guardarDecision(any(), any()) } returns Unit
    }

    @After
    fun limpiar() {
        Dispatchers.resetMain()
    }

    private fun viewModel(): DetalleAtencionViewModel {
        val viewModel = DetalleAtencionViewModel(application, atencionRepository, decisionRepository)
        viewModel.cargarAtencion(1001)
        return viewModel
    }

    @Test
    fun `carga atencion y decision sincronizada`() = runTest(dispatcher) {
        val viewModel = viewModel()

        assertEquals(1001, viewModel.uiState.value.atencion?.id)
        assertEquals(DecisionCotizacion.PENDIENTE, viewModel.uiState.value.decision)
        coVerify { decisionRepository.sincronizar(1001) }
    }

    @Test
    fun `alterna diagnostico y guarda imagen`() = runTest(dispatcher) {
        val viewModel = viewModel()

        viewModel.onEvent(DetalleAtencionUiEvent.AlternarDiagnostico)
        assertFalse(viewModel.uiState.value.mostrarDiagnostico)

        viewModel.onEvent(DetalleAtencionUiEvent.ImagenSeleccionada("content://evidencia"))
        assertEquals("content://evidencia", viewModel.uiState.value.imagenUri)
    }

    @Test
    fun `acepta y rechaza cotizacion`() = runTest(dispatcher) {
        val viewModel = viewModel()

        viewModel.onEvent(DetalleAtencionUiEvent.AceptarCotizacion)
        viewModel.onEvent(DetalleAtencionUiEvent.RechazarCotizacion)

        coVerify { decisionRepository.guardarDecision(1001, DecisionCotizacion.ACEPTADA) }
        coVerify { decisionRepository.guardarDecision(1001, DecisionCotizacion.RECHAZADA) }
    }
}
