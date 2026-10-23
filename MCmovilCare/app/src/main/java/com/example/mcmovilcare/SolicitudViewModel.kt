
package com.example.mcmovilcare

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SolicitudUiState(
    val descripcion: String = "",
    val errorDescripcion: String? = null,
    val solicitudValida: Boolean = false,
    val guardando: Boolean = false,
    val mensaje: String? = null,
    val solicitudes: List<SolicitudEntity> = emptyList()
)

class SolicitudViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val solicitudDao = AppDatabase
        .getInstance(application)
        .solicitudDao()

    private val _uiState = MutableStateFlow(SolicitudUiState())

    val uiState: StateFlow<SolicitudUiState> =
        _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            solicitudDao.obtenerTodas().collect { lista ->
                _uiState.value = _uiState.value.copy(
                    solicitudes = lista
                )
            }
        }
    }

    fun actualizarDescripcion(nuevaDescripcion: String) {
        _uiState.value = _uiState.value.copy(
            descripcion = nuevaDescripcion,
            errorDescripcion = null,
            solicitudValida = false,
            mensaje = null
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

    fun guardarSolicitud(equipoId: Int) {
        if (_uiState.value.guardando) return

        validarSolicitud()

        if (!_uiState.value.solicitudValida) return

        val descripcion = _uiState.value.descripcion.trim()

        _uiState.value = _uiState.value.copy(
            guardando = true,
            mensaje = null
        )

        viewModelScope.launch {
            try {
                solicitudDao.insertar(
                    SolicitudEntity(
                        equipoId = equipoId,
                        descripcion = descripcion,
                        estado = "Pendiente"
                    )
                )

                _uiState.value = _uiState.value.copy(
                    descripcion = "",
                    errorDescripcion = null,
                    solicitudValida = false,
                    guardando = false,
                    mensaje = "Solicitud registrada correctamente"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    guardando = false,
                    mensaje = "No se pudo guardar la solicitud"
                )
            }
        }
    }
}
