package com.example.mcmovilcare.repository

import com.example.mcmovilcare.model.Producto
import com.example.mcmovilcare.model.toProducto

class ProductoRepository(
    private val api: DummyJsonApi = ApiClient.dummyJsonApi
) {

    suspend fun obtenerProducto(equipoId: Int): Producto? {
        val productoId = PRODUCTOS_POR_EQUIPO[equipoId] ?: return null
        return runCatching {
            api.obtenerProducto(productoId).toProducto()
        }.getOrNull()
    }

    companion object {
        // Referencias electrónicas verificadas en DummyJSON; no son los equipos del taller.
        private val PRODUCTOS_POR_EQUIPO = mapOf(
            1 to 81,  // Notebook Lenovo -> Lenovo Yoga 920
            2 to 132, // Teléfono Samsung -> Samsung Galaxy S8
            3 to 160  // Tablet Samsung -> Samsung Galaxy Tab S8 Plus
        )
    }
}
