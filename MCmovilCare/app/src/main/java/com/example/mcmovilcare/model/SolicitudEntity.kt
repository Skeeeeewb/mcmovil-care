package com.example.mcmovilcare.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "solicitudes")
data class SolicitudEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val equipoId: Int,
    val descripcion: String,
    val estado: String = "Pendiente",
    val fotoRuta: String? = null
)
