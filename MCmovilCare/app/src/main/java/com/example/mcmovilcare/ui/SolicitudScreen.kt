package com.example.mcmovilcare.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.mcmovilcare.viewmodel.MensajeSolicitud
import com.example.mcmovilcare.viewmodel.SolicitudUiEvent
import com.example.mcmovilcare.viewmodel.SolicitudUiState

@Composable
fun SolicitudScreen(
    uiState: SolicitudUiState,
    onEvent: (SolicitudUiEvent) -> Unit,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            onEvent(SolicitudUiEvent.FotoCapturada(bitmap))
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Nueva solicitud",
            style = MaterialTheme.typography.headlineMedium
        )

        uiState.equipo?.let { equipo ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = equipo.nombre,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text("Marca: ${equipo.marca}")
                    Text("Modelo: ${equipo.modelo}")
                }
            }
        }

        OutlinedTextField(
            value = uiState.descripcion,
            onValueChange = { onEvent(SolicitudUiEvent.DescripcionCambiada(it)) },
            label = { Text("Describe el problema") },
            placeholder = { Text("Ej: Mi equipo no enciende") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 5,
            enabled = !uiState.guardando,
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

        Text(
            text = "Fotografía del problema",
            style = MaterialTheme.typography.titleMedium
        )

        OutlinedButton(
            onClick = { camaraLauncher.launch(null) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Abrir cámara")
        }

        uiState.foto?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Fotografía del equipo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                contentScale = ContentScale.Fit
            )

            OutlinedButton(
                onClick = { onEvent(SolicitudUiEvent.FotoEliminada) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Eliminar fotografía")
            }
        }

        Button(
            onClick = { onEvent(SolicitudUiEvent.Guardar) },
            enabled = !uiState.guardando,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (uiState.guardando) "Guardando..." else "Registrar solicitud"
            )
        }

        uiState.mensaje?.let { mensaje ->
            Text(
                text = when (mensaje) {
                    MensajeSolicitud.Guardada ->
                        "Solicitud registrada correctamente"

                    MensajeSolicitud.Error ->
                        "No se pudo guardar la solicitud"
                },
                color = if (mensaje == MensajeSolicitud.Guardada) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.error
                }
            )
        }

        Text(
            text = "Solicitudes registradas",
            style = MaterialTheme.typography.titleLarge
        )

        if (uiState.solicitudes.isEmpty()) {
            Text("Este equipo todavía no tiene solicitudes.")
        } else {
            uiState.solicitudes.forEach { solicitud ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Solicitud #${solicitud.id}",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(solicitud.descripcion)
                        Text("Estado: ${solicitud.estado}")
                    }
                }
            }
        }

        OutlinedButton(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver")
        }
    }
}
