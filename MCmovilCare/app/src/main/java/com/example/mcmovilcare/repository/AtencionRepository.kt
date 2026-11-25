package com.example.mcmovilcare.repository

import com.example.mcmovilcare.model.Atencion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class AtencionRepository {

    private val atenciones = MutableStateFlow(
        listOf(
            Atencion(
                id = 1001,
                equipoId = 1,
                estado = "En diagnóstico",
                diagnostico = "Posible falla de almacenamiento",
                cotizacion = 45000
            ),
            Atencion(
                id = 1002,
                equipoId = 2,
                estado = "En reparación",
                diagnostico = "Cambio de pantalla",
                cotizacion = 85000
            ),
            Atencion(
                id = 1003,
                equipoId = 3,
                estado = "Finalizada",
                diagnostico = "Revisión general completada",
                cotizacion = 20000
            )
        )
    )

    fun observarAtenciones(): Flow<List<Atencion>> = atenciones.asStateFlow()

    fun observarAtencion(id: Int): Flow<Atencion?> =
        atenciones.map { lista -> lista.firstOrNull { it.id == id } }
}
