package com.example.mcmovilcare.repository

import com.example.mcmovilcare.model.Solicitud
import com.example.mcmovilcare.model.SolicitudDao
import com.example.mcmovilcare.model.SolicitudEntity
import com.example.mcmovilcare.model.toDto
import com.example.mcmovilcare.model.toSolicitud
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SolicitudRepository(
    private val dao: SolicitudDao,
    private val api: McmovilApi = ApiClient.mcmovilApi
) {

    fun observarSolicitudes(): Flow<List<Solicitud>> =
        dao.obtenerTodas().map { lista -> lista.map { it.toDomain() } }

    suspend fun sincronizar() {
        runCatching { api.listarSolicitudes() }
            .onSuccess { solicitudes ->
                dao.sincronizar(solicitudes.map { it.toEntity() })
            }
    }

    suspend fun guardar(solicitud: Solicitud): Long {
        val remota = runCatching { api.crearSolicitud(solicitud.toDto()) }
            .getOrNull()
        val entity = (remota?.toSolicitud() ?: solicitud).toEntity()
        return dao.insertar(entity)
    }

    private fun SolicitudEntity.toDomain(): Solicitud = Solicitud(
        id = id,
        equipoId = equipoId,
        descripcion = descripcion,
        estado = estado,
        fotoRuta = fotoRuta
    )

    private fun Solicitud.toEntity(): SolicitudEntity = SolicitudEntity(
        id = id,
        equipoId = equipoId,
        descripcion = descripcion,
        estado = estado,
        fotoRuta = fotoRuta
    )

    private fun com.example.mcmovilcare.model.SolicitudDto.toEntity(): SolicitudEntity =
        toSolicitud().toEntity()
}
