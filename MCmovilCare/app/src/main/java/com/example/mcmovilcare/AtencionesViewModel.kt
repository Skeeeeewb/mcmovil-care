package com.example.mcmovilcare

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AtencionesViewModel : ViewModel() {

    private val _decisiones =
        MutableStateFlow<Map<Int, DecisionCotizacion>>(emptyMap())

    val decisiones: StateFlow<Map<Int, DecisionCotizacion>> =
        _decisiones.asStateFlow()

    fun aceptarCotizacion(atencionId: Int) {
        _decisiones.value = _decisiones.value +
                (atencionId to DecisionCotizacion.ACEPTADA)
    }

    fun rechazarCotizacion(atencionId: Int) {
        _decisiones.value = _decisiones.value +
                (atencionId to DecisionCotizacion.RECHAZADA)
    }

    fun obtenerDecision(atencionId: Int): DecisionCotizacion {
        return _decisiones.value[atencionId]
            ?: DecisionCotizacion.PENDIENTE
    }
}
