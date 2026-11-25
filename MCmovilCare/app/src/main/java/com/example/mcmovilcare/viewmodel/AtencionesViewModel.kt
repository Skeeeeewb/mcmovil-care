package com.example.mcmovilcare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mcmovilcare.model.Atencion
import com.example.mcmovilcare.repository.AtencionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AtencionesUiState(
    val atenciones: List<Atencion> = emptyList()
)

class AtencionesViewModel(
    private val repository: AtencionRepository = AtencionRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AtencionesUiState())
    val uiState: StateFlow<AtencionesUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observarAtenciones().collect { atenciones ->
                _uiState.update { it.copy(atenciones = atenciones) }
            }
        }
    }
}
