package com.example.mcmovilcare.ui

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mcmovilcare.model.Atencion
import com.example.mcmovilcare.ui.theme.MCmovilCareTheme
import com.example.mcmovilcare.viewmodel.AtencionesUiState

@Composable
fun AtencionesScreen(
    uiState: AtencionesUiState,
    onAtencionClick: (Int) -> Unit,
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

        items(uiState.atenciones, key = { atencion -> atencion.id }) { atencion ->
            TarjetaAtencion(
                atencion = atencion,
                onClick = { onAtencionClick(atencion.id) }
            )
        }
    }
}

@Composable
private fun TarjetaAtencion(
    atencion: Atencion,
    onClick: () -> Unit
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
private fun AtencionesPreview() {
    MCmovilCareTheme {
        AtencionesScreen(
            uiState = AtencionesUiState(
                atenciones = listOf(
                    Atencion(
                        id = 1001,
                        equipoId = 1,
                        estado = "En diagnóstico",
                        diagnostico = "Posible falla de almacenamiento",
                        cotizacion = 45000
                    )
                )
            ),
            onAtencionClick = {}
        )
    }
}
