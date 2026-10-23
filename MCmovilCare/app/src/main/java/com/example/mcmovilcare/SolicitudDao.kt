
package com.example.mcmovilcare

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SolicitudDao {

    @Insert
    suspend fun insertar(solicitud: SolicitudEntity): Long

    @Query("SELECT * FROM solicitudes ORDER BY id DESC")
    fun obtenerTodas(): Flow<List<SolicitudEntity>>
}
