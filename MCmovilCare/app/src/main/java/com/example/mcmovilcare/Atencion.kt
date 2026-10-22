package com.example.mcmovilcare

data class Atencion(
    val id: Int,
    val equipoId: Int,
    val estado: String,
    val diagnostico: String,
    val cotizacion: Int
)