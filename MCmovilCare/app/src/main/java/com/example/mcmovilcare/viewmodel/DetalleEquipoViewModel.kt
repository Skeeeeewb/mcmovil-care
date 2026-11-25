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

data class DetalleEquipoUiState(
    val equipo: Equipo? = null
)

class DetalleEquipoViewModel : ViewModel() {

    private val repository = EquipoRepository()

    private val _uiState = MutableStateFlow(DetalleEquipoUiState())
    val uiState: StateFlow<DetalleEquipoUiState> = _uiState.asStateFlow()

    fun cargarEquipo(equipoId: Int) {
        viewModelScope.launch {
            repository.observarEquipo(equipoId).collect { equipo ->
                _uiState.update { it.copy(equipo = equipo) }
            }
        }
    }
}
