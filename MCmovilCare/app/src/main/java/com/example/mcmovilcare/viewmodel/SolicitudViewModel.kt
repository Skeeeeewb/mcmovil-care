package com.example.mcmovilcare.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mcmovilcare.model.AppDatabase
import com.example.mcmovilcare.model.Equipo
import com.example.mcmovilcare.model.Solicitud
import com.example.mcmovilcare.model.ValidacionSolicitud
import com.example.mcmovilcare.repository.EquipoRepository
import com.example.mcmovilcare.repository.FotoRepository
import com.example.mcmovilcare.repository.SolicitudRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface MensajeSolicitud {
    data object Guardada : MensajeSolicitud
    data object Error : MensajeSolicitud
}

data class SolicitudUiState(
    val equipo: Equipo? = null,
    val descripcion: String = "",
    val errorDescripcion: String? = null,
    val guardando: Boolean = false,
    val mensaje: MensajeSolicitud? = null,
    val foto: Bitmap? = null,
    val fotoRuta: String? = null,
    val permisoCamaraDenegado: Boolean = false,
    val solicitudes: List<Solicitud> = emptyList()
)

sealed interface SolicitudUiEvent {
    data class DescripcionCambiada(val texto: String) : SolicitudUiEvent
    data class FotoCapturada(val bitmap: Bitmap) : SolicitudUiEvent
    data object FotoEliminada : SolicitudUiEvent
    data object PermisoCamaraDenegado : SolicitudUiEvent
    data object Guardar : SolicitudUiEvent
}

class SolicitudViewModel(
    application: Application,
    private val solicitudRepository: SolicitudRepository = SolicitudRepository(
        AppDatabase.getInstance(application).solicitudDao()
    ),
    private val equipoRepository: EquipoRepository = EquipoRepository(),
    private val fotoRepository: FotoRepository = FotoRepository(application)
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(SolicitudUiState())
    val uiState: StateFlow<SolicitudUiState> = _uiState.asStateFlow()

    private var equipoId: Int = 0
    private var todasLasSolicitudes: List<Solicitud> = emptyList()

    init {
        viewModelScope.launch {
            solicitudRepository.sincronizar()
        }
        viewModelScope.launch {
            solicitudRepository.observarSolicitudes().collect { solicitudes ->
                todasLasSolicitudes = solicitudes
                aplicarFiltro()
            }
        }
    }

    fun cargarEquipo(id: Int) {
        equipoId = id
        aplicarFiltro()

        viewModelScope.launch {
            equipoRepository.observarEquipo(id).collect { equipo ->
                _uiState.update { it.copy(equipo = equipo) }
            }
        }
    }

    fun onEvent(event: SolicitudUiEvent) {
        when (event) {
            is SolicitudUiEvent.DescripcionCambiada -> _uiState.update {
                it.copy(
                    descripcion = event.texto,
                    errorDescripcion = null,
                    mensaje = null
                )
            }

            is SolicitudUiEvent.FotoCapturada -> {
                val ruta = fotoRepository.guardar(event.bitmap)
                _uiState.update {
                    it.copy(
                        foto = event.bitmap,
                        fotoRuta = ruta,
                        permisoCamaraDenegado = false
                    )
                }
            }

            SolicitudUiEvent.FotoEliminada -> _uiState.update {
                fotoRepository.eliminar(it.fotoRuta)
                it.copy(foto = null, fotoRuta = null)
            }

            SolicitudUiEvent.PermisoCamaraDenegado -> _uiState.update {
                it.copy(permisoCamaraDenegado = true)
            }

            SolicitudUiEvent.Guardar -> guardarSolicitud()
        }
    }

    private fun aplicarFiltro() {
        _uiState.update {
            it.copy(solicitudes = todasLasSolicitudes.filter { s -> s.equipoId == equipoId })
        }
    }

    private fun guardarSolicitud() {
        val estado = _uiState.value
        if (estado.guardando || estado.equipo == null) return

        val error = ValidacionSolicitud.validarDescripcion(estado.descripcion)
        _uiState.update { it.copy(errorDescripcion = error, mensaje = null) }
        if (error != null) return

        _uiState.update { it.copy(guardando = true) }

        viewModelScope.launch {
            val solicitud = Solicitud(
                id = 0,
                equipoId = equipoId,
                descripcion = estado.descripcion.trim(),
                estado = "Pendiente",
                fotoRuta = estado.fotoRuta
            )

            runCatching { solicitudRepository.guardar(solicitud) }
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            descripcion = "",
                            errorDescripcion = null,
                            guardando = false,
                            foto = null,
                            fotoRuta = null,
                            mensaje = MensajeSolicitud.Guardada
                        )
                    }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(
                            guardando = false,
                            mensaje = MensajeSolicitud.Error
                        )
                    }
                }
        }
    }
}
