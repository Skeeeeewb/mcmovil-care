package com.example.mcmovilcare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mcmovilcare.model.Equipo
import com.example.mcmovilcare.model.Producto
import com.example.mcmovilcare.repository.EquipoRepository
import com.example.mcmovilcare.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetalleEquipoUiState(
    val equipo: Equipo? = null,
    val producto: Producto? = null
)

class DetalleEquipoViewModel(
    private val repository: EquipoRepository = EquipoRepository(),
    private val productoRepository: ProductoRepository = ProductoRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetalleEquipoUiState())
    val uiState: StateFlow<DetalleEquipoUiState> = _uiState.asStateFlow()

    fun cargarEquipo(equipoId: Int) {
        viewModelScope.launch {
            repository.observarEquipo(equipoId).collect { equipo ->
                _uiState.update { it.copy(equipo = equipo) }
            }
        }
        viewModelScope.launch {
            _uiState.update { it.copy(producto = productoRepository.obtenerProducto(equipoId)) }
        }
    }
}
