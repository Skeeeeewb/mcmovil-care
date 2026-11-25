package com.example.mcmovilcare.repository

import com.example.mcmovilcare.model.DecisionCotizacion
import com.example.mcmovilcare.model.DecisionCotizacionDao
import com.example.mcmovilcare.model.DecisionCotizacionEntity
import com.example.mcmovilcare.model.DecisionDto
import com.example.mcmovilcare.model.toDecision
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DecisionCotizacionRepository(
    private val dao: DecisionCotizacionDao,
    private val api: McmovilApi = ApiClient.mcmovilApi
) {

    fun observarDecision(atencionId: Int): Flow<DecisionCotizacion> =
        dao.observarDecision(atencionId).map { entidad ->
            entidad?.toDomain() ?: DecisionCotizacion.PENDIENTE
        }

    suspend fun sincronizar(atencionId: Int) {
        runCatching { api.obtenerDecision(atencionId) }
            .onSuccess { respuesta -> guardarLocal(atencionId, respuesta.toDecision()) }
    }

    suspend fun guardarDecision(atencionId: Int, decision: DecisionCotizacion) {
        runCatching { api.guardarDecision(atencionId, DecisionDto(decision.name)) }
            .onSuccess { respuesta -> guardarLocal(atencionId, respuesta.toDecision()) }
            .onFailure { guardarLocal(atencionId, decision) }
    }

    private suspend fun guardarLocal(atencionId: Int, decision: DecisionCotizacion) {
        dao.guardarDecision(
            DecisionCotizacionEntity(
                atencionId = atencionId,
                decision = decision.name
            )
        )
    }

    private fun DecisionCotizacionEntity.toDomain(): DecisionCotizacion =
        runCatching { DecisionCotizacion.valueOf(decision) }
            .getOrDefault(DecisionCotizacion.PENDIENTE)
}
