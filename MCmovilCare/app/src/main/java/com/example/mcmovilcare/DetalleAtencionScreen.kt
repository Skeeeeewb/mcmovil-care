
package com.example.mcmovilcare

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mcmovilcare.ui.theme.MCmovilCareTheme

@Composable
fun DetalleAtencionScreen(
    atencion: Atencion,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Detalle de atención",
            style = MaterialTheme.typography.headlineMedium
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Atención N.º ${atencion.id}",
                    style = MaterialTheme.typography.titleLarge
                )

                Text(text = "Equipo ID: ${atencion.equipoId}")
                Text(text = "Estado: ${atencion.estado}")

                Text(
                    text = "Diagnóstico",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(text = atencion.diagnostico)

                Text(
                    text = "Cotización",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(text = "$${atencion.cotizacion}")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetalleAtencionPreview() {
    val atencionEjemplo = Atencion(
        id = 1001,
        equipoId = 1,
        estado = "En diagnóstico",
        diagnostico = "Posible falla de almacenamiento",
        cotizacion = 45000
    )

    MCmovilCareTheme {
        DetalleAtencionScreen(atencion = atencionEjemplo)
    }
}
