package com.example.mcmovilcare.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mcmovilcare.model.AppDatabase
import com.example.mcmovilcare.model.Atencion
import com.example.mcmovilcare.model.DecisionCotizacion
import com.example.mcmovilcare.repository.AtencionRepository
import com.example.mcmovilcare.repository.DecisionCotizacionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetalleAtencionUiState(
    val atencion: Atencion? = null,
    val decision: DecisionCotizacion = DecisionCotizacion.PENDIENTE,
    val mostrarDiagnostico: Boolean = true,
    val imagenUri: String? = null,
    val permisoGaleriaDenegado: Boolean = false
)

sealed interface DetalleAtencionUiEvent {
    data object AlternarDiagnostico : DetalleAtencionUiEvent
    data class ImagenSeleccionada(val uri: String) : DetalleAtencionUiEvent
    data object PermisoGaleriaDenegado : DetalleAtencionUiEvent
    data object AceptarCotizacion : DetalleAtencionUiEvent
    data object RechazarCotizacion : DetalleAtencionUiEvent
}

class DetalleAtencionViewModel(
    application: Application,
    private val atencionRepository: AtencionRepository = AtencionRepository(),
    private val decisionRepository: DecisionCotizacionRepository = DecisionCotizacionRepository(
        AppDatabase.getInstance(application).decisionCotizacionDao()
    )
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(DetalleAtencionUiState())
    val uiState: StateFlow<DetalleAtencionUiState> = _uiState.asStateFlow()

    private var atencionId: Int = 0

    fun cargarAtencion(id: Int) {
        atencionId = id

        viewModelScope.launch {
            atencionRepository.observarAtencion(id).collect { atencion ->
                _uiState.update { it.copy(atencion = atencion) }
            }
        }
        viewModelScope.launch {
            decisionRepository.sincronizar(id)
        }
        viewModelScope.launch {
            decisionRepository.observarDecision(id).collect { decision ->
                _uiState.update { it.copy(decision = decision) }
            }
        }
    }

    fun onEvent(event: DetalleAtencionUiEvent) {
        when (event) {
            DetalleAtencionUiEvent.AlternarDiagnostico -> _uiState.update {
                it.copy(mostrarDiagnostico = !it.mostrarDiagnostico)
            }

            is DetalleAtencionUiEvent.ImagenSeleccionada -> _uiState.update {
                it.copy(imagenUri = event.uri, permisoGaleriaDenegado = false)
            }

            DetalleAtencionUiEvent.PermisoGaleriaDenegado -> _uiState.update {
                it.copy(permisoGaleriaDenegado = true)
            }

            DetalleAtencionUiEvent.AceptarCotizacion ->
                guardarDecision(DecisionCotizacion.ACEPTADA)

            DetalleAtencionUiEvent.RechazarCotizacion ->
                guardarDecision(DecisionCotizacion.RECHAZADA)
        }
    }

    private fun guardarDecision(decision: DecisionCotizacion) {
        viewModelScope.launch {
            decisionRepository.guardarDecision(atencionId, decision)
        }
    }
}
