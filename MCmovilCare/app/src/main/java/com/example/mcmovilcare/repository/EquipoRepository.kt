package com.example.mcmovilcare.repository

import com.example.mcmovilcare.model.Equipo
import com.example.mcmovilcare.model.toEquipo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class EquipoRepository(
    private val api: McmovilApi = ApiClient.mcmovilApi
) {

    fun observarEquipos(): Flow<List<Equipo>> = flow {
        emit(api.listarEquipos().map { it.toEquipo() })
    }.catch { emit(EQUIPOS_RESPALDO) }

    fun observarEquipo(id: Int): Flow<Equipo?> = flow<Equipo?> {
        emit(api.obtenerEquipo(id).toEquipo())
    }.catch { emit(EQUIPOS_RESPALDO.firstOrNull { it.id == id }) }

    companion object {
        private val EQUIPOS_RESPALDO = listOf(
            Equipo(1, "Notebook personal", "Lenovo", "IdeaPad 3", "En diagnóstico"),
            Equipo(2, "Teléfono", "Samsung", "Galaxy A54", "En reparación"),
            Equipo(3, "Tablet", "Samsung", "Galaxy Tab A8", "Disponible para retiro")
        )
    }
}
