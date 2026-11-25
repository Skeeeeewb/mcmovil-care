package com.example.mcmovilcare.viewmodel

import android.app.Application
import android.graphics.Bitmap
import com.example.mcmovilcare.model.Equipo
import com.example.mcmovilcare.model.Solicitud
import com.example.mcmovilcare.repository.EquipoRepository
import com.example.mcmovilcare.repository.SolicitudRepository
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
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class SolicitudViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private val application = mockk<Application>(relaxed = true)
    private val solicitudRepository = mockk<SolicitudRepository>()
    private val equipoRepository = mockk<EquipoRepository>()

    @Before
    fun preparar() {
        Dispatchers.setMain(dispatcher)
        coEvery { solicitudRepository.sincronizar() } returns Unit
        coEvery { solicitudRepository.guardar(any()) } returns 7L
        every { solicitudRepository.observarSolicitudes() } returns flowOf(
            listOf(
                Solicitud(1, 1, "Revisión del notebook", "Pendiente"),
                Solicitud(2, 2, "Cambio de pantalla", "Pendiente")
            )
        )
        every { equipoRepository.observarEquipo(1) } returns flowOf(
            Equipo(1, "Notebook", "Lenovo", "IdeaPad 3", "En diagnóstico")
        )
    }

    @After
    fun limpiar() {
        Dispatchers.resetMain()
    }

    private fun viewModel(): SolicitudViewModel {
        val viewModel = SolicitudViewModel(application, solicitudRepository, equipoRepository)
        viewModel.cargarEquipo(1)
        return viewModel
    }

    @Test
    fun `carga equipo y filtra solicitudes`() = runTest(dispatcher) {
        val viewModel = viewModel()

        assertEquals("Notebook", viewModel.uiState.value.equipo?.nombre)
        assertEquals(1, viewModel.uiState.value.solicitudes.size)
        coVerify { solicitudRepository.sincronizar() }
    }

    @Test
    fun `actualiza descripcion y foto`() = runTest(dispatcher) {
        val viewModel = viewModel()
        val foto = mockk<Bitmap>(relaxed = true)

        viewModel.onEvent(SolicitudUiEvent.DescripcionCambiada("Descripción válida"))
        viewModel.onEvent(SolicitudUiEvent.FotoCapturada(foto))
        assertEquals("Descripción válida", viewModel.uiState.value.descripcion)
        assertEquals(foto, viewModel.uiState.value.foto)

        viewModel.onEvent(SolicitudUiEvent.FotoEliminada)
        assertNull(viewModel.uiState.value.foto)
    }

    @Test
    fun `valida descripcion corta antes de guardar`() = runTest(dispatcher) {
        val viewModel = viewModel()
        viewModel.onEvent(SolicitudUiEvent.DescripcionCambiada("Corta"))

        viewModel.onEvent(SolicitudUiEvent.Guardar)

        assertEquals(
            "La descripción debe tener al menos 10 caracteres",
            viewModel.uiState.value.errorDescripcion
        )
        coVerify(exactly = 0) { solicitudRepository.guardar(any()) }
    }

    @Test
    fun `guarda solicitud valida`() = runTest(dispatcher) {
        val viewModel = viewModel()
        viewModel.onEvent(SolicitudUiEvent.DescripcionCambiada("Descripción válida"))

        viewModel.onEvent(SolicitudUiEvent.Guardar)

        assertEquals("", viewModel.uiState.value.descripcion)
        assertEquals(MensajeSolicitud.Guardada, viewModel.uiState.value.mensaje)
        coVerify { solicitudRepository.guardar(match { it.descripcion == "Descripción válida" }) }
    }

    @Test
    fun `muestra error si falla el guardado`() = runTest(dispatcher) {
        coEvery { solicitudRepository.guardar(any()) } throws IOException("Sin conexión")
        val viewModel = viewModel()
        viewModel.onEvent(SolicitudUiEvent.DescripcionCambiada("Descripción válida"))

        viewModel.onEvent(SolicitudUiEvent.Guardar)

        assertEquals(MensajeSolicitud.Error, viewModel.uiState.value.mensaje)
        assertEquals(false, viewModel.uiState.value.guardando)
    }
}
