package com.example.mcmovilcare.repository

import com.example.mcmovilcare.model.Atencion
import com.example.mcmovilcare.model.toAtencion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class AtencionRepository(
    private val api: McmovilApi = ApiClient.mcmovilApi
) {

    fun observarAtenciones(): Flow<List<Atencion>> = flow {
        emit(api.listarAtenciones().map { it.toAtencion() })
    }.catch { emit(ATENCIONES_RESPALDO) }

    fun observarAtencion(id: Int): Flow<Atencion?> = flow<Atencion?> {
        emit(api.obtenerAtencion(id).toAtencion())
    }.catch { emit(ATENCIONES_RESPALDO.firstOrNull { it.id == id }) }

    companion object {
        private val ATENCIONES_RESPALDO = listOf(
            Atencion(1001, 1, "En diagnóstico", "Posible falla de almacenamiento", 45000),
            Atencion(1002, 2, "En reparación", "Cambio de pantalla", 85000),
            Atencion(1003, 3, "Finalizada", "Revisión general completada", 20000)
        )
    }
}
