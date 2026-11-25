package com.example.mcmovilcare.model

import org.junit.Assert.assertEquals
import org.junit.Test

class ApiDtoMapperTest {

    @Test
    fun `mapea equipos atenciones y solicitudes`() {
        val equipo = EquipoDto(1, "Notebook", "Lenovo", "IdeaPad 3", "En diagnóstico").toEquipo()
        val atencion = AtencionDto(1001, 1, "En diagnóstico", "Diagnóstico", 45000).toAtencion()
        val solicitud = SolicitudDto(5, 1, "Revisión del equipo", "Pendiente").toSolicitud()

        assertEquals("Notebook", equipo.nombre)
        assertEquals(45000, atencion.cotizacion)
        assertEquals("Revisión del equipo", solicitud.descripcion)
        assertEquals(1, Solicitud(1, 1, "Revisión del equipo").toDto().equipoId)
    }

    @Test
    fun `mapea decisiones y producto externo`() {
        assertEquals(
            DecisionCotizacion.ACEPTADA,
            DecisionRespuestaDto(1001, "ACEPTADA").toDecision()
        )
        assertEquals(
            DecisionCotizacion.PENDIENTE,
            DecisionRespuestaDto(1001, "INVALIDA").toDecision()
        )

        val producto = ProductoDto("Producto", "Marca", 9.99, "url").toProducto()
        assertEquals("Marca", producto.marca)
        assertEquals(9.99, producto.precio, 0.01)
    }
}
