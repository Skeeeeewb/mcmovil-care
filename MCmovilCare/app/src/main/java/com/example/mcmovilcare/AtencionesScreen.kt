
package com.example.mcmovilcare

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.mcmovilcare.ui.theme.MCmovilCareTheme
import androidx.compose.foundation.clickable

@Composable
fun AtencionesScreen(
    atenciones: List<Atencion>,
    modifier: Modifier = Modifier,
    onAtencionClick: (Atencion) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Mis atenciones",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        items(atenciones, key = { atencion -> atencion.id }) { atencion ->
            TarjetaAtencion(
                atencion = atencion,
                onClick = { onAtencionClick(atencion) }
            )
        }
    }
}

@Composable
fun TarjetaAtencion(
    atencion: Atencion,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "Atención N.º ${atencion.id}",
                style = MaterialTheme.typography.titleMedium
            )

            Text(text = "Equipo ID: ${atencion.equipoId}")
            Text(text = "Estado: ${atencion.estado}")
            Text(text = "Diagnóstico: ${atencion.diagnostico}")
            Text(text = "Cotización: $${atencion.cotizacion}")
        }

    }
}

@Preview(showBackground = true)
@Composable
fun AtencionesPreview() {
    val atencionesEjemplo = listOf(
        Atencion(
            id = 1001,
            equipoId = 1,
            estado = "En diagnóstico",
            diagnostico = "Posible falla de almacenamiento",
            cotizacion = 45000
        ),
        Atencion(
            id = 1002,
            equipoId = 2,
            estado = "En reparación",
            diagnostico = "Cambio de pantalla",
            cotizacion = 85000
        ),
        Atencion(
            id = 1003,
            equipoId = 3,
            estado = "Finalizada",
            diagnostico = "Revisión general completada",
            cotizacion = 20000
        )
    )

    MCmovilCareTheme {
        AtencionesScreen(atenciones = atencionesEjemplo)
    }
}

