package com.example.mcmovilcare.repository

import com.example.mcmovilcare.model.DecisionCotizacion
import com.example.mcmovilcare.model.DecisionCotizacionDao
import com.example.mcmovilcare.model.DecisionCotizacionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DecisionCotizacionRepository(private val dao: DecisionCotizacionDao) {

    fun observarDecision(atencionId: Int): Flow<DecisionCotizacion> =
        dao.observarDecision(atencionId).map { entidad ->
            entidad?.toDomain() ?: DecisionCotizacion.PENDIENTE
        }

    suspend fun guardarDecision(atencionId: Int, decision: DecisionCotizacion) {
        dao.guardarDecision(
            DecisionCotizacionEntity(
                atencionId = atencionId,
                decision = decision.name
            )
        )
    }
}

private fun DecisionCotizacionEntity.toDomain(): DecisionCotizacion =
    runCatching { DecisionCotizacion.valueOf(decision) }
        .getOrDefault(DecisionCotizacion.PENDIENTE)
