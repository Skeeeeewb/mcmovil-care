package com.example.mcmovilcare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mcmovilcare.model.Equipo
import com.example.mcmovilcare.repository.EquipoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EquiposUiState(
    val equipos: List<Equipo> = emptyList()
)

class EquiposViewModel(
    private val repository: EquipoRepository = EquipoRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(EquiposUiState())
    val uiState: StateFlow<EquiposUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observarEquipos().collect { equipos ->
                _uiState.update { it.copy(equipos = equipos) }
            }
        }
    }
}
