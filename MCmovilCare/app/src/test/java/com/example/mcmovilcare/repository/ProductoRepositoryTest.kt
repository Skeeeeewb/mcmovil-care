package com.example.mcmovilcare.repository

import com.example.mcmovilcare.model.ProductoDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.io.IOException

class ProductoRepositoryTest {

    private val api = mockk<DummyJsonApi>()

    @Test
    fun `obtiene referencia electronica segun el equipo`() = runTest {
        coEvery { api.obtenerProducto(81) } returns ProductoDto(
            "Lenovo Yoga 920", "Lenovo", 1099.99, "https://cdn.dummyjson.com/lenovo.jpg"
        )

        val producto = ProductoRepository(api).obtenerProducto(1)

        assertEquals("Lenovo Yoga 920", producto?.titulo)
        assertEquals("Lenovo", producto?.marca)
        assertEquals("https://cdn.dummyjson.com/lenovo.jpg", producto?.imagen)
    }

    @Test
    fun `devuelve nulo si el equipo no tiene referencia configurada`() = runTest {
        assertNull(ProductoRepository(api).obtenerProducto(99))
    }

    @Test
    fun `devuelve nulo si falla DummyJSON`() = runTest {
        coEvery { api.obtenerProducto(81) } throws IOException("Sin conexión")

        assertNull(ProductoRepository(api).obtenerProducto(1))
    }
}
