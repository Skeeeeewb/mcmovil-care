
package com.example.mcmovilcare

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SolicitudUiState(
    val descripcion: String = "",
    val errorDescripcion: String? = null,
    val solicitudValida: Boolean = false
)

class SolicitudViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SolicitudUiState())

    val uiState: StateFlow<SolicitudUiState> =
        _uiState.asStateFlow()

    fun actualizarDescripcion(nuevaDescripcion: String) {
        _uiState.value = _uiState.value.copy(
            descripcion = nuevaDescripcion,
            errorDescripcion = null,
            solicitudValida = false
        )
    }

    fun validarSolicitud() {
        val descripcion = _uiState.value.descripcion.trim()

        val error = when {
            descripcion.isEmpty() ->
                "Debes describir el problema"

            descripcion.length < 10 ->
                "La descripción debe tener al menos 10 caracteres"

            else -> null
        }

        _uiState.value = _uiState.value.copy(
            errorDescripcion = error,
            solicitudValida = error == null
        )
    }
}


