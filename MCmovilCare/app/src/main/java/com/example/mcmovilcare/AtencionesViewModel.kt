package com.example.mcmovilcare

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AtencionesViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = DecisionCotizacionRepository(
        AppDatabase.getInstance(application).decisionCotizacionDao()
    )

    private val _decisiones =
        MutableStateFlow<Map<Int, DecisionCotizacion>>(emptyMap())

    val decisiones: StateFlow<Map<Int, DecisionCotizacion>> =
        _decisiones.asStateFlow()

    fun observarDecision(atencionId: Int) {
        viewModelScope.launch {
            repository.observarDecision(atencionId).collect { decision ->
                _decisiones.value = _decisiones.value +
                        (atencionId to decision)
            }
        }
    }

    fun aceptarCotizacion(atencionId: Int) {
        guardarDecision(atencionId, DecisionCotizacion.ACEPTADA)
    }

    fun rechazarCotizacion(atencionId: Int) {
        guardarDecision(atencionId, DecisionCotizacion.RECHAZADA)
    }

    private fun guardarDecision(
        atencionId: Int,
        decision: DecisionCotizacion
    ) {
        viewModelScope.launch {
            repository.guardarDecision(atencionId, decision)
        }
    }

    fun obtenerDecision(atencionId: Int): DecisionCotizacion {
        return _decisiones.value[atencionId]
            ?: DecisionCotizacion.PENDIENTE
    }
}