
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

@Composable
fun AtencionesScreen(
    atenciones: List<Atencion>,
    modifier: Modifier = Modifier
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
            TarjetaAtencion(atencion = atencion)
        }
    }
}

@Composable
fun TarjetaAtencion(atencion: Atencion) {
    Card(
        modifier = Modifier.fillMaxWidth()
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
