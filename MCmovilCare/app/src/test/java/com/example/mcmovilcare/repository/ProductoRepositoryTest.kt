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
    fun `obtiene producto de DummyJSON`() = runTest {
        coEvery { api.obtenerProducto(1) } returns ProductoDto(
            "Essence Mascara", "Essence", 9.99, "https://cdn.dummyjson.com/imagen.jpg"
        )

        val producto = ProductoRepository(api).obtenerProducto(1)

        assertEquals("Essence", producto?.marca)
        assertEquals("https://cdn.dummyjson.com/imagen.jpg", producto?.imagen)
    }

    @Test
    fun `devuelve nulo si falla DummyJSON`() = runTest {
        coEvery { api.obtenerProducto(1) } throws IOException("Sin conexión")

        assertNull(ProductoRepository(api).obtenerProducto(1))
    }
}
