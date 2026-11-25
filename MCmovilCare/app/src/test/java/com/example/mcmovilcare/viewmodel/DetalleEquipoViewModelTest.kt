package com.example.mcmovilcare.viewmodel

import com.example.mcmovilcare.model.Equipo
import com.example.mcmovilcare.model.Producto
import com.example.mcmovilcare.repository.EquipoRepository
import com.example.mcmovilcare.repository.ProductoRepository
import io.mockk.coEvery
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
class DetalleEquipoViewModelTest {

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
    fun `carga equipo y producto de referencia`() = runTest(dispatcher) {
        val equipoRepository = mockk<EquipoRepository>()
        val productoRepository = mockk<ProductoRepository>()
        every { equipoRepository.observarEquipo(1) } returns flowOf(
            Equipo(1, "Notebook", "Lenovo", "IdeaPad 3", "En diagnóstico")
        )
        coEvery { productoRepository.obtenerProducto(1) } returns Producto(
            "Essence Mascara", "Essence", 9.99, "https://cdn.dummyjson.com/imagen.jpg"
        )

        val viewModel = DetalleEquipoViewModel(equipoRepository, productoRepository)
        viewModel.cargarEquipo(1)

        val estado = viewModel.uiState.value
        assertEquals("Notebook", estado.equipo?.nombre)
        assertEquals("Essence", estado.producto?.marca)
        assertEquals(9.99, estado.producto?.precio ?: 0.0, 0.01)
    }
}
