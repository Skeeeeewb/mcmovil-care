package com.example.mcmovilcare.repository

import com.example.mcmovilcare.model.Solicitud
import com.example.mcmovilcare.model.SolicitudDao
import com.example.mcmovilcare.model.SolicitudEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SolicitudRepository(private val dao: SolicitudDao) {

    fun observarSolicitudes(): Flow<List<Solicitud>> =
        dao.obtenerTodas().map { lista -> lista.map { it.toDomain() } }

    suspend fun guardar(solicitud: Solicitud): Long =
        dao.insertar(solicitud.toEntity())
}

private fun SolicitudEntity.toDomain(): Solicitud = Solicitud(
    id = id,
    equipoId = equipoId,
    descripcion = descripcion,
    estado = estado
)

private fun Solicitud.toEntity(): SolicitudEntity = SolicitudEntity(
    id = id,
    equipoId = equipoId,
    descripcion = descripcion,
    estado = estado
)
