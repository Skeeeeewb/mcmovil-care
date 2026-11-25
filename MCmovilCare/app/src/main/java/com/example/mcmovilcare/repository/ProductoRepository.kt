package com.example.mcmovilcare.repository

import com.example.mcmovilcare.model.Producto
import com.example.mcmovilcare.model.toProducto

class ProductoRepository(
    private val api: DummyJsonApi = ApiClient.dummyJsonApi
) {

    suspend fun obtenerProducto(id: Int): Producto? = runCatching {
        api.obtenerProducto(id).toProducto()
    }.getOrNull()
}
