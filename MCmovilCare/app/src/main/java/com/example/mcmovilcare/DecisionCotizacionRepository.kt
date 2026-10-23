package com.example.mcmovilcare

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DecisionCotizacionRepository(
    private val dao: DecisionCotizacionDao
) {

    suspend fun guardarDecision(
        atencionId: Int,
        decision: DecisionCotizacion
    ) {
        val entidad = DecisionCotizacionEntity(
            atencionId = atencionId,
            decision = decision.name
        )

        dao.guardarDecision(entidad)
    }

    fun observarDecision(
        atencionId: Int
    ): Flow<DecisionCotizacion> {
        return dao.observarDecision(atencionId).map { entidad ->
            entidad?.decision
                ?.let { valor ->
                    enumValues<DecisionCotizacion>()
                        .firstOrNull { it.name == valor }
                }
                ?: DecisionCotizacion.PENDIENTE
        }
    }
}