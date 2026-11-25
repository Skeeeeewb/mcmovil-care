package com.example.mcmovilcare.viewmodel

import com.example.mcmovilcare.model.Equipo
import com.example.mcmovilcare.repository.EquipoRepository
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
class EquiposViewModelTest {

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
    fun `expone equipos del repositorio`() = runTest(dispatcher) {
        val repository = mockk<EquipoRepository>()
        every { repository.observarEquipos() } returns flowOf(
            listOf(Equipo(1, "Notebook", "Lenovo", "IdeaPad 3", "En diagnóstico"))
        )

        val viewModel = EquiposViewModel(repository)

        assertEquals(1, viewModel.uiState.value.equipos.size)
        assertEquals("Notebook", viewModel.uiState.value.equipos[0].nombre)
    }
}
