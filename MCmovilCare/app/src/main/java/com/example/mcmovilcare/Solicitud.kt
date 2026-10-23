package com.example.mcmovilcare

data class Solicitud(
    val id: Int,
    val equipoId: Int,
    val descripcion: String,
    val estado: String = "Pendiente"
)

