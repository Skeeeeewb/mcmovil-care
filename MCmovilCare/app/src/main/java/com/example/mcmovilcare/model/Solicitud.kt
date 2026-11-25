package com.example.mcmovilcare.model

data class Solicitud(
    val id: Int,
    val equipoId: Int,
    val descripcion: String,
    val estado: String = "Pendiente"
)
