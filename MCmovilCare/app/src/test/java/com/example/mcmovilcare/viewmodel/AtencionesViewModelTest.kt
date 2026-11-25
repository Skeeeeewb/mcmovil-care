package com.example.mcmovilcare.viewmodel

import com.example.mcmovilcare.model.Atencion
import com.example.mcmovilcare.repository.AtencionRepository
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
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AtencionesViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun preparar() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun limpiar() {
        Dispatchers.resetMain()
    }

    @Test
    fun `expone atenciones del repositorio`() = runTest(dispatcher) {
        val repository = mockk<AtencionRepository>()
        every { repository.observarAtenciones() } returns flowOf(
            listOf(Atencion(1001, 1, "En diagnóstico", "Diagnóstico", 45000))
        )

        val viewModel = AtencionesViewModel(repository)

        assertEquals(1001, viewModel.uiState.value.atenciones[0].id)
    }
}
