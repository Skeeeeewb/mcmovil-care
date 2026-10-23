
package com.example.mcmovilcare

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SolicitudScreen(
    equipo: Equipo,
    onGuardar: (String) -> Unit,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier,
    solicitudViewModel: SolicitudViewModel = viewModel()
) {
    val uiState by solicitudViewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Nueva solicitud",
            style = MaterialTheme.typography.headlineMedium
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = equipo.nombre,
                    style = MaterialTheme.typography.titleLarge
                )

                Text(text = "Marca: ${equipo.marca}")
                Text(text = "Modelo: ${equipo.modelo}")
            }
        }

        OutlinedTextField(
            value = uiState.descripcion,
            onValueChange = {
                solicitudViewModel.actualizarDescripcion(it)
            },
            label = {
                Text("Describe el problema")
            },
            placeholder = {
                Text("Ej: Mi equipo no enciende")
            },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 5,
            isError = uiState.errorDescripcion != null,
            supportingText = {
                uiState.errorDescripcion?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            trailingIcon = {
                if (uiState.errorDescripcion != null) {
                    Icon(
                        imageVector = Icons.Filled.Warning,
                        contentDescription = "Error de validación",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        )

        Button(
            onClick = {
                solicitudViewModel.validarSolicitud()

                if (solicitudViewModel.uiState.value.solicitudValida) {
                    onGuardar(
                        solicitudViewModel.uiState.value.descripcion.trim()
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar solicitud")
        }

        OutlinedButton(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver")
        }
    }
}
