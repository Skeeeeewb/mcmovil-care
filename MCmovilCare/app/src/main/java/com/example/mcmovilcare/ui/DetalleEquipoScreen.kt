package com.example.mcmovilcare.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mcmovilcare.viewmodel.DetalleEquipoUiState

@Composable
fun DetalleEquipoScreen(
    uiState: DetalleEquipoUiState,
    onSolicitarAtencion: (Int) -> Unit,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    uiState.equipo?.let { equipo ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Detalle del equipo",
                style = MaterialTheme.typography.headlineMedium
            )

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = equipo.nombre,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(text = "Marca: ${equipo.marca}")
                    Text(text = "Modelo: ${equipo.modelo}")
                    Text(text = "Estado: ${equipo.estado}")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { onSolicitarAtencion(equipo.id) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Solicitar atención")
            }

            OutlinedButton(
                onClick = onVolver,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver a mis equipos")
            }
        }
    }
}
