package com.example.mcmovilcare.model

data class EquipoDto(
    val id: Int = 0,
    val nombre: String = "",
    val marca: String = "",
    val modelo: String = "",
    val estado: String = ""
)

data class AtencionDto(
    val id: Int = 0,
    val equipoId: Int = 0,
    val estado: String = "",
    val diagnostico: String = "",
    val cotizacion: Int = 0
)

data class SolicitudDto(
    val id: Int = 0,
    val equipoId: Int = 0,
    val descripcion: String = "",
    val estado: String = "Pendiente"
)

data class DecisionDto(val decision: String)

data class DecisionRespuestaDto(
    val atencionId: Int = 0,
    val decision: String = "PENDIENTE"
)

data class ProductoDto(
    val title: String = "",
    val brand: String = "",
    val price: Double = 0.0,
    val thumbnail: String = ""
)

data class Producto(
    val titulo: String,
    val marca: String,
    val precio: Double,
    val imagen: String
)

fun EquipoDto.toEquipo(): Equipo = Equipo(id, nombre, marca, modelo, estado)

fun AtencionDto.toAtencion(): Atencion = Atencion(id, equipoId, estado, diagnostico, cotizacion)

fun SolicitudDto.toSolicitud(): Solicitud = Solicitud(id, equipoId, descripcion, estado)

fun Solicitud.toDto(): SolicitudDto = SolicitudDto(id, equipoId, descripcion, estado)

fun DecisionRespuestaDto.toDecision(): DecisionCotizacion =
    runCatching { DecisionCotizacion.valueOf(decision) }
        .getOrDefault(DecisionCotizacion.PENDIENTE)

fun ProductoDto.toProducto(): Producto = Producto(title, brand, price, thumbnail)
