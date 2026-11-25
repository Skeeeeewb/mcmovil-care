package com.example.mcmovilcare.repository

import com.example.mcmovilcare.model.Equipo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class EquipoRepository {

    private val equipos = MutableStateFlow(
        listOf(
            Equipo(
                id = 1,
                nombre = "Notebook personal",
                marca = "Lenovo",
                modelo = "IdeaPad 3",
                estado = "En diagnóstico"
            ),
            Equipo(
                id = 2,
                nombre = "Teléfono",
                marca = "Samsung",
                modelo = "Galaxy A54",
                estado = "En reparación"
            ),
            Equipo(
                id = 3,
                nombre = "Tablet",
                marca = "Samsung",
                modelo = "Galaxy Tab A8",
                estado = "Disponible para retiro"
            )
        )
    )

    fun observarEquipos(): Flow<List<Equipo>> = equipos.asStateFlow()

    fun observarEquipo(id: Int): Flow<Equipo?> =
        equipos.map { lista -> lista.firstOrNull { it.id == id } }
}
